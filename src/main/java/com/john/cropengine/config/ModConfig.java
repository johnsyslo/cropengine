package com.john.cropengine.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Config(name = "cropengine")
public class ModConfig implements ConfigData {

    public boolean enabled = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public CropType cropType = CropType.Pumpkin;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public FarmType farmType = FarmType.SShape;

    public enum CropType {
        Pumpkin, Carrot
    }

    public enum FarmType {
        SShape,
        Vertical
    }

    public List<FarmType> getAvailableFarmTypes() {
        return switch (cropType) {
            case Pumpkin -> List.of(FarmType.SShape);
            case Carrot -> List.of(FarmType.SShape, FarmType.Vertical);
        };
    }

    public FarmType getEffectiveFarmType() {
        List<FarmType> availablePatterns = getAvailableFarmTypes();
        if (!availablePatterns.contains(farmType)) {
            farmType = availablePatterns.getFirst();
        }
        return farmType;
    }

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 25)
    public int minDelay = 10;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 25, max = 50)
    public int maxDelay = 30;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public StartSide startSide = StartSide.Right;

    public enum StartSide {
        Left, Right
    }

}
