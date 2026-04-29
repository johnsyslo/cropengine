package com.john.cropengine.mixin;

import com.john.cropengine.utils.ChatHudUtil;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin implements ChatHudUtil {

    @Shadow @Final private List<ChatHudLine> messages;
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow public abstract void reset();

    @Override
    @Unique
    public void cropengine$removeToggleMessages() {
        String identifier = "[CropEngine]";

        this.messages.removeIf(line -> line.content().getString().contains(identifier));
        this.visibleMessages.clear();
        this.reset();
    }
}