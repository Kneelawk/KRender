package com.kneelawk.krender.engine.api.buffer;

import org.jetbrains.annotations.Nullable;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import com.kneelawk.krender.engine.api.material.RenderMaterial;

/**
 * Per-quad Fabric Render API style model emitter.
 */
public interface QuadEmitter extends QuadView, QuadSink {
    /**
     * No-rotation bake options.
     */
    int BAKE_ROTATE_NONE = 0;

    /**
     * Rotate the texture clockwise by 90 degrees.
     */
    int BAKE_ROTATE_90 = 1;

    /**
     * Rotate the texture clockwise by 180 degrees.
     */
    int BAKE_ROTATE_180 = 2;

    /**
     * Rotate the texture clockwise by 270 degrees.
     */
    int BAKE_ROTATE_270 = 3;

    /**
     * Derive texture coordinates based on vertex position.
     */
    int BAKE_LOCK_UV = 4;

    /**
     * Flip U texture coordinates.
     */
    int BAKE_FLIP_U = 8;

    /**
     * Flip V texture coordinates.
     */
    int BAKE_FLIP_V = 16;

    /**
     * Indicates that UV values are 0-16 like they are in model jsons instead of 0-1 like they are when using sprites directly.
     */
    int BAKE_DENORMALIZED = 32;

    /**
     * Finishes one quad, emitting it to the backend and moving on to the next quad.
     *
     * @return this quad emitter.
     */
    QuadEmitter emit();

    /**
     * Sets a vertex position of the current quad.
     *
     * @param vertexIndex the index of the vertex to set the position of.
     * @param x           the x position.
     * @param y           the y position.
     * @param z           the z position.
     * @return this quad emitter.
     */
    QuadEmitter setPos(int vertexIndex, float x, float y, float z);

    /**
     * Sets a coordinate value of a vertex of the current quad.
     *
     * @param vertexIndex     the index of the vertex to set the position of.
     * @param coordinateIndex the index of the value within the vector to set.
     * @param value           the value to set.
     * @return this quad emitter.
     */
    QuadEmitter setPosByIndex(int vertexIndex, int coordinateIndex, float value);

    /**
     * Sets a vertex position of the current quad.
     *
     * @param vertexIndex the index of the vertex to set the position of.
     * @param pos         the position to set.
     * @return this quad emitter.
     */
    default QuadEmitter setPos(int vertexIndex, Vector3f pos) {
        return setPos(vertexIndex, pos.x, pos.y, pos.z);
    }

    /**
     * Sets a vertex position of the current quad.
     *
     * @param vertexIndex the index of the vertex to set the position of.
     * @param pos         the position to set.
     * @return this quad emitter.
     */
    default QuadEmitter setPos(int vertexIndex, Vector3fc pos) {
        return setPos(vertexIndex, pos.x(), pos.y(), pos.z());
    }

    /**
     * Set a vertex color in ARGB format.
     *
     * @param vertexIndex the index of the vertex to set the color of.
     * @param color       the color to set.
     * @return this quad emitter.
     */
    QuadEmitter setColor(int vertexIndex, int color);

    /**
     * Convenience method to set the colors of all vertices at once.
     *
     * @param c0 vertex 0 color.
     * @param c1 vertex 1 color.
     * @param c2 vertex 2 color.
     * @param c3 vertex 3 color.
     * @return this quad emitter.
     */
    default QuadEmitter setQuadColor(int c0, int c1, int c2, int c3) {
        setColor(0, c0);
        setColor(1, c1);
        setColor(2, c2);
        setColor(3, c3);
        return this;
    }

    /**
     * Sets the texture coordinates of a vertex in the current quad.
     *
     * @param vertexIndex the index of the vertex to set the texture coordinates of.
     * @param u           the horizontal texture coordinate.
     * @param v           the vertical texture coordinate.
     * @return this quad emitter.
     */
    QuadEmitter setUv(int vertexIndex, float u, float v);

    /**
     * Sets a texture coordinate value of a vertex of the current quad.
     *
     * @param vertexIndex     the index of the vertex to set the texture coordinate of.
     * @param coordinateIndex the index of the value within the vector to set.
     * @param value           the value to set.
     * @return this quad emitter.
     */
    QuadEmitter setUvByIndex(int vertexIndex, int coordinateIndex, float value);

    /**
     * Sets the texture coordinates of a vertex in the current quad.
     *
     * @param vertexIndex the index of the vertex to set the texture coordinates of.
     * @param uv          the texture coordinate vector.
     * @return this quad emitter.
     */
    default QuadEmitter setUv(int vertexIndex, Vector2f uv) {
        return setUv(vertexIndex, uv.x, uv.y);
    }

    /**
     * Sets the texture coordinates of a vertex in the current quad.
     *
     * @param vertexIndex the index of the vertex to set the texture coordinates of.
     * @param uv          the texture coordinate vector.
     * @return this quad emitter.
     */
    default QuadEmitter setUv(int vertexIndex, Vector2fc uv) {
        return setUv(vertexIndex, uv.x(), uv.y());
    }

