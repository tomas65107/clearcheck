package com.tomas65107.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tomas65107.clearcheck.clearcheck;
import com.tomas65107.helpers.Security;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenManager {
    private static final File TOKENS_FILE;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        if (FMLEnvironment.dist.isClient()) {
            TOKENS_FILE = new File(clearcheck.CLEARCHECK_DIR, "tokens.json");
        } else {
            TOKENS_FILE = new File(clearcheck.CLEARCHECK_DIR, "player_signatures.json");
        }
    }

    public static String createNewPlayerToken(String playerMetadata) {
        StringBuilder token = new StringBuilder();
        token.append(UUID.randomUUID());
        token.append(".");
        token.append(System.currentTimeMillis());
        token.append(".");
        token.append(Security.encrypt(playerMetadata, "metadata"));
        return token.toString();
    }

    /// client
    public static String retrieveForServer(String serverIpRaw) {
        if (!TOKENS_FILE.exists()) return null;
        try (Reader reader = new FileReader(TOKENS_FILE)) {
            Map<String, Object> combined = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());

            if (combined == null) return null;

            Map<String, String> map = gson.fromJson(
                    gson.toJson(combined.get("savedTokensClient")),
                    new TypeToken<Map<String, String>>() {}.getType()
            );
            return map.get(serverIpRaw);

        } catch (IOException e) {
            throw new RuntimeException("Clearcheck failed while retrieving information." + e + "\nPlease report this!");
        }
    }

    /// client
    public static List<String> getSavedServers() {
        if (!TOKENS_FILE.exists()) return List.of();

        try (Reader reader = new FileReader(TOKENS_FILE)) {
            Map<String, Object> combined = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());

            if (combined == null) return List.of();

            Map<String, String> map = gson.fromJson(
                    gson.toJson(combined.get("savedTokensClient")),
                    new TypeToken<Map<String, String>>() {}.getType()
            );

            if (map == null) return List.of();

            return List.copyOf(map.keySet());

        } catch (IOException e) {
            throw new RuntimeException("Clearcheck failed while retrieving saved servers." + e + "\nPlease report this!");
        }
    }

    /// client
    public static void addNewToClient(String serverIp, String secureToken) {
        Map<String, Object> combined = new HashMap<>();
        Map<String, String> map = new HashMap<>();

        // read
        try (Reader reader = new FileReader(TOKENS_FILE)) {
            combined = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            if (combined == null) combined = new HashMap<>();

            map = gson.fromJson(
                    gson.toJson(combined.get("savedTokensClient")),
                    new TypeToken<Map<String, String>>() {}.getType()
            );

            if (map == null) map = new HashMap<>();
        } catch (IOException ignored) {}

        // modify
        map.put(serverIp, secureToken);
        combined.put("savedTokensClient", map);

        // write
        try (FileWriter writer = new FileWriter(TOKENS_FILE)) {
            gson.toJson(combined, writer);
        } catch (IOException e) {
            throw new RuntimeException("Clearcheck failed while saving information." + e + "\nPlease report this!");
        }
    }

    /// server
    public static String retrieveSignatureForPlayer(String player) {
        if (!TOKENS_FILE.exists()) return null;
        try (Reader reader = new FileReader(TOKENS_FILE)) {
            Map<String, Object> combined = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());

            if (combined == null) return null;

            Map<String, String> map = gson.fromJson(
                    gson.toJson(combined.get("playerSignatures")),
                    new TypeToken<Map<String, String>>() {}.getType()
            );
            return map.get(player);

        } catch (IOException e) {
            throw new RuntimeException("Clearcheck failed while retrieving information." + e + "\nPlease report this!");
        }
    }

    /// server
    public static void addNewSignatureForPlayer(String player, String signatureOfToken) {
        Map<String, Object> combined = new HashMap<>();
        Map<String, String> map = new HashMap<>();

        // read
        try (Reader reader = new FileReader(TOKENS_FILE)) {
            combined = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            if (combined == null) combined = new HashMap<>();

            map = gson.fromJson(
                    gson.toJson(combined.get("playerSignatures")),
                    new TypeToken<Map<String, String>>() {}.getType()
            );

            if (map == null) map = new HashMap<>();
        } catch (IOException ignored) {}

        // modify
        map.put(player, signatureOfToken);
        combined.put("playerSignatures", map);

        // write
        try (FileWriter writer = new FileWriter(TOKENS_FILE)) {
            gson.toJson(combined, writer);
        } catch (IOException e) {
            throw new RuntimeException("Clearcheck failed while saving information." + e + "\nPlease report this!");
        }
    }

}
