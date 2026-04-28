package com.john.cropengine.client.movement;

import com.john.cropengine.CropEngine;
import net.minecraft.client.MinecraftClient;


public class MovementHandler {
    public static void stopAll(MinecraftClient client) {
        if (client.options == null) return;
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
    }

    public static void stopAllMovement(MinecraftClient client) {
        stopAll(client);
        toggleAttack(client, false);
    }



    public static void toggleAttack(MinecraftClient client, boolean mode) {
        client.options.attackKey.setPressed(mode);
    }

    public static void toggleForward(MinecraftClient client, boolean dir) {
        stopAll(client);
        client.options.forwardKey.setPressed(dir);
    }

    public static void toggleDirection(MinecraftClient client, boolean dir) {
        stopAll(client);
        client.options.rightKey.setPressed(dir);
        client.options.leftKey.setPressed(!dir);
    }
}