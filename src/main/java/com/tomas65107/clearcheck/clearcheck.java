package com.tomas65107.clearcheck;

import com.tomas65107.managers.PlayerDataManager;
import com.tomas65107.managers.ServerSecret;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static com.tomas65107.managers.JsonManager.prepareDirectory;
import static com.tomas65107.managers.ServerSecret.SERVER_SECRET;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(clearcheck.MODID)
public class clearcheck {

    public static final String MODID = "clearcheck";
    public static String CLEARCHECK_DIR;
    public static final Logger LOGGER = LogUtils.getLogger();

    public clearcheck(IEventBus modEventBus, ModContainer modContainer) {
        CLEARCHECK_DIR = prepareDirectory();

        modContainer.registerConfig(
                ModConfig.Type.SERVER,
                Configs.CLEARCHECK_CONFIG_SERVER
        );

        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                Configs.CLEARCHECK_CONFIG_CLIENT
        );
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("common setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        if (!FMLEnvironment.dist.isDedicatedServer()) return;

        if (!SERVER_SECRET.exists()) ServerSecret.generateNewDat();
        PlayerDataManager.load();

        LOGGER.info("finished loading PlayerDataManager and server secret");
    }
}
