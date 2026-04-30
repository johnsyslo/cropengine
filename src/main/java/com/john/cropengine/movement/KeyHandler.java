package com.john.cropengine.movement;

import net.minecraft.client.MinecraftClient;


public class KeyHandler {
    public static void stopMovement(MinecraftClient client) {
        if (client.options == null) return;
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
    }

    public static void stopAll(MinecraftClient client) {
        stopMovement(client);
        toggleAttack(client, false);
    }



    public static void toggleAttack(MinecraftClient client, boolean mode) {
        client.options.attackKey.setPressed(mode);
    }

    public static void toggleForward(MinecraftClient client, boolean mode) {
        stopMovement(client);
        client.options.forwardKey.setPressed(mode);
    }

    public static void toggleDirection(MinecraftClient client, boolean direction) {
        stopMovement(client);
        client.options.rightKey.setPressed(direction);
        client.options.leftKey.setPressed(!direction);
    }
}