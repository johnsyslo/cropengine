package com.john.cropengine.logic;

import com.john.cropengine.CropEngine;
import com.john.cropengine.config.ModConfig;
import com.john.cropengine.logic.state.MovementState;
import com.john.cropengine.movement.KeyHandler;
import net.minecraft.client.MinecraftClient;

public class Controller {
    private static final MovementState STATE = new MovementState();

    public static void reset() {
        boolean startRight = CropEngine.CONFIG.startSide == ModConfig.StartSide.Right;
        STATE.reset(startRight);
    }

    public static void updateTick(MinecraftClient client) {
        if (!CropEngine.CONFIG.enabled || client.player == null) return;

        if (STATE.getCurrentState() == MovementState.BotState.RESTART) {
            KeyHandler.stopMovement(client);
            if (StateManager.handleRestart(STATE)) {
                reset();
            }
            return;
        }

        KeyHandler.toggleAttack(client, STATE.getCurrentState() == MovementState.BotState.HARVEST);

        switch (STATE.getCurrentState()) {
            case HARVEST -> StateManager.handleFarming(client, STATE);
            case SWITCH -> StateManager.handleRowSwitch(client, STATE);
        }
    }
}
