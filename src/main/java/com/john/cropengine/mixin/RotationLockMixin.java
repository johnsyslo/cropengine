package com.john.cropengine.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Entity.class)
public abstract class RotationLockMixin {

    private static final Set<String> TOOL_KEYWORD = Set.of(
            "chopper", "hoe", "fungi", "dicer", "cactus", "cane"
    );

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void onUpdateRotation(double x, double y, CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity player) {
            if (isHoldingFarmingTool(player)) {
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean isHoldingFarmingTool(ClientPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return false;

        String itemName = stack.getName().getString().toLowerCase();
        return TOOL_KEYWORD.stream().anyMatch(itemName::contains);
    }
}