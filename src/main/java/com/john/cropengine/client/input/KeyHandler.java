package com.john.cropengine.client.input;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.logic.Controller;
import com.john.cropengine.client.movement.MovementHandler;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    private static KeyBinding toggleKey;

    public static void init() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cropengine.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));
    }

    public static void handleToggle(MinecraftClient client) {
        while (toggleKey.wasPressed()) {

            CropEngine.CONFIG.enabled = !CropEngine.CONFIG.enabled;

            String status = CropEngine.CONFIG.enabled ? "§aEnabled" : "§cDisabled";

            client.player.sendMessage(
                    Text.literal("§7CropEngine is now " + status),
                    true
            );

            if (CropEngine.CONFIG.enabled) {
                Controller.reset();
            } else {
                MovementHandler.stopAll(client);
            }
        }
    }
}