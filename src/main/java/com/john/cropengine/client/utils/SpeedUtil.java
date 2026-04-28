package com.john.cropengine.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class SpeedUtil {
    public static double getHorizontalSpeed(MinecraftClient client) {
        if (client.player == null) return 0;
        Vec3d velocityVec = client.player.getVelocity();
        return Math.sqrt(velocityVec.x * velocityVec.x + velocityVec.z * velocityVec.z);
    }
}
