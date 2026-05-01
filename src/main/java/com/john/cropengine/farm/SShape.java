package com.john.cropengine.farm;

import com.john.cropengine.CropEngine;
import com.john.cropengine.logic.StateManager;
import com.john.cropengine.logic.state.MovementState;
import com.john.cropengine.logic.utils.RandomUtil;
import com.john.cropengine.logic.utils.SpeedUtil;
import com.john.cropengine.movement.KeyHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class SShape implements Behavior {
    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    @Override
    public void handleFarming(MinecraftClient client, MovementState state) {
        KeyHandler.toggleDirection(client, state.getDirection());
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        if (horizontalSpeed < FARMING_IDLE_SPEED) {
            if (state.getElapsedTicks() == 0) {
                state.setRequiredTicks(RandomUtil.getTickDelay(CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay));
            }

            state.incrementElapsedTicks();

            if (state.getElapsedTicks() >= state.getRequiredTicks()) {
                state.setSwitchPos(new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ()));
                state.setCurrentState(MovementState.BotState.SWITCH);
                state.setElapsedTicks(0);
            }
            return;
        }

        state.setElapsedTicks(0);
    }

    @Override
    public void handleRowSwitch(MinecraftClient client, MovementState state) {
        KeyHandler.toggleForward(client, true);
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        if (horizontalSpeed < SWITCH_IDLE_SPEED) {
            if (state.getElapsedTicks() == 0) {
                state.setRequiredTicks(RandomUtil.getTickDelay(CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay));
            }

            state.incrementElapsedTicks();

            if (state.getElapsedTicks() >= state.getRequiredTicks()) {
                double distanceMoved = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ())
                        .distanceTo(state.getSwitchPos());

                if (distanceMoved < 1.0) {
                    StateManager.stopAndWarp(client, state);
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
}
