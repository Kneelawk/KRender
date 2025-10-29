package com.kneelawk.krender.engine.backend.test.impl;

import com.kneelawk.commonevents.api.Listen;
import com.kneelawk.commonevents.api.Scan;
import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;

@Scan(side = Scan.Side.CLIENT)
public class TestEntryPoint {
    @Listen(BackendRegistrationCallback.class)
    public static void register(BackendRegistrationCallback.Context ctx) {
        ctx.registerBackend(TestBackend.INSTANCE);
    }
}
