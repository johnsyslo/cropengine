package com.john.cropengine.client.logic.state;

public class MovementState {
    public enum BotState {
        HARVESTING,
        SWITCH
    }

    public static BotState currentState = BotState.HARVESTING;
    public static boolean isAttacking = true;
    public static boolean strafeRight = true;

    // These moved from Controller to here
    public static int elapsedTicks = 0;
    public static int requiredTicks = 0;

    public static void reset(boolean startRight) {
        currentState = BotState.HARVESTING;
        strafeRight = startRight;
        isAttacking = true;
        elapsedTicks = 0;
        requiredTicks = 0;
    }
}