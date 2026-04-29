package com.john.cropengine.client.logic.state;

import net.minecraft.util.math.Vec3d;

public class MovementState {
    public enum BotState {
        HARVEST,
        SWITCH,
        RESTART
    }

    public static BotState currentState = BotState.HARVEST;
    public static boolean isAttacking = true;
    public static boolean strafeRight = true;
    public static Vec3d switchPos = Vec3d.ZERO;

    // These moved from Controller to here
    public static int elapsedTicks = 0;
    public static int requiredTicks = 0;

    public static void reset(boolean startRight) {
        currentState = BotState.HARVEST;
        strafeRight = startRight;
        isAttacking = true;
        elapsedTicks = 0;
        requiredTicks = 0;
    }
}