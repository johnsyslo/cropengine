package com.john.cropengine.client.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.logic.state.MovementState;
import com.john.cropengine.client.movement.MovementHandler;
import com.john.cropengine.client.utils.SpeedUtil;
import com.john.cropengine.client.utils.RandomUtil;
import com.john.cropengine.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Controller {

    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    public static void reset() {
        boolean startRight = CropEngine.CONFIG.startSide == ModConfig.StartSide.Right;
        MovementState.reset(startRight);
    }

    public static void updateTick(MinecraftClient client) {
        if (!CropEngine.CONFIG.enabled || client.player == null) return;

        if (MovementState.currentState == MovementState.BotState.RESTART) {
            MovementHandler.stopAll(client);
            handleRestart(client);
            return;
        }

        MovementHandler.toggleAttack(client, MovementState.currentState == MovementState.BotState.HARVEST);
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        switch (MovementState.currentState) {
            case HARVEST -> handleFarming(client, horizontalSpeed);
            case SWITCH -> rowSwitch(client, horizontalSpeed);
        }
    }

    private static void handleRestart(MinecraftClient client) {
        MovementState.elapsedTicks++;

        if (MovementState.elapsedTicks >= MovementState.requiredTicks) {
            reset();
            MovementState.currentState = MovementState.BotState.HARVEST;
        }
    }

    private static void handleFarming(MinecraftClient client, double horizontalSpeed) {
        MovementHandler.toggleDirection(client, MovementState.strafeRight);

        if (horizontalSpeed < FARMING_IDLE_SPEED) {
            if (MovementState.elapsedTicks == 0) {
                MovementState.requiredTicks = RandomUtil.getTickDelay(
                        CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay);
            }

            MovementState.elapsedTicks++;

            if (MovementState.elapsedTicks >= MovementState.requiredTicks) {
                MovementState.switchPos = client.player.getEntityPos();
                MovementState.currentState = MovementState.BotState.SWITCH;
                MovementState.elapsedTicks = 0;
            }
        } else {
            MovementState.elapsedTicks = 0;
        }
    }

    private static void rowSwitch(MinecraftClient client, double horizontalSpeed) {
        MovementHandler.toggleForward(client, true);

        if (horizontalSpeed < SWITCH_IDLE_SPEED) {
            if (MovementState.elapsedTicks == 0) {
                MovementState.requiredTicks = RandomUtil.getTickDelay(
                        CropEngine.CONFIG.minDelay, CropEngine.CONFIG.maxDelay);
            }

            MovementState.elapsedTicks++;

            if (MovementState.elapsedTicks >= MovementState.requiredTicks) {
                double distanceMoved = client.player.getEntityPos().distanceTo(MovementState.switchPos);

                if (distanceMoved < 1.0) {
                    stopAndWarp(client);
                    return;
                }

                MovementState.strafeRight = !MovementState.strafeRight;
                MovementState.currentState = MovementState.BotState.HARVEST;
                MovementState.elapsedTicks = 0;
            }
        } else {
            MovementState.elapsedTicks = 0;
        }
    }

    private static void stopAndWarp(MinecraftClient client) {
        MovementHandler.stopAll(client);

        if (client.player.networkHandler != null) {
            client.player.networkHandler.sendChatCommand("warp garden");
        }

        MovementState.currentState = MovementState.BotState.RESTART;
        MovementState.elapsedTicks = 0;
        MovementState.requiredTicks = 65;
    }
}