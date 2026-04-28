package com.john.cropengine.client.input;

import com.john.cropengine.CropEngine;
import com.john.cropengine.client.logic.Controller;
import com.john.cropengine.client.movement.MovementHandler;
import com.john.cropengine.utils.ChatHudUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    public static final Identifier CROP_ENGINE_ID = Identifier.of("cropengine", "main");
    public static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(CROP_ENGINE_ID);
    private static KeyBinding toggleKey;

    public static void init() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cropengine.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                CATEGORY
        ));
    }

    public static void handleToggle(MinecraftClient client) {
        while (toggleKey.wasPressed()) {
            if (client.player == null) return;

            CropEngine.CONFIG.enabled = !CropEngine.CONFIG.enabled;
            sendToggleMessage(client);

            if (CropEngine.CONFIG.enabled) {
                Controller.reset();
            } else {
                MovementHandler.stopAllMovement(client);
            }
        }
    }

    private static void sendToggleMessage(MinecraftClient client) {
        if (client.player == null || client.inGameHud == null) return;

        ((ChatHudUtil) client.inGameHud.getChatHud()).cropengine$removeToggleMessages();

        Formatting color = CropEngine.CONFIG.enabled ? Formatting.GREEN : Formatting.RED;
        String status = CropEngine.CONFIG.enabled ? "[Enabled]" : "[Disabled]";

        client.player.sendMessage(
                Text.literal("[CropEngine] ").formatted(Formatting.DARK_AQUA)
                        .append(Text.literal("Status: ").formatted(Formatting.GRAY))
                        .append(Text.literal(status).formatted(color)),
                false
        );
    }
}