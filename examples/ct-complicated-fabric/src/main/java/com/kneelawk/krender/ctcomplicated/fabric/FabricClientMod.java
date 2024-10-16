package com.kneelawk.krender.ctcomplicated.fabric;

import net.fabricmc.api.ClientModInitializer;

import com.kneelawk.krender.ctcomplicated.client.CTComplicatedClient;

public class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CTComplicatedClient.init();
        CTComplicatedClient.syncInit();
    }
}
