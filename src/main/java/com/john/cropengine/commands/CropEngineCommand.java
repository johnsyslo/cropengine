package com.john.cropengine.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.john.cropengine.config.ConfigScreenProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CropEngineCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("cropengine")
                        .executes(ctx -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            client.send(() ->
                                    client.setScreen(
                                            ConfigScreenProvider.create(client.currentScreen)
                                    )
                            );
                            return 1;
                        })
        );
    }
}
