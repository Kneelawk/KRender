package com.kneelawk.krender.engine.base.texture;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.texture.MaterialTexture;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.impl.KREConstants;

/**
 * Base {@link MaterialTextureManager} implementation for use in backends.
 */
public class BaseMaterialTextureManager implements MaterialTextureManager {
    /**
     * The max number of textures that can be managed.
     */
    public static final int TEXTURE_COUNT = 0x4000;

    /**
     * The texture location of the 'none' texture.
     */
    public static final Identifier NONE_TEXTURE_ID = KREConstants.prl("/none");

    /**
     * The renderer this texture manager is associated with.
     */
    protected final KRenderer renderer;

    /**
     * Holds all managed textures.
     */
    protected final MaterialTexture[] textures = new MaterialTexture[TEXTURE_COUNT];

    /**
     * Atomic texture id counter.
     */
    protected final AtomicInteger nextId = new AtomicInteger(0);

    /**
     * A map of textures by resource-location.
     */
    protected final ConcurrentHashMap<Identifier, MaterialTexture> textureById = new ConcurrentHashMap<>();

    /**
     * The material texture that represents 'no texture'.
     */
    protected final MaterialTexture noneTexture;

    /**
     * The default missing material texture.
     */
    protected final MaterialTexture missingTexture;

    /**
     * The block atlas texture.
     */
    protected final MaterialTexture blockAtlasTexture;

    /**
     * Final reference to this manager's create-texture method.
     */
    protected final Function<Identifier, MaterialTexture> createTexture = this::createTexture;

    /**
     * The backend's texture factory.
     */
    protected final TextureFactory textureFactory;

    /**
     * Iterable of all textures.
     */
    protected final Iterable<MaterialTexture> textureIter = new Iterable<>() {
        @Override
        public @NotNull Iterator<MaterialTexture> iterator() {
            return new TextureIterator();
        }
    };

    /**
     * Constructs a new {@link BaseMaterialTextureManager}.
     *
     * @param renderer       the renderer that this manager is associated with.
     * @param textureFactory the factory for the backend's {@link MaterialTexture} implementation.
     */
    public BaseMaterialTextureManager(KRenderer renderer, TextureFactory textureFactory) {
        this.renderer = renderer;
        this.textureFactory = textureFactory;

        noneTexture = textureById(NONE_TEXTURE_ID);
        missingTexture = textureById(MissingTextureAtlasSprite.getLocation());
        blockAtlasTexture = textureById(TextureAtlas.LOCATION_BLOCKS);
    }

    private MaterialTexture createTexture(Identifier textureId) {
        int id = nextId.getAndIncrement();
        MaterialTexture tex = textureFactory.create(textureId, id);
        textures[id] = tex;
        return tex;
    }

    @Override
    public MaterialTexture textureByIntId(int id) {
        if (id < -1 || id >= TEXTURE_COUNT) throw new IllegalArgumentException("Invalid material int id");
        MaterialTexture tex = textures[id];
        if (tex == null)
            throw new NoSuchElementException("Attempted to request a texture " + id +
                " that does not exist. This likely indicates material corruption.");
        return tex;
    }

    @Override
    public int maxIntId() {
        return TEXTURE_COUNT;
    }

    @Override
    public Iterable<MaterialTexture> allTextures() {
        return textureIter;
    }

    @Override
    public MaterialTexture none() {
        return noneTexture;
    }

    @Override
    public MaterialTexture missing() {
        return missingTexture;
    }

    @Override
    public MaterialTexture blockAtlas() {
        return blockAtlasTexture;
    }

    @Override
    public MaterialTexture textureById(Identifier id) {
        return textureById.computeIfAbsent(id, createTexture);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return renderer;
    }

    /**
     * A backend-implemented factory for material textures.
     */
    public interface TextureFactory {
        /**
         * Create a material texture specific to the backend being implemented.
         *
         * @param textureId the resource location of the backing texture.
         * @param intId     the integer id of the new texture.
         * @return a new texture.
         */
        MaterialTexture create(Identifier textureId, int intId);
    }

    private class TextureIterator implements Iterator<MaterialTexture> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            // impl-wise materials never get deleted and our array is dense, so we can count on this
            return textures[index] != null;
        }

        @Override
        public MaterialTexture next() {
            MaterialTexture mat = textures[index++];
            if (mat == null) throw new NoSuchElementException("This texture manager currently has no more elements.");
            return mat;
        }
    }
}
