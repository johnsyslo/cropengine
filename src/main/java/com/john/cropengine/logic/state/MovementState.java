package com.john.cropengine.logic.state;

import net.minecraft.util.math.Vec3d;

public class MovementState {
    public enum BotState {
        HARVEST,
        SWITCH,
        RESTART
    }

    private BotState currentState = BotState.HARVEST;
    private boolean movingDirection = true;
    private Vec3d switchPos = Vec3d.ZERO;

    private double lastStableY = 0.0;
    private int elapsedTicks = 0;
    private int requiredTicks = 0;

    public BotState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BotState currentState) {
        this.currentState = currentState;
    }

    public boolean getDirection() {
        return movingDirection;
    }

    public void toggleDirection() {
        movingDirection = !movingDirection;
    }

    public Vec3d getSwitchPos() {
        return switchPos;
    }

    public void setSwitchPos(Vec3d switchPos) {
        this.switchPos = switchPos;
    }

    public int getElapsedTicks() {
        return elapsedTicks;
    }

    public void setElapsedTicks(int elapsedTicks) {
        this.elapsedTicks = elapsedTicks;
    }

    public void incrementElapsedTicks() { elapsedTicks++; }

    public int getRequiredTicks() {
        return requiredTicks;
    }

    public void setRequiredTicks(int requiredTicks) {
        this.requiredTicks = requiredTicks;
    }

    public void reset(boolean startRight) {
        currentState = BotState.HARVEST;
        movingDirection = startRight;
        switchPos = Vec3d.ZERO;
        lastStableY = 0.0;
        elapsedTicks = 0;
        requiredTicks = 0;
    }

    public double getLastStableY() {
        return lastStableY;
    }

    public void setLastStableY(double lastStableY) {
        this.lastStableY = lastStableY;
    }
}
