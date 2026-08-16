package com.tomas65107.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tomas65107.clearcheck.clearcheck;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    public static final File SERVER_DATA = new File(clearcheck.CLEARCHECK_DIR, "clearcheck_data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Map<String, String> lastLoginPlayerHistory = new HashMap<>();
    public static Map<String, String> lastLoginPlayerBannedModifications = new HashMap<>();
    public static Map<String, String> lastLoginPlayerModsAndPacks = new HashMap<>();
    public static List<String> notAllowedContent = new ArrayList<>();
    public static List<String> whitelistContent = new ArrayList<>();
    public static List<String> commandsToTrack = new ArrayList<>();


    public static void save() {
        if (FMLEnvironment.dist.isClient()) throw new RuntimeException("This cant be executed on client!");
        try (FileWriter writer = new FileWriter(SERVER_DATA)) {
            Map<String, Object> combined = new HashMap<>();
            combined.put("lastLoginPlayerHistory", lastLoginPlayerHistory);
            combined.put("lastLoginPlayerBannedModifications", lastLoginPlayerBannedModifications);
            combined.put("lastLoginPlayerModsAndPacks", lastLoginPlayerModsAndPacks);
            combined.put("notAllowedContent", notAllowedContent);
            combined.put("whitelistContent", whitelistContent);
            combined.put("commandsToTrack", commandsToTrack);
            gson.toJson(combined, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (FMLEnvironment.dist.isClient()) throw new RuntimeException("This cant be executed on client!");
        try {
            if (!SERVER_DATA.exists()) return;
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> combined = gson.fromJson(new FileReader(SERVER_DATA), type);
            if (combined != null) {
                lastLoginPlayerHistory = gson.fromJson(gson.toJson(combined.get("lastLoginPlayerHistory")),
                        new TypeToken<Map<String, String>>() {}.getType());

                lastLoginPlayerBannedModifications = gson.fromJson(gson.toJson(combined.get("lastLoginPlayerBannedModifications")),
                        new TypeToken<Map<String, String>>() {}.getType());

                lastLoginPlayerModsAndPacks = gson.fromJson(gson.toJson(combined.get("lastLoginPlayerModsAndPacks")),
                        new TypeToken<Map<String, String>>() {}.getType());

                notAllowedContent = gson.fromJson(gson.toJson(combined.get("notAllowedContent")),
                        new TypeToken<List<String>>() {}.getType());

                whitelistContent = gson.fromJson(gson.toJson(combined.get("whitelistContent")),
                        new TypeToken<List<String>>() {}.getType());

                commandsToTrack = gson.fromJson(gson.toJson(combined.get("commandsToTrack")),
                        new TypeToken<List<String>>() {}.getType());

                if (lastLoginPlayerHistory == null) lastLoginPlayerHistory = new HashMap<>();
                if (lastLoginPlayerModsAndPacks == null) lastLoginPlayerModsAndPacks = new HashMap<>();
                if (lastLoginPlayerBannedModifications == null) lastLoginPlayerBannedModifications = new HashMap<>();
                if (notAllowedContent == null) notAllowedContent = new ArrayList<>();
                if (whitelistContent == null) whitelistContent = new ArrayList<>();
                if (commandsToTrack == null) commandsToTrack = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}