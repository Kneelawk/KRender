package com.kneelawk.krender.engine.impl.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

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

    public boolean keepCurrentBackend;
    public String defaultBackend;
    public String[] __presentBackends;

    public static String getDefaultBackend(String bestBackend, Set<String> presentBackends) {
        load(bestBackend, presentBackends);
        return instance.defaultBackend;
    }

    public static void load(String bestBackend, Set<String> presentBackends) {
        BackendConfig config = loadImpl();
        initImpl(config, bestBackend, presentBackends);
        saveImpl(config);
        instance = config;
    }

    public static void save() {
        saveImpl(instance);
    }

    private static BackendConfig loadImpl() {
        try {
            return gson.fromJson(Files.newBufferedReader(CONFIG_FILE), BackendConfig.class);
        } catch (Exception e) {
            KRELog.LOG.warn("Error loading KRender Backend config", e);
            return new BackendConfig();
        }
    }

    private static void initImpl(BackendConfig initTo, String bestBackend, Set<String> presentBackends) {
        if (!initTo.keepCurrentBackend || initTo.defaultBackend == null) {
            initTo.defaultBackend = bestBackend;
        } else if (presentBackends.contains(initTo.defaultBackend)) {
            initTo.defaultBackend = bestBackend;
        }
        initTo.__presentBackends = presentBackends.toArray(String[]::new);
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
