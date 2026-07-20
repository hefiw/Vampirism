package de.teamlapen.vampirism.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class HostileToVampireConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Set<ResourceLocation> EXCEPTIONS = new HashSet<>();

    public static void load(Path configDir) {

        try {

            Path file = configDir.resolve("vampirism_hostile_to_vampires.json");

            if (!Files.exists(file)) {

                JsonObject root = new JsonObject();

                root.addProperty("comment",
                        "These mobs remain hostile to vampires");

                root.add("exceptions",
                        GSON.toJsonTree(new String[]{
                                "minecraft:warden"
                        }));

                try (Writer writer = Files.newBufferedWriter(file)) {
                    GSON.toJson(root, writer);
                }
            }

            EXCEPTIONS.clear();

            try (Reader reader = Files.newBufferedReader(file)) {

                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                json.getAsJsonArray("exceptions")
                        .forEach(e ->
                                EXCEPTIONS.add(
                                        ResourceLocation.parse(
                                                e.getAsString()
                                        )
                                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isException(ResourceLocation id) {
        return EXCEPTIONS.contains(id);
    }
}