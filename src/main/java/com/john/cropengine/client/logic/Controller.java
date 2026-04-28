package com.john.cropengine.client.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.movement.MovementHandler;
import com.john.cropengine.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ThreadLocalRandom;

public class Controller {

    private enum State {
        FARMING,
        ROW_SWITCH
    }

    private static State currentState = State.FARMING;

    private static boolean strafeRight;

    private static int tickCounter;
    private static int requiredTicks;

    private static final double FARMING_IDLE_SPEED = 0.01;
    private static final double SWITCH_IDLE_SPEED = 0.02;

    public static void reset() {
        strafeRight = CropEngine.CONFIG.startSide == ModConfig.StartSide.Right;
        currentState = State.FARMING;
        tickCounter = 0;
        requiredTicks = 0;
    }

    public static void tick(MinecraftClient client) {
        if (!CropEngine.CONFIG.enabled) return;
        if (client.player == null) return;

        MovementHandler.holdAttack(client);

        double speed = getHorizontalSpeed(client);

        switch (currentState) {
            case FARMING -> handleFarming(client, speed);
            case ROW_SWITCH -> handleRowSwitch(client, speed);
        }
    }

    private static void handleFarming(MinecraftClient client, double speed) {

        // Always fully define movement state (no leftovers)
        MovementHandler.strafe(client, strafeRight);
        MovementHandler.stopForward(client);

        if (speed < FARMING_IDLE_SPEED) {

            if (tickCounter == 0) {
                requiredTicks = getRandomDelay();
            }

            tickCounter++;

            if (tickCounter >= requiredTicks) {
                currentState = State.ROW_SWITCH;
                tickCounter = 0;
                MovementHandler.stopStrafe(client);
            }

        } else {
            tickCounter = 0;
        }
    }

    private static void handleRowSwitch(MinecraftClient client, double speed) {

        // Fully override movement every tick
        MovementHandler.moveForward(client);
        MovementHandler.stopStrafe(client);

        if (speed < SWITCH_IDLE_SPEED) {

            if (tickCounter == 0) {
                requiredTicks = getRandomDelay();
            }

            tickCounter++;

            if (tickCounter >= requiredTicks) {
                strafeRight = !strafeRight;
                currentState = State.FARMING;
                tickCounter = 0;

                MovementHandler.stopForward(client);
            }

        } else {
            tickCounter = 0;
        }
    }

    private static double getHorizontalSpeed(MinecraftClient client) {
        Vec3d v = client.player.getVelocity();
        return Math.sqrt(v.x * v.x + v.z * v.z);
    }

    private static int getRandomDelay() {
        int min = CropEngine.CONFIG.minDelay;
        int max = CropEngine.CONFIG.maxDelay;

        if (min < 0) min = 0;
        if (max < min) max = min;

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}