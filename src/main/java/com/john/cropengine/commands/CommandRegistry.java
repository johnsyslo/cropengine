package com.john.cropengine.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommandRegistry {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            YeetCommand.register(dispatcher);
            CropEngineCommand.register(dispatcher);
            LookCommand.register(dispatcher);
        });
    }
}