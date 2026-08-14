package com.tomas65107.managers;

import net.neoforged.fml.loading.FMLEnvironment;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonManager {
    /// called at client init
    public static String prepareDirectory() {
        try {
            Path basePath;

            if (FMLEnvironment.dist.isClient()) {
                String os = System.getProperty("os.name").toLowerCase();

                if (os.contains("win")) {
                    String appData = System.getenv("APPDATA");

                    if (appData == null || appData.isBlank()) {
                        basePath = Paths.get(System.getProperty("user.home"), "AppData", "Roaming");
                    } else {
                        basePath = Paths.get(appData);
                    }

                } else if (os.contains("mac")) {
                    basePath = Paths.get(System.getProperty("user.home"), "Library", "Application Support");
                } else {
                    String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");

                    if (xdgConfigHome != null && !xdgConfigHome.isBlank()) {
                        basePath = Paths.get(xdgConfigHome);
                    } else {
                        basePath = Paths.get(System.getProperty("user.home"), ".config");
                    }
                }
            } else {
                basePath = Paths.get(System.getProperty("user.dir")); // server side
            }

            Path modDir = basePath.resolve("clearcheck");

            Files.createDirectories(modDir);

            return modDir.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed while initializing Clearcheck core services", e);
        }
    }
}
