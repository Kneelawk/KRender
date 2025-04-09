package com.kneelawk.krender.engine.backend.neoforge.impl.mesh;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.base.buffer.TransformStack;

public class NFTransformStack extends TransformStack {
    public NFTransformStack(KRenderer renderer) {
        super(renderer, NFTransformingQuadEmitter::new);
    }
}
