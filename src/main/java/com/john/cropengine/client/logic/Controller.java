package com.john.cropengine.client.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.logic.state.MovementState;
import com.john.cropengine.client.movement.MovementHandler;
import com.john.cropengine.client.utils.SpeedUtil;
import com.john.cropengine.client.utils.RandomUtil;
import com.john.cropengine.config.ModConfig;
import net.minecraft.client.MinecraftClient;

public class Controller {

    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    public static void reset() {
        boolean startRight = CropEngine.CONFIG.startSide == ModConfig.StartSide.Right;
        MovementState.reset(startRight);
    }

    public static void updateTick(MinecraftClient client) {
        if (!CropEngine.CONFIG.enabled || client.player == null) return;


        MovementHandler.toggleAttack(client, MovementState.isAttacking);
        double horizontalSpeed = SpeedUtil.getHorizontalSpeed(client);

        switch (MovementState.currentState) {
            case HARVESTING -> handleFarming(client, horizontalSpeed);
            case SWITCH -> rowSwitch(client, horizontalSpeed);
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
                MovementState.strafeRight = !MovementState.strafeRight;
                MovementState.currentState = MovementState.BotState.HARVESTING;
                MovementState.elapsedTicks = 0;
            }
        } else {
            MovementState.elapsedTicks = 0;
        }
    }
}