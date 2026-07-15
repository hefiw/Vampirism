package de.teamlapen.vampirism.data;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public record BloodEffectData(ResourceLocation effect, int duration, int amplifier) {
    public static final Codec<BloodEffectData> CODEC =
            Codec.unboundedMap(
                    Codec.STRING,
                    Codec.STRING
            ).xmap(
                    map -> new BloodEffectData(
                            ResourceLocation.parse(map.get("effect")),
                            Integer.parseInt(map.get("duration")),
                            Integer.parseInt(map.get("amplifier"))
                    ),
                    data -> java.util.Map.of(
                            "effect", data.effect().toString(),
                            "duration", String.valueOf(data.duration()),
                            "amplifier", String.valueOf(data.amplifier())
                    )
            );
}