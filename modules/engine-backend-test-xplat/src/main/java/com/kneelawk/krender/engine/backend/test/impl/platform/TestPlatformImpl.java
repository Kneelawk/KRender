package com.kneelawk.krender.engine.backend.test.impl.platform;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.kneelawk.krender.engine.impl.Platform;

public class TestPlatformImpl implements Platform {
    @Override
    public boolean isModLoaded(String modId) {
        return false;
    }

    @Override
    public Path getConfigDir() {
        return Paths.get("").toAbsolutePath().resolve("run/config");
    }
}
