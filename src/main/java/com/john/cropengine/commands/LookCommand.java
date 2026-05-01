package com.john.cropengine.commands;

import com.john.cropengine.utils.RotationManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class LookCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("look")
                        .then(argument("yaw", FloatArgumentType.floatArg())
                                .then(argument("pitch", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            float yaw = FloatArgumentType.getFloat(ctx, "yaw");
                                            float pitch = FloatArgumentType.getFloat(ctx, "pitch");

                                            RotationManager.setTarget(yaw, pitch);

                                            return 1;
                                        })))
        );
    }
}