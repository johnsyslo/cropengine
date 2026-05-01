package com.john.cropengine.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class RotationManager {
    private static boolean active = false;
    private static float targetYaw;
    private static float targetPitch;
    private static final float SMOOTHNESS = 0.2f;

    /**
     * Sets the target angles for the smooth rotation to begin.
     */
    public static void setTarget(float yaw, float pitch) {
        targetYaw = yaw;
        targetPitch = pitch;
        active = true;
    }

    public static void update(ClientPlayerEntity player) {
        if (!active || player == null) {
            return;
        }

        float currentYaw = player.getYaw();
        float currentPitch = player.getPitch();

        // Calculate the shortest path for the rotation
        float yawDiff = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float pitchDiff = targetPitch - currentPitch;

        // If we are close enough, snap to target and stop updating
        if (Math.abs(yawDiff) < 0.1f && Math.abs(pitchDiff) < 0.1f) {
            player.setYaw(targetYaw);
            player.setPitch(targetPitch);
            active = false;
            return;
        }

        // Apply smooth movement
        player.setYaw(currentYaw + (yawDiff * SMOOTHNESS));
        player.setPitch(currentPitch + (pitchDiff * SMOOTHNESS));
    }

    public static boolean isActive() {
        return active;
    }
}