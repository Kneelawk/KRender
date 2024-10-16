package com.kneelawk.krender.engine.backend.frapi.impl;

import com.kneelawk.commonevents.api.Listen;
import com.kneelawk.commonevents.api.Scan;
import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;

@Scan(side = Scan.Side.CLIENT)
public class FRAPIEntryPoint {
    @Listen(BackendRegistrationCallback.class)
    public static void register(BackendRegistrationCallback.Context ctx) {
        if (ctx.isModLoaded("fabric-renderer-api-v1") || ctx.isModLoaded("fabric_renderer_api_v1")) {
            ctx.registerBackend("com.kneelawk.krender.engine.backend.frapi.impl.FRAPIPredicate",
                "com.kneelawk.krender.engine.backend.frapi.impl.FRAPIBackend");
        }
    }
}
