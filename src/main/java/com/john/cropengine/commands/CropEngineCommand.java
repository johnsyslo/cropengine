package com.john.cropengine.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.autoconfig.AutoConfig;
import com.john.cropengine.config.ModConfig;
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
                                            AutoConfig.getConfigScreen(ModConfig.class, client.currentScreen).get()
                                    )
                            );
                            return 1;
                        })
        );
    }
}