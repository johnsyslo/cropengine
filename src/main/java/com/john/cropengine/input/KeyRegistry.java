package com.john.cropengine.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyRegistry {
    public static KeyBinding toggleKey;
    public static KeyBinding configKey;

    public static final Identifier CROP_ENGINE_ID = Identifier.of("cropengine", "main");
    public static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(CROP_ENGINE_ID);

    public static void init() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cropengine.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                CATEGORY
        ));

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cropengine.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                CATEGORY
        ));
    }
}
