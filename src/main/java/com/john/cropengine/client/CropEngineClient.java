package com.john.cropengine.client;
import com.john.cropengine.commands.CommandRegistry;
import com.john.cropengine.input.KeyRegistry;
import com.john.cropengine.input.KeyHandler;
import com.john.cropengine.logic.Controller;
import com.john.cropengine.CropEngine;
import com.john.cropengine.utils.RotationManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class CropEngineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        KeyRegistry.init();
        CommandRegistry.register();
        CropEngine.LOGGER.info("[CropEngine] Keybinds and commands registered.");


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            KeyHandler.handleToggle(client);
            RotationManager.update(client.player);

            if (CropEngine.CONFIG.enabled) {
                Controller.updateTick(client);
            }
        });
    }
}
