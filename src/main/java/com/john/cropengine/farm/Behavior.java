package com.john.cropengine.farm;

import com.john.cropengine.logic.state.MovementState;
import net.minecraft.client.MinecraftClient;

public interface Behavior {
    void handleFarming(MinecraftClient client, MovementState state);
    void handleRowSwitch(MinecraftClient client, MovementState state);
}

