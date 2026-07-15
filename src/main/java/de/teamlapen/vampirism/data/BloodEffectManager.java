package de.teamlapen.vampirism.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BloodEffectManager {

    private static final Map<ResourceLocation,
            List<BloodEffectData>> EFFECTS =
            new ConcurrentHashMap<>();

    public static void setEffects(
            Map<ResourceLocation,
                    List<BloodEffectData>> effects
    ) {
        EFFECTS.clear();
        EFFECTS.putAll(effects);
    }

    public static List<BloodEffectData> get(
            EntityType<?> type
    ) {
        return EFFECTS.getOrDefault(
                BuiltInRegistries.ENTITY_TYPE.getKey(type),
                Collections.emptyList()
        );
    }
}