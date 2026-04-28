package com.john.cropengine.client.movement;

import net.minecraft.client.MinecraftClient;

public class MovementHandler {

    public static void holdAttack(MinecraftClient client) {
        client.options.attackKey.setPressed(true);
    }

    public static void strafe(MinecraftClient client, boolean direction) {
        client.options.rightKey.setPressed(direction);
        client.options.leftKey.setPressed(!direction);
        client.options.forwardKey.setPressed(false);
    }

    public static void stopStrafe(MinecraftClient client) {
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
    }

    public static void moveForward(MinecraftClient client) {
        client.options.forwardKey.setPressed(true);
        stopStrafe(client);
    }

    public static void stopForward(MinecraftClient client) {
        client.options.forwardKey.setPressed(false);
    }

    public static void stopAll(MinecraftClient client) {
        client.options.attackKey.setPressed(false);
        stopStrafe(client);
        stopForward(client);
    }
}