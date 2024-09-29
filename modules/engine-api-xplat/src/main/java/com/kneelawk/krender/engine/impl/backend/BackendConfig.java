package com.kneelawk.krender.engine.impl.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

import com.kneelawk.krender.engine.impl.KREConstants;
import com.kneelawk.krender.engine.impl.KRELog;
import com.kneelawk.krender.engine.impl.Platform;

public class BackendConfig {
    private static final Gson gson = new Gson();
    private static final String CONFIG_NAME = "backends.json";
    private static final Path CONFIG_DIR = Platform.INSTANCE.getConfigDir().resolve(KREConstants.CONFIG_DIR);
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve(CONFIG_NAME);

    private static BackendConfig instance;

    public Map<String, Integer> backendPriorities;

    public static Map<String, Integer> getDefaultBackend(Map<String, Integer> priorities) {
        load(priorities);
        return instance.backendPriorities;
    }

    public static void load(Map<String, Integer> priorities) {
        BackendConfig config = loadImpl();
        initImpl(config, priorities);
        saveImpl(config);
        instance = config;
    }

    public static void save() {
        saveImpl(instance);
    }

    private static BackendConfig loadImpl() {
        if (Files.exists(CONFIG_FILE)) {
            try {
                return gson.fromJson(Files.newBufferedReader(CONFIG_FILE), BackendConfig.class);
            } catch (Exception e) {
                KRELog.LOG.warn("Error loading KRender Backend config", e);
                return new BackendConfig();
            }
        } else {
            return new BackendConfig();
        }
    }

    private static void initImpl(BackendConfig initTo, Map<String, Integer> priorities) {
        if (initTo.backendPriorities == null || initTo.backendPriorities.isEmpty()) {
            initTo.backendPriorities = new LinkedHashMap<>(priorities);
        } else {
            for (var entry : priorities.entrySet()) {
                initTo.backendPriorities.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void saveImpl(BackendConfig toSave) {
        try {
            if (!Files.exists(CONFIG_DIR)) Files.createDirectories(CONFIG_DIR);
            gson.toJson(toSave, BackendConfig.class, Files.newBufferedWriter(CONFIG_FILE));
        } catch (Exception e) {
            KRELog.LOG.warn("Error waving KRender Backend config", e);
        }
    }
}
