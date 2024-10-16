package com.kneelawk.krender.ctcomplicated.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.kneelawk.krender.ctcomplicated.CTComplicated;
import com.kneelawk.krender.ctcomplicated.CTConstants;

@Mod(CTConstants.MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        CTComplicated.init();

        modBus.addListener(this::onInit);
        modBus.addListener(this::onRegister);
    }

    private void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(CTComplicated::initSync);
    }

    private void onRegister(RegisterEvent event) {
        CTComplicated.REGISTRARS.register(event.getRegistry());
    }
}
