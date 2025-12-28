package com.kneelawk.krender.engine.base.buffer;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.QuadView;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.api.util.bits.Bits;
import com.kneelawk.krender.engine.api.util.bits.BooleanBits;
import com.kneelawk.krender.engine.api.util.bits.EnumBits;
import com.kneelawk.krender.engine.api.util.bits.IntBits;

// This class is largely based on the Fabric Render Indigo EncodingFormat.

/**
 * Static values useful for encoding and decoding quads when using the base implementations.
 */
public final class BaseQuadFormat {
    private static final Cache<MaterialTextureManager, BaseQuadFormat> cache =
        CacheBuilder.newBuilder().weakKeys().build();

    /**
     * Gets a base quad format for the given material manager.
     *
     * @param manager the manager to get the quad format for.
     * @return the quad format for the given material manager.
     */
    public static BaseQuadFormat get(MaterialTextureManager manager) {
        try {
            return cache.get(manager, () -> new BaseQuadFormat(Mth.ceillog2(manager.maxIntId())));
        } catch (ExecutionException e) {
            // should never happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a base quad format for the given renderer.
     *
     * @param renderer the renderer implementation supplying the texture manager.
     * @return the material format for the given renderer.
     */
    public static BaseQuadFormat get(KRenderer renderer) {
        return get(renderer.textureManager());
    }

    /**
     * The int index of the x position in the vertex.
     */
    public static final int VERTEX_X = 0;
    /**
     * The int index of the y position in the vertex.
     */
    public static final int VERTEX_Y = 1;
    /**
     * The int index of the z position in the vertex.
     */
    public static final int VERTEX_Z = 2;
    /**
     * The int index of the color in the vertex.
     */
    public static final int VERTEX_COLOR = 3;
    /**
     * The int index of the u tex-coord in the vertex.
     */
    public static final int VERTEX_U = 4;
    /**
     * The int index of the v tex-coord in the vertex.
     */
    public static final int VERTEX_V = 5;
    /**
     * The int index of the lightmap value in the vertex.
     */
    public static final int VERTEX_LIGHTMAP = 6;
    /**
     * The int index of the compressed normal in the vertex.
     */
    public static final int VERTEX_NORMAL = 7;
    /**
     * The number of ints in a vertex.
     */
    public static final int VERTEX_STRIDE = 8;

    /**
     * The number of ints in a quad.
     */
    public static final int QUAD_STRIDE = VERTEX_STRIDE * 4;
    /**
     * The number of bytes in a quad.
     */
    public static final int QUAD_STRIDE_BYTES = QUAD_STRIDE * 4;

    /**
     * The number of bits in the normal presence flags.
     */
    public static final int NORMALS_COUNT = 4;

    static {
        // We check that our vertices are the same size and format as vanilla's because that makes translation a lot easier
        Preconditions.checkState(VERTEX_STRIDE == QuadView.VANILLA_VERTEX_STRIDE,
            "KRender Engine base vertex format (%s ints) is incompatible with vanilla vertex format (%s ints)",
            VERTEX_STRIDE, QuadView.VANILLA_VERTEX_STRIDE);
    }

    /**
     * The cull direction.
     */
    public final EnumBits<@Nullable Direction> cull;
    /**
     * The light direction.
     */
    public final EnumBits<Direction> light;
    /**
     * The normal presence flags.
     */
    public final IntBits normalsInt;
    /**
     * The individual normal presence flags.
     */
    public final BooleanBits[] normals;
    /**
     * The geometry flags.
     */
    public final IntBits geometry;
    /**
     * The terrain render layer.
     */
    public final EnumBits<@Nullable ChunkSectionLayer> renderLayer;
    /**
     * Whether the quad is emissive.
     */
    public final BooleanBits emissive;
    /**
     * Whether the quad has diffuse shading disabled.
     */
    public final BooleanBits diffuseDisabled;
    /**
     * Whether the quad has ambient occlusion shading.
     */
    public final EnumBits<TriState> ambientOcclusion;
    /**
     * The foil type quad's foil type.
     */
    public final EnumBits<ItemStackRenderState.@Nullable FoilType> foilType;
    /**
     * The material for a given quad.
     */
    public final IntBits texture;

    /**
     * The int index of the bits in the header.
     */
    public final int headerBits;
    /**
     * The int index of the face normal in the header.
     */
    public final int headerFaceNormal;
    /**
     * The int index of the color index in the header.
     */
    public final int headerTintIndex;
    /**
     * The int index of the tag in the header.
     */
    public final int headerTag;
    /**
     * The number of ints in the header.
     */
    public final int headerStride;

    /**
     * The number of ints in a quad, including the quad's header.
     */
    public final int totalStride;

    /**
     * Empty vertex data.
     */
    public final int[] empty;

    private BaseQuadFormat(int textureBits) {
        cull = EnumBits.of(Direction.class, true);
        light = EnumBits.ofI(cull, Direction.class, false);
        normalsInt = IntBits.ofI(light, NORMALS_COUNT);
        geometry = IntBits.ofI(normalsInt, GeometryHelper.FLAG_BIT_COUNT);
        renderLayer = EnumBits.ofI(geometry, ChunkSectionLayer.class, true);
        emissive = BooleanBits.ofI(renderLayer);
        ambientOcclusion = EnumBits.ofI(emissive, TriState.class, false);
        diffuseDisabled = BooleanBits.ofI(ambientOcclusion);
        foilType = EnumBits.ofI(diffuseDisabled, ItemStackRenderState.FoilType.class, true);
        texture = IntBits.ofI(foilType, textureBits);

        normals = new BooleanBits[NORMALS_COUNT];
        Bits prev = light;
        for (int i = 0; i < NORMALS_COUNT; i++) {
            prev = normals[i] = BooleanBits.ofI(prev);
        }

        headerBits = 0;
        headerFaceNormal = headerBits + texture.unitIndex() + 1;
        headerTintIndex = headerFaceNormal + 1;
        headerTag = headerTintIndex + 1;
        headerStride = headerTag + 1;

        totalStride = headerStride + QUAD_STRIDE;
        empty = new int[totalStride];
    }

    /**
     * Gets the entire set of normals flags when given header bits.
     *
     * @param bits the header bits.
     * @return the normal flags.
     */
    public int getNormalFlags(int bits) {
        return normalsInt.getI(bits);
    }

    /**
     * Gets whether a specific normal is present when given header bits.
     *
     * @param bits        the header bits.
     * @param vertexIndex the index of the normal to check.
     * @return whether the normal exists.
     */
    public boolean isNormalPresent(int bits, int vertexIndex) {
        return normals[vertexIndex].getI(bits);
    }

    /**
     * Sets whether a specific normal is present when given existing header bits, the index of the normal, and whether
     * it should be present.
     *
     * @param bits        the existing header bits.
     * @param vertexIndex the index of the vertex the normal is for.
     * @param present     whether the normal should be marked as present.
     * @return the new header bits.
     */
    public int setNormalPresent(int bits, int vertexIndex, boolean present) {
        return normals[vertexIndex].setI(bits, present);
    }
}
