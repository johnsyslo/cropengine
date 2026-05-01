package com.john.cropengine.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.config.ModConfig;
import com.john.cropengine.farm.Behavior;
import com.john.cropengine.farm.SShape;
import com.john.cropengine.farm.Vertical;
import com.john.cropengine.logic.state.MovementState;
import com.john.cropengine.movement.KeyHandler;
import net.minecraft.client.MinecraftClient;

public class StateManager {
    private static final Behavior S_SHAPE = new SShape();
    private static final Behavior VERTICAL = new Vertical();

    public static boolean handleRestart(MovementState state) {
        state.incrementElapsedTicks();
        return state.getElapsedTicks() >= state.getRequiredTicks();
    }

    public static void handleFarming(MinecraftClient client, MovementState state) {
        resolveBehavior().handleFarming(client, state);
    }

    public static void handleRowSwitch(MinecraftClient client, MovementState state) {
        resolveBehavior().handleRowSwitch(client, state);
    }

    public static void stopAndWarp(MinecraftClient client, MovementState state) {
        KeyHandler.stopAll(client);

        if (client.player != null && client.player.networkHandler != null) {
            client.player.networkHandler.sendChatCommand("warp garden");
        }

        state.setCurrentState(MovementState.BotState.RESTART);
        state.setElapsedTicks(0);
        state.setRequiredTicks(65);
    }

    private static Behavior resolveBehavior() {
        ModConfig.FarmType farmType = CropEngine.CONFIG.getEffectiveFarmType();
        if (CropEngine.CONFIG.cropType == ModConfig.CropType.Carrot && farmType == ModConfig.FarmType.Vertical) {
            return VERTICAL;
        }
        return S_SHAPE;
    }
}
