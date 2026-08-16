package com.tomas65107.clearcheck;

import com.google.gson.Gson;
import com.tomas65107.clearcheck.ui.NotAllowedEntry;
import com.tomas65107.managers.DataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = clearcheck.MODID, value = Dist.DEDICATED_SERVER)
public class PacketReinforcer {

    private static final Map<ServerPlayer, Integer> handshakeMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!FMLEnvironment.dist.isDedicatedServer()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        DataManager.load();

        // Start handshake timer
        handshakeMap.put(player, 0);
    }

    public static void removePlayerFromQueue(ServerPlayer player) {
        handshakeMap.remove(player);
    }

    public static void disconnectPlayerHasNoToken(ServerPlayer player) {
        player.connection.disconnect(Component.literal(ClearcheckDisconnections.NO_TOKEN.getFullId()));
    }

    public static void disconnectMalformedCredentials(ServerPlayer player) {
        player.connection.disconnect(Component.literal(ClearcheckDisconnections.INCORRECT_TOKEN.getFullId()));
    }

    public static void disconnectBannedAssets(ServerPlayer player, List<NotAllowedEntry> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        player.connection.disconnect(Component.literal(ClearcheckDisconnections.DISALLOWED_ASSETS.getFullIdAndSuffix(json)));
    }

    private static Component getFormatedOutput(String title, String message, String expl) {
        return getFormatedOutput(title, message, expl, null);
    }

    private static Component getFormatedOutput(String title, String message, String expl, String website) {
        return Component.empty()
                .append(Component.literal(title).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED))
                .append(Component.literal("\n"))
                .append(Component.literal(message).withStyle(ChatFormatting.WHITE))
                .append(expl != null ? Component.literal("\n\n"+expl).withStyle(ChatFormatting.GRAY) : Component.empty())
                .append(website != null ? Component.literal("\nLearn more: "+website).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE) : Component.empty());
    }

    private static Component getFormatedOutputCUSTOM(String title, String message, String website, Component customComponents) {
        return Component.empty()
                .append(Component.literal(title).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED))
                .append(Component.literal("\n"))
                .append(Component.literal(message).withStyle(ChatFormatting.WHITE))
                .append(Component.literal("\n\n"))
                .append(customComponents)
                .append(website != null ? Component.literal("\nLearn more: "+website).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE) : Component.empty());
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!FMLEnvironment.dist.isDedicatedServer()) return;
        handshakeMap.entrySet().removeIf(entry -> {
            ServerPlayer player = entry.getKey();
            int ticks = entry.getValue() + 1;

            if (ticks >= Configs.PACKETS_TIMEOUT.get()) {
                clearcheck.LOGGER.warn("Kicking player " + player.getName().getString() + " for not sending packet in time...");
                player.connection.disconnect(Component.literal(ClearcheckDisconnections.PACKETS_NOT_SENT.getFullId()));
                return true; // remove from map
            } else {
                entry.setValue(ticks);
                return false;
            }
        });
    }
}