    /**
     * Assigns atlas-based texture coordinates to the current quad.
     * <p>
     * Unless {@link #BAKE_LOCK_UV} is passed to {@code bakeFlags}, then the new coordinates within the sprite will be
     * based on the previous uv coordinates for this quad. If {@link #BAKE_DENORMALIZED} is passed to {@code bakeFlags},
     * then the quad's previous uv coordinates are expected to be denormalized (0.0-16.0), like they are in model json
     * files. Otherwise, uv coordinates are expected to be normalized (0.0-1.0).
     *
     * @param sprite    the sprite to bake the texture coordinates of.
     * @param bakeFlags the bake flags that control locking, rotation, interpolation, etc.
     * @return this quad emitter.
     */
    QuadEmitter spriteBake(TextureAtlasSprite sprite, int bakeFlags);

    /**
     * Set the minimum vanilla light map values a vertex can have.
     *
     * @param vertexIndex the index of the vertex to set the light map value of.
     * @param lightmap    the vanilla light map value to set.
     * @return this quad emitter.
     */
    QuadEmitter setLightmap(int vertexIndex, int lightmap);

    /**
     * Convenience method to set all light map values at once.
     *
     * @param b0 the vertex 0 lightmap.
     * @param b1 the vertex 1 lightmap.
     * @param b2 the vertex 2 lightmap.
     * @param b3 the vertex 3 lightmap.
     * @return this quad emitter.
     */
    default QuadEmitter setLightmap(int b0, int b1, int b2, int b3) {
        setLightmap(0, b0);
        setLightmap(1, b1);
        setLightmap(2, b2);
        setLightmap(3, b3);
        return this;
    }

    /**
     * Adds a vertex normal to a vertex on the current quad.
     *
     * @param vertexIndex the index of the vertex to add the normal to.
     * @param x           the x component of the normal.
     * @param y           the y component of the normal.
     * @param z           the z component of the normal.
     * @return this quad emitter.
     */
    QuadEmitter setNormal(int vertexIndex, float x, float y, float z);

    /**
     * Sets a normal value of a vertex of the current quad.
     *
     * @param vertexIndex     the index of the vertex to set the normal of.
     * @param coordinateIndex the index of the value within the vector to set.
     * @param value           the value to set.
     * @return this quad emitter.
     */
    QuadEmitter setNormalByIndex(int vertexIndex, int coordinateIndex, float value);

    /**
     * Adds a vertex normal to a vertex on the current quad.
     *
     * @param vertexIndex the index of the vertex to add the normal to.
     * @param normal      the normal vector.
     * @return this quad emitter.
     */
    default QuadEmitter setNormal(int vertexIndex, Vector3f normal) {
        return setNormal(vertexIndex, normal.x, normal.y, normal.z);
    }

    /**
     * Adds a vertex normal to a vertex on the current quad.
     *
     * @param vertexIndex the index of the vertex to add the normal to.
     * @param normal      the normal vector.
     * @return this quad emitter.
     */
    default QuadEmitter setNormal(int vertexIndex, Vector3fc normal) {
        return setNormal(vertexIndex, normal.x(), normal.y(), normal.z());
    }

    /**
     * Invalidates the normal for a given vertex on the current quad.
     *
     * @param vertexIndex the index of the vertex to invalidate the normal on.
     * @return this quad emitter.
     */
    QuadEmitter removeNormal(int vertexIndex);

    /**
     * Sets the cull face for this quad.
     *
     * @param face the direction that this quad will be culled from if any.
     * @return this quad emitter.
     */
    QuadEmitter setCullFace(@Nullable Direction face);

    /**
     * Set the face should be seen as equivalent to the normal of the whole quad.
     *
     * @param face the direction closest to where this quad's normal faces.
     * @return this quad emitter.
     */
    QuadEmitter setNominalFace(@Nullable Direction face);

    /**
     * Sets this quad emitter's default material for new quads.
     *
     * @param material the emitter's new default material.
     * @return this quad emitter.
     */
    QuadEmitter setDefaultMaterial(RenderMaterial material);

    /**
     * Sets this quad's material.
     * <p>
     * If no material is set, then this quad will use the default material.
     *
     * @param material the new material for this quad.
     * @return this quad emitter.
     */
    QuadEmitter setMaterial(@Nullable RenderMaterial material);

    /**
     * Sets this quad's color index.
     *
     * @param colorIndex the color index for this quad.
     * @return this quad emitter.
     */
    QuadEmitter setColorIndex(int colorIndex);

    /**
     * Adds an extra user-defined integer tag to this quad.
     *
     * @param tag the integer tag.
     * @return this quad emitter.
     */
    QuadEmitter setTag(int tag);

    /**
     * Copies vanilla quad properties to the current quad.
     *
     * @param quadData   the array of vanilla quad data.
     * @param startIndex the starting index within the quad data array.
     * @return this quad emitter.
     */
    QuadEmitter fromVanilla(int[] quadData, int startIndex);

    /**
     * Copies vanilla quad properties to the current quad.
     *
     * @param quad     the vanilla quad to copy from.
     * @param material the material to use when copying the quad. Note: diffuse-shading only applies if both the
     *                 material and the quad have it enabled.
     * @param cullFace the cull-face of the vanilla quad.
     * @return this quad emitter.
     */
    QuadEmitter fromVanilla(BakedQuad quad, RenderMaterial material, @Nullable Direction cullFace);

    @Override
    default QuadEmitter asQuadEmitter() {
        return this;
    }
}
