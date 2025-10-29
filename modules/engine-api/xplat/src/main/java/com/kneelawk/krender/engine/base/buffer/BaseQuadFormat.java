package com.kneelawk.krender.engine.base.buffer;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.QuadView;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.api.util.DirectionIds;
import com.kneelawk.krender.engine.api.util.bits.Bits;
import com.kneelawk.krender.engine.api.util.bits.BooleanBits;
import com.kneelawk.krender.engine.api.util.bits.IntBits;
import com.kneelawk.krender.engine.base.material.BaseMaterialFormat;

import static com.kneelawk.krender.engine.api.util.DirectionIds.DIRECTION_BIT_COUNT;
import static com.kneelawk.krender.engine.api.util.DirectionIds.DIRECTION_MASK;

// This class is largely based on the Fabric Render Indigo EncodingFormat.

/**
 * Static values useful for encoding and decoding quads when using the base implementations.
 */
public final class BaseQuadFormat {
    private static final Cache<MaterialManager, BaseQuadFormat> cache =
        CacheBuilder.newBuilder().weakKeys().build();

    /**
     * Gets a base quad format for the given material manager.
     *
     * @param manager the manager to get the quad format for.
     * @return the quad format for the given material manager.
     */
    public static BaseQuadFormat get(MaterialManager manager) {
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
        return get(renderer.materialManager());
    }

    /**
     * The int index of the bits in the header.
     */
    public static final int HEADER_BITS = 0;
    /**
     * The int index of the face normal in the header.
     */
    public static final int HEADER_FACE_NORMAL = 1;
    /**
     * The int index of the color index in the header.
     */
    public static final int HEADER_TINT_INDEX = 2;
    /**
     * The int index of the tag in the header.
     */
    public static final int HEADER_TAG = 3;
    /**
     * The number of ints in the header.
     */
    public static final int HEADER_STRIDE = 4; // 4 ints

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
     * The number of ints in a quad, including the quad's header.
     */
    public static final int TOTAL_STRIDE = HEADER_STRIDE + QUAD_STRIDE;

    /**
     * Empty vertex data.
     */
    public static final int[] EMPTY = new int[TOTAL_STRIDE];

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
    public final IntBits cull;
    /**
     * The light direction.
     */
    public final IntBits light;
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
     * The material for a given quad.
     */
    public final IntBits material;

    private BaseQuadFormat(int materialBits) {
        cull = IntBits.of(DIRECTION_BIT_COUNT);
        light = IntBits.ofNoSplitI(cull, DIRECTION_BIT_COUNT);
        normalsInt = IntBits.ofNoSplitI(light, 4);
        geometry = IntBits.ofNoSplitI(normalsInt, GeometryHelper.FLAG_BIT_COUNT);
        material = IntBits.ofNoSplitI(geometry, materialBits);

        normals = new BooleanBits[NORMALS_COUNT];
        Bits prev = light;
        for (int i = 0; i < NORMALS_COUNT; i++) {
            prev = normals[i] = BooleanBits.ofI(prev);
        }

        // Check that there are enough bits in the header to hold everything
        Preconditions.checkState(getHeaderBitCount() <= 32,
            "KRender Engine base quad format header bit count (%s) has exceeded 32 bits", getHeaderBitCount());
    }

    /**
     * {@return the number of bits in the header}
     */
    public int getHeaderBitCount() {
        return material.fullShift() + material.bitCount();
    }

    /**
     * Gets the direction cull face when given header bits.
     *
     * @param bits the header bits.
     * @return the direction cull face.
     */
    public @Nullable Direction getCullFace(int bits) {
        return DirectionIds.idToDirection(cull.getI(bits));
    }

    /**
     * Sets the cull face when given existing header bits and the new cull face direction.
     *
     * @param bits the existing header bits.
     * @param face the new cull face direction.
     * @return the header bits with the new cull face.
     */
    public int setCullFace(int bits, @Nullable Direction face) {
        return cull.setI(bits, DirectionIds.directionToId(face));
    }

    /**
     * Gets the light face when given header bits.
     *
     * @param bits the header bits.
     * @return the light face.
     */
    public Direction getLightFace(int bits) {
        final Direction direction = DirectionIds.idToDirection(light.getI(bits));
        assert direction != null;
        return direction;
    }

    /**
     * Sets the light face when given existing header bits and the new light face direction.
     *
     * @param bits the existing header bits.
     * @param face the new light face direction.
     * @return the header bits with the new light face.
     */
    public int setLightFace(int bits, Direction face) {
        return light.setI(bits, DirectionIds.directionToId(face));
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

    /**
     * Gets the geometry flags from the header bits.
     *
     * @param bits the header bits.
     * @return the geometry flags.
     */
    public int getGeometryFlags(int bits) {
        return geometry.getI(bits);
    }

    /**
     * Sets the geometry flags when given existing header bits and the new geometry flags.
     *
     * @param bits          the existing header bits.
     * @param geometryFlags the new geometry flags.
     * @return the new header bits.
     */
    public int setGeometryFlags(int bits, int geometryFlags) {
        return geometry.setI(bits, geometryFlags);
    }

    /**
     * Gets the render material from the given manager when given header bits.
     *
     * @param bits    the header bits.
     * @param manager the material manager.
     * @return the render material.
     */
    public RenderMaterial getMaterial(int bits, MaterialManager manager) {
        return manager.materialByIntId(material.getI(bits));
    }

    /**
     * Sets the render material when given existing header bits and the new render material.
     *
     * @param bits     the existing header bits.
     * @param material the new render material.
     * @return the new header bits.
     */
    public int setMaterial(int bits, RenderMaterial material) {
        return this.material.setI(bits, material.intId());
    }
}
