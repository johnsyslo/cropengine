package com.john.cropengine.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "cropengine")
public class ModConfig implements ConfigData {

    public boolean enabled = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 50)
    public int minDelay = 20;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public StartSide startSide = StartSide.Right;

    public enum StartSide {
        Left, Right
    }

}
