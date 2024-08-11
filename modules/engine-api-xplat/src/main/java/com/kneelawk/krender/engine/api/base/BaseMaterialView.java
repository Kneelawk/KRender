package com.kneelawk.krender.engine.api.base;

import net.minecraft.util.Mth;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.api.material.MaterialView;

/**
 * Base implementation of {@link MaterialView} that can be used for platforms that don't have an existing implementation.
 */
public abstract class BaseMaterialView implements MaterialView {
    private static final BlendMode[] BLEND_MODES = BlendMode.values();
    private static final int BLEND_MODE_COUNT = BLEND_MODES.length;
    private static final TriState[] TRI_STATES = TriState.values();
    private static final int TRI_STATE_COUNT = TRI_STATES.length;

    /**
     * Blend mode bit length.
     */
    protected static final int BLEND_MODE_BIT_LENGTH = Mth.ceillog2(BLEND_MODE_COUNT);
    /**
     * Color index disable flag bit length.
     */
    protected static final int COLOR_INDEX_BIT_LENGTH = 1;
    /**
     * Emissive flag bit length.
     */
    protected static final int EMISSIVE_BIT_LENGTH = 1;
    /**
     * Diffuse shading disable flag bit length.
     */
    protected static final int DIFFUSE_BIT_LENGTH = 1;
    /**
     * Ambient occlusion bit length.
     */
    protected static final int AO_BIT_LENGTH = Mth.ceillog2(TRI_STATE_COUNT);

    /**
     * Blend mode bit offset.
     */
    protected static final int BLEND_MODE_BIT_OFFSET = 0;
    /**
     * Color index disable flag bit offset.
     */
    protected static final int COLOR_INDEX_BIT_OFFSET = BLEND_MODE_BIT_OFFSET + BLEND_MODE_BIT_LENGTH;
    /**
     * Emissive flag bit length.
     */
    protected static final int EMISSIVE_BIT_OFFSET = COLOR_INDEX_BIT_OFFSET + COLOR_INDEX_BIT_LENGTH;
    /**
     * Diffuse shading disable flag bit length.
     */
    protected static final int DIFFUSE_BIT_OFFSET = EMISSIVE_BIT_OFFSET + EMISSIVE_BIT_LENGTH;
    /**
     * Ambient occlusion bit length.
     */
    protected static final int AO_BIT_OFFSET = DIFFUSE_BIT_OFFSET + DIFFUSE_BIT_LENGTH;
    /**
     * Total used bits in a material.
     */
    protected static final int TOTAL_BIT_LENGTH = AO_BIT_OFFSET + AO_BIT_LENGTH;

    /**
     * Blend mode bit mask.
     */
    protected static final int BLEND_MODE_MASK = bitMask(BLEND_MODE_BIT_LENGTH, BLEND_MODE_BIT_OFFSET);
    /**
     * Color index disable flag bit mask.
     */
    protected static final int COLOR_INDEX_FLAG = bitMask(COLOR_INDEX_BIT_LENGTH, COLOR_INDEX_BIT_OFFSET);
    /**
     * Emissive flag bit mask.
     */
    protected static final int EMISSIVE_FLAG = bitMask(EMISSIVE_BIT_LENGTH, EMISSIVE_BIT_OFFSET);
    /**
     * Diffuse shading disable flag bit mask.
     */
    protected static final int DIFFUSE_FLAG = bitMask(DIFFUSE_BIT_LENGTH, DIFFUSE_BIT_OFFSET);
    /**
     * Ambient occlusion bit mask.
     */
    protected static final int AO_MASK = bitMask(AO_BIT_LENGTH, AO_BIT_OFFSET);

    /**
     * Creates a bit mask given the mask length and mask offset.
     *
     * @param len the mask length.
     * @param off the mask offset.
     * @return the bit mask.
     */
    protected static int bitMask(int len, int off) {
        return ((1 << len) - 1) << off;
    }

    /**
     * Determines whether the given material bits are valid.
     *
     * @param bits the bits to verify.
     * @return {@code true} if the given bits are a valid material.
     */
    protected static boolean isValid(int bits) {
        int blendMode = (bits & BLEND_MODE_MASK) >>> BLEND_MODE_BIT_OFFSET;
        int ao = (bits & AO_MASK) >>> AO_BIT_OFFSET;

        return blendMode < BLEND_MODE_COUNT && ao < TRI_STATE_COUNT;
    }

    /**
     * This material's bits.
     */
    protected int bits;

    /**
     * Creates a new {@link BaseMaterialView} with the given bits.
     *
     * @param bits the bits representing this material.
     */
    public BaseMaterialView(int bits) {
        this.bits = bits;
    }

    /**
     * {@return this material view's bits}
     */
    public int getBits() {
        return bits;
    }

    @Override
    public BlendMode getBlendMode() {
        return BLEND_MODES[(bits & BLEND_MODE_MASK) >>> BLEND_MODE_BIT_OFFSET];
    }

    @Override
    public boolean isColorIndexDisabled() {
        return (bits & COLOR_INDEX_FLAG) != 0;
    }

    @Override
    public boolean isEmissive() {
        return (bits & EMISSIVE_FLAG) != 0;
    }

    @Override
    public boolean isDiffuseDisabled() {
        return (bits & DIFFUSE_FLAG) != 0;
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return TRI_STATES[(bits & AO_MASK) >>> AO_BIT_OFFSET];
    }
}
