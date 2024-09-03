package com.kneelawk.krender.engine.impl.texture;

import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kneelawk.krender.engine.api.texture.SpriteFinder;

/**
 * General-purpose sprite finder that can handle strangely shaped sprites and sprites whose size is not a power of 2.
 * <p>
 * This uses a quad-tree where a single sprite may be stored under multiple quadrants to allow for sprites that do not
 * fit evenly into a single quad.
 * <p>
 * Every node has a number of sprites that are just linearly searched. Once that array of sprites is full, then sprites
 * are added to child nodes.
 */
public class QuadTreeSpriteFinder implements SpriteFinder {
    private final Node root = new Node(new Region(0f, 0f, 1f, 1f));
    private final TextureAtlas atlas;

    public QuadTreeSpriteFinder(TextureAtlas atlas, TextureAtlasSprite[] sprites) {
        this.atlas = atlas;

        // add all sprites from the atlas
        for (TextureAtlasSprite sprite : sprites) {
            root.add(sprite);
        }
    }

    @Override
    public TextureAtlasSprite find(float u, float v) {
        TextureAtlasSprite sprite = root.find(u, v);
        if (sprite == null) return atlas.getSprite(MissingTextureAtlasSprite.getLocation());
        return sprite;
    }

    private record Region(float u0, float v0, float u1, float v1) {
        public boolean contains(float u, float v) {
            return u >= u0 && u < u1 && v >= v0 && v < v1;
        }

        public boolean overlaps(Region other) {
            return !(other.u1 < u0 || other.u0 > u1 || other.v0 > v1 || other.v1 < v0);
        }

        public boolean overlaps(TextureAtlasSprite sprite) {
            return !(sprite.getU1() < u0 || sprite.getU0() > u1 || sprite.getV0() > v1 || sprite.getV1() < v0);
        }

        public Region child(int index) {
            float childWidth = (u1 - u0) / 2f;
            float childHeight = (v1 - v0) / 2f;

            return switch (index) {
                case 0 -> new Region(u0, v0, u0 + childWidth, v0 + childHeight);
                case 1 -> new Region(u0, v0 + childHeight, u0 + childWidth, v1);
                case 2 -> new Region(u0 + childWidth, v0 + childHeight, u1, v1);
                case 3 -> new Region(u0 + childWidth, v0, u1, v0 + childHeight);
                default -> throw new IllegalStateException("Unexpected child index: " + index);
            };
        }
    }

    private static class Node {
        private static final int MAX_SPRITE_COUNT = 3;
        private static final int CHILD_COUNT = 4;

        final Region region;
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[MAX_SPRITE_COUNT];
        int spriteCount = 0;
        Node[] children = new Node[CHILD_COUNT];

        private Node(Region region) {this.region = region;}

        TextureAtlasSprite find(float u, float v) {
            // check sprites first
            for (int i = 0; i < spriteCount; i++) {
                TextureAtlasSprite sprite = sprites[i];
                if (contains(sprite, u, v)) {
                    return sprite;
                }
            }

            // check child nodes
            Node child = getChild(u, v);

            if (child == null) {
                return null;
            } else {
                return child.find(u, v);
            }
        }

        private Node getChild(float u, float v) {
            float halfU = (region.u0() + region.u1()) / 2f;
            float halfV = (region.v0() + region.v1()) / 2f;

            Node child;
            if (u < halfU) {
                if (v < halfV) {
                    child = children[0];
                } else {
                    child = children[1];
                }
            } else {
                if (v < halfV) {
                    child = children[3];
                } else {
                    child = children[2];
                }
            }
            return child;
        }

        void add(TextureAtlasSprite sprite) {
            if (spriteCount < MAX_SPRITE_COUNT) {
                sprites[spriteCount++] = sprite;
            } else {
                for (int i = 0; i < CHILD_COUNT; i++) {
                    Region childRegion;
                    Node child = children[i];
                    if (child == null) {
                        childRegion = region.child(i);
                    } else {
                        childRegion = child.region;
                    }

                    if (childRegion.overlaps(sprite)) {
                        if (child == null) {
                            child = new Node(childRegion);
                            children[i] = child;
                        }

                        child.add(sprite);
                    }
                }
            }
        }
    }

    private static boolean contains(TextureAtlasSprite sprite, float u, float v) {
        return u >= sprite.getU0() && u < sprite.getU1() && v >= sprite.getV0() && v < sprite.getV1();
    }
}
