package com.john.cropengine.client.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.movement.MovementHandler;
import com.john.cropengine.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ThreadLocalRandom;

public class Controller {

    private enum State {
        HARVESTING,
        ROW_SWITCH
    }

    private static State currentState = State.HARVESTING;

    private static boolean strafeRight;

    private static int elapsedTicks;
    private static int requiredTicks;

    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    public static void reset() {
        strafeRight = CropEngine.CONFIG.startSide == ModConfig.StartSide.Right;
        currentState = State.HARVESTING;
        elapsedTicks = 0;
        requiredTicks = 0;
    }

    public static void updateTick(MinecraftClient client) {
        if (!CropEngine.CONFIG.enabled) return;
        if (client.player == null) return;

        MovementHandler.holdAttack(client);

        double horizontalSpeed = getHorizontalSpeed(client);

        switch (currentState) {
            case HARVESTING -> handleFarming(client, horizontalSpeed);
            case ROW_SWITCH -> handleRowSwitch(client, horizontalSpeed);
        }
    }

    private static void handleFarming(MinecraftClient client, double horizontalSpeed) {

        // Always fully define movement state (no leftovers)
        MovementHandler.strafe(client, strafeRight);
        MovementHandler.stopForward(client);

        if (horizontalSpeed < FARMING_IDLE_SPEED) {

            if (elapsedTicks == 0) {
                requiredTicks = getTickDelay();
            }

            elapsedTicks++;

            if (elapsedTicks >= requiredTicks) {
                currentState = State.ROW_SWITCH;
                elapsedTicks = 0;
                MovementHandler.stopStrafe(client);
            }

        } else {
            elapsedTicks = 0;
        }
    }

    private static void handleRowSwitch(MinecraftClient client, double horizontalSpeed) {

        // Fully override movement every tick
        MovementHandler.moveForward(client);
        MovementHandler.stopStrafe(client);

        if (horizontalSpeed < SWITCH_IDLE_SPEED) {

            if (elapsedTicks == 0) {
                requiredTicks = getTickDelay();
            }

            elapsedTicks++;

            if (elapsedTicks >= requiredTicks) {
                strafeRight = !strafeRight;
                currentState = State.HARVESTING;
                elapsedTicks = 0;

                MovementHandler.stopForward(client);
            }

        } else {
            elapsedTicks = 0;
        }
    }

    private static double getHorizontalSpeed(MinecraftClient client) {
        Vec3d velocityVec = client.player.getVelocity();
        return Math.sqrt(velocityVec.x * velocityVec.x + velocityVec.z * velocityVec.z);
    }

    private static int getTickDelay() {
        int min = CropEngine.CONFIG.minDelay;
        int max = CropEngine.CONFIG.maxDelay;

        if (min < 0) min = 0;
        if (max < min) max = min;

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}