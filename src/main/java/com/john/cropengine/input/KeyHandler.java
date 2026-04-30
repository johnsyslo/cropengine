package com.john.cropengine.input;

import com.john.cropengine.CropEngine;
import com.john.cropengine.utils.ChatHudUtil;
import com.john.cropengine.logic.Controller;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class KeyHandler {
    public static void handleToggle(MinecraftClient client) {
        while (KeyRegistry.toggleKey.wasPressed()) {
            if (client.player == null) return;

            CropEngine.CONFIG.enabled = !CropEngine.CONFIG.enabled;
            sendToggleMessage(client);

            if (CropEngine.CONFIG.enabled) {
                Controller.reset();
            } else {
                com.john.cropengine.movement.KeyHandler.stopAll(client);
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
