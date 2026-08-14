package com.tomas65107.managers;

import com.tomas65107.clearcheck.clearcheck;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class ServerSecret {
    public static final File SERVER_SECRET = new File(clearcheck.CLEARCHECK_DIR, "server_secret.dat");
    private static final String CHARS = "?,=+6a_]Qyfv6-vr+Vf-bnt?G=K*Z/#4b?ClEarfyBg*DE]gGju3b+/._CHeck8F)pP{mW*4%n-D.*";

    // generate 100 random chars
    public static void generateNewDat() {
        if (FMLEnvironment.dist.isClient()) throw new RuntimeException("Why is this being run as client?");
        try {
            clearcheck.LOGGER.warn("WARNING: New server secret is being generated. This means, all player tokens are unusable! If this is the first server startup with this mod, you can ignore this.");
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 80; i++) sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            sb.append(";").append(System.currentTimeMillis());
            try (FileWriter writer = new FileWriter(SERVER_SECRET)) {
                writer.write(sb.toString());
                clearcheck.LOGGER.warn("New server secret was successfully generated!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String readDat() {
        if (FMLEnvironment.dist.isClient()) throw new RuntimeException("Why is this being run as client?");
        try {
            if (!SERVER_SECRET.exists()) return null;
            return Files.readString(Path.of(SERVER_SECRET.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
