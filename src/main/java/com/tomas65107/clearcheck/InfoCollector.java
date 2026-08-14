package com.tomas65107.clearcheck;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.neoforged.fml.ModList;
import org.lwjgl.system.Configuration;

import java.util.List;

public class InfoCollector {

    public static String getUserInfo() {

        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String userName = Configs.SHARE_USERNAME.get() ? System.getProperty("user.name") : "not_shared";

        return osName + "@" + osVersion + " - " + userName;
    }

    public static String getMods() {
        List<String> mods =  ModList.get().getMods().stream()
                .map(mod -> "-> [" + mod.getDisplayName() + "] " + mod.getOwningFile().getFile().getFileName()
                        + " : " + mod.getModId() + "@" + mod.getVersion()
                )
                .toList();

        StringBuilder modsString = new StringBuilder();
        for (String mod : mods) {
            modsString.append(mod).append("\n");
        }

        return modsString.toString();
    }

    public static String getResourcePacks() {
        StringBuilder packsList = new StringBuilder();

        PackRepository repo = Minecraft.getInstance().getResourcePackRepository();

        for (Pack info : repo.getAvailablePacks()) {
            try {
                String fileName = info.getId();
                String packName = info.getTitle().getString();
                String description = info.getDescription().getString();

                packsList.append("-> [" + packName + "] " + fileName + " :: " + description).append("\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (packsList.isEmpty()) {return "No packs installed";}

        return packsList.toString();
    }

}