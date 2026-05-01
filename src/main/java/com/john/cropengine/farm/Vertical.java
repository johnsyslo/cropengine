package com.john.cropengine.farm;

import com.john.cropengine.CropEngine;
import com.john.cropengine.logic.StateManager;
import com.john.cropengine.logic.state.MovementState;
import com.john.cropengine.logic.utils.RandomUtil;
import com.john.cropengine.logic.utils.SpeedUtil;
import com.john.cropengine.movement.KeyHandler;
import net.minecraft.client.MinecraftClient;

public class Vertical implements Behavior {
    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;
    private static final double DROP_THRESHOLD = 1;

    @Override
    public void handleFarming(MinecraftClient client, MovementState state) {
        KeyHandler.toggleDirection(client, state.getDirection());
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        if (horizontalSpeed > FARMING_IDLE_SPEED) {
            state.setLastStableY(client.player.getY());
            CropEngine.LOGGER.info(String.valueOf(client.player.getY()));
            state.setElapsedTicks(0);
            return;
        }

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

    @Override
    public void handleRowSwitch(MinecraftClient client, MovementState state) {
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);
        double droppedDistance = state.getLastStableY() - client.player.getY();
        System.out.println(droppedDistance);

        if (droppedDistance >= DROP_THRESHOLD) {
            state.toggleDirection();
            state.setCurrentState(MovementState.BotState.HARVEST);
            state.setElapsedTicks(0);
            return;
        }

        if (horizontalSpeed < SWITCH_IDLE_SPEED) {
            if (state.getElapsedTicks() == 0) {
                state.setRequiredTicks(RandomUtil.getTickDelay(CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay));
            }

            state.incrementElapsedTicks();

            if (state.getElapsedTicks() >= state.getRequiredTicks()) {
                StateManager.stopAndWarp(client, state);
            }
            return;
        }

        state.setElapsedTicks(0);
    }
}
