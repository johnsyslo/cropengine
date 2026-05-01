package com.john.cropengine.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public final class ConfigScreenProvider {
    private static final ModConfig.FarmType[] PUMPKIN_PATTERNS = {ModConfig.FarmType.SShape};
    private static final ModConfig.FarmType[] CARROT_PATTERNS = {ModConfig.FarmType.SShape, ModConfig.FarmType.Vertical};

    private ConfigScreenProvider() {
    }

    public static Screen create(Screen parent) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        AtomicReference<Boolean> enabled = new AtomicReference<>(config.enabled);
        AtomicReference<ModConfig.CropType> cropType = new AtomicReference<>(config.cropType);
        AtomicReference<ModConfig.FarmType> pumpkinPattern = new AtomicReference<>(ModConfig.FarmType.SShape);
        AtomicReference<ModConfig.FarmType> carrotPattern = new AtomicReference<>(normalizeCarrotPattern(config.farmType));
        AtomicReference<Integer> minDelay = new AtomicReference<>(config.minDelay);
        AtomicReference<Integer> maxDelay = new AtomicReference<>(config.maxDelay);
        AtomicReference<ModConfig.StartSide> startSide = new AtomicReference<>(config.startSide);
        AtomicReference<Boolean> lockRotation = new AtomicReference<>(config.lockRotation);

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("text.autoconfig.cropengine.title"));
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("text.cropengine.config.category.general"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        var enabledEntry = entries.startBooleanToggle(Text.translatable("text.autoconfig.cropengine.option.enabled"), enabled.get())
                .setDefaultValue(false)
                .setSaveConsumer(enabled::set)
                .build();

        var cropTypeEntry = entries.startEnumSelector(Text.literal("Farm Type"), ModConfig.CropType.class, cropType.get())
                .setDefaultValue(ModConfig.CropType.Pumpkin)
                .setSaveConsumer(cropType::set)
                .build();

        var pumpkinPatternEntry = entries.startSelector(Text.literal("Farm Pattern"), PUMPKIN_PATTERNS, pumpkinPattern.get())
                .setDefaultValue(ModConfig.FarmType.SShape)
                .setNameProvider(ConfigScreenProvider::toPatternText)
                .setSaveConsumer(pumpkinPattern::set)
                .setDisplayRequirement(Requirement.isValue(cropTypeEntry, ModConfig.CropType.Pumpkin))
                .build();

        var carrotPatternEntry = entries.startSelector(Text.literal("Farm Pattern"), CARROT_PATTERNS, carrotPattern.get())
                .setDefaultValue(ModConfig.FarmType.SShape)
                .setNameProvider(ConfigScreenProvider::toPatternText)
                .setSaveConsumer(carrotPattern::set)
                .setDisplayRequirement(Requirement.isValue(cropTypeEntry, ModConfig.CropType.Carrot))
                .build();

        var minDelayEntry = entries.startIntField(Text.translatable("text.autoconfig.cropengine.option.minDelay"), minDelay.get())
                .setTooltip(Text.translatable("text.autoconfig.cropengine.option.minDelay.@Tooltip"))
                .setDefaultValue(10)
                .setMin(1)
                .setMax(25)
                .setSaveConsumer(minDelay::set)
                .build();

        var maxDelayEntry = entries.startIntField(Text.translatable("text.autoconfig.cropengine.option.maxDelay"), maxDelay.get())
                .setTooltip(Text.translatable("text.autoconfig.cropengine.option.maxDelay.@Tooltip"))
                .setDefaultValue(30)
                .setMin(25)
                .setMax(50)
                .setSaveConsumer(maxDelay::set)
                .build();

        var startSideEntry = entries.startEnumSelector(Text.translatable("text.autoconfig.cropengine.option.startSide"), ModConfig.StartSide.class, startSide.get())
                .setTooltip(Text.translatable("text.autoconfig.cropengine.option.startSide.@Tooltip"))
                .setDefaultValue(ModConfig.StartSide.Right)
                .setSaveConsumer(startSide::set)
                .build();

        var lockRotationEntry = entries.startBooleanToggle(Text.translatable("text.autoconfig.cropengine.option.lockRotation"), lockRotation.get())
                .setTooltip(Text.translatable("text.autoconfig.cropengine.option.lockRotation.@Tooltip"))
                .setDefaultValue(false)
                .setSaveConsumer(lockRotation::set)
                .build();

        general.addEntry(enabledEntry);
        general.addEntry(cropTypeEntry);
        general.addEntry(pumpkinPatternEntry);
        general.addEntry(carrotPatternEntry);
        general.addEntry(minDelayEntry);
        general.addEntry(maxDelayEntry);
        general.addEntry(startSideEntry);
        general.addEntry(lockRotationEntry);

        builder.setSavingRunnable(() -> {
            config.enabled = enabled.get();
            config.cropType = cropType.get();
            config.farmType = config.cropType == ModConfig.CropType.Pumpkin
                    ? ModConfig.FarmType.SShape
                    : normalizeCarrotPattern(carrotPattern.get());
            config.minDelay = minDelay.get();
            config.maxDelay = maxDelay.get();
            config.startSide = startSide.get();
            config.lockRotation = lockRotation.get();
            AutoConfig.getConfigHolder(ModConfig.class).save();
        });

        return builder.build();
    }

    private static ModConfig.FarmType normalizeCarrotPattern(ModConfig.FarmType pattern) {
        if (pattern == ModConfig.FarmType.Vertical) return ModConfig.FarmType.Vertical;
        return ModConfig.FarmType.SShape;
    }

    private static Text toPatternText(ModConfig.FarmType pattern) {
        return switch (pattern) {
            case SShape -> Text.literal("S-Shaped");
            case Vertical -> Text.literal("Vertical");
        };
    }
}
