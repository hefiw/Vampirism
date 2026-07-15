package de.teamlapen.vampirism.data.reloadlistener;

import com.google.gson.*;
import de.teamlapen.vampirism.data.BloodEffectData;
import de.teamlapen.vampirism.data.BloodEffectManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodEffectReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public BloodEffectReloadListener() {
        super(GSON, "blood_effects");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, List<BloodEffectData>> loaded = new HashMap<>();
        for (JsonElement file : objects.values()) {
            JsonObject root = file.getAsJsonObject();
            for (String entityId : root.keySet()) {
                JsonArray array = root.getAsJsonArray(entityId);

                List<BloodEffectData> effects = new ArrayList<>();

                for (JsonElement effectElement : array) {

                    JsonObject effectObj = effectElement.getAsJsonObject();
                    effects.add(
                            new BloodEffectData(
                                    ResourceLocation.parse(effectObj.get("effect").getAsString()),
                                    effectObj.get("duration").getAsInt(),
                                    effectObj.get("amplifier").getAsInt()
                            )
                    );
                }

                loaded.put(
                        ResourceLocation.parse(entityId),
                        effects
                );
            }
        }

        BloodEffectManager.setEffects(loaded);
    }
}
