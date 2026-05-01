package com.john.cropengine.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.logic.state.MovementState;
import com.john.cropengine.logic.utils.RandomUtil;
import com.john.cropengine.logic.utils.SpeedUtil;
import com.john.cropengine.movement.KeyHandler;
import net.minecraft.client.MinecraftClient;

public class StateManager {
    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    public static boolean handleRestart(MovementState state) {
        state.incrementElapsedTicks();
        return state.getElapsedTicks() >= state.getRequiredTicks();
    }

    public static void handleFarming(MinecraftClient client, MovementState state) {
        KeyHandler.toggleDirection(client, state.getDirection());
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        if (horizontalSpeed < FARMING_IDLE_SPEED) {
            if (state.getElapsedTicks() == 0) {
                state.setRequiredTicks(RandomUtil.getTickDelay(CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay));
            }

            state.incrementElapsedTicks();

            if (state.getElapsedTicks() >= state.getRequiredTicks()) {
                state.setSwitchPos(client.player.getEntityPos());
                state.setCurrentState(MovementState.BotState.SWITCH);
                state.setElapsedTicks(0);
            }
            return;
        }

        state.setElapsedTicks(0);
    }

    public static void handleRowSwitch(MinecraftClient client, MovementState state) {
        KeyHandler.toggleForward(client, true);
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        if (horizontalSpeed < SWITCH_IDLE_SPEED) {
            if (state.getElapsedTicks() == 0) {
                state.setRequiredTicks(RandomUtil.getTickDelay(CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay));
            }

            state.incrementElapsedTicks();

            if (state.getElapsedTicks() >= state.getRequiredTicks()) {
                double distanceMoved = client.player.getEntityPos().distanceTo(state.getSwitchPos());

                if (distanceMoved < 1.0) {
                    stopAndWarp(client, state);
                    return;
                }

                state.toggleDirection();
                state.setCurrentState(MovementState.BotState.HARVEST);
                state.setElapsedTicks(0);
            }
            return;
        }

        state.setElapsedTicks(0);
    }

    private static void stopAndWarp(MinecraftClient client, MovementState state) {
        KeyHandler.stopAll(client);

        if (client.player != null && client.player.networkHandler != null) {
            client.player.networkHandler.sendChatCommand("warp garden");
        }

        state.setCurrentState(MovementState.BotState.RESTART);
        state.setElapsedTicks(0);
        state.setRequiredTicks(65);
    }
}
