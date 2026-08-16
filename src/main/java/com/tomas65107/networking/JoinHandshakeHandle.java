package com.tomas65107.networking;

import com.mojang.logging.LogUtils;
import com.tomas65107.clearcheck.PacketReinforcer;
import com.tomas65107.clearcheck.Configs;
import com.tomas65107.clearcheck.clearcheck;
import com.tomas65107.helpers.Security;
import com.tomas65107.clearcheck.ui.NotAllowedEntry;
import com.tomas65107.managers.DataManager;
import com.tomas65107.managers.ServerSecret;
import com.tomas65107.managers.TokenManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.neoforged.neoforge.network.PacketDistributor.sendToPlayer;

public class JoinHandshakeHandle {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void handle(JoinHandshake payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!FMLEnvironment.dist.isDedicatedServer()) return;
            String playerName = ctx.player().getName().getString();

            // Packet reinforcement and logging
            LOGGER.info("Player [" + playerName + "] joined with correct packets and metadata: '"+ payload.userData() + "'. More info in clearcheck file");
            DataManager.lastLoginPlayerHistory.put(playerName, payload.userData());
            DataManager.lastLoginPlayerModsAndPacks.put(playerName, "| MOD DATA |  "+payload.modList() + "          | PACKS DATA |  "+payload.packList());
            PacketReinforcer.removePlayerFromQueue((ServerPlayer) ctx.player());
            DataManager.save();

            // Token validation
            if (Configs.PERFORM_AUTH.get()) {
                if (Objects.equals(payload.token(), "not_stated")) { // Not yet registered
                    if (TokenManager.retrieveSignatureForPlayer(playerName) != null) {
                        PacketReinforcer.disconnectPlayerHasNoToken((ServerPlayer) ctx.player());
                        return;
                    }
                    String newPlayerToken = TokenManager.createNewPlayerToken(payload.userData());
                    TokenManager.addNewSignatureForPlayer(playerName, Security.hmac(newPlayerToken, ServerSecret.readDat()));
                    sendToPlayer((ServerPlayer) ctx.player(), new TokenStater(newPlayerToken, ((ServerPlayer) ctx.player()).server.getLocalIp()));
                    LOGGER.info("Created new token for " + playerName + " and saved signature on server. Next log on will player have to send token");
                } else { // Already registered
                    String storedServerSignature = TokenManager.retrieveSignatureForPlayer(playerName);
                    String computedSignature = Security.hmac(payload.token(), ServerSecret.readDat());

                    // Server has lost the signature, but the player still possesses their token.
                    if (Configs.RECONSTRUCT_SIGNATURES.get() && storedServerSignature == null) {
                        TokenManager.addNewSignatureForPlayer(playerName, computedSignature);
                        LOGGER.warn("No signature found for " + playerName + ". Reconstructed and saved the signature from the player's token.");
                    } else {
                        if (Objects.equals(storedServerSignature, computedSignature)) {
                            // Signature matches correctly, allow access and check assets
                        } else {
                            PacketReinforcer.disconnectMalformedCredentials((ServerPlayer) ctx.player());
                            LOGGER.warn(playerName + " signature does not match correctly! Disconnecting.");
                            return;
                        }
                    }
                }
            }

            //Detecting disallowed assets
            List<NotAllowedEntry> notAllowedEntries = new ArrayList<>();

            for (String disallowedAsset : DataManager.notAllowedContent) {
                String keyword = disallowedAsset.toLowerCase();

                for (String line : payload.modList().split("\\R")) {
                    String lowerLine = line.toLowerCase();

                    if (lowerLine.contains(keyword)
                            && DataManager.whitelistContent.stream()
                            .map(String::toLowerCase) .noneMatch(lowerLine::contains)) {
                        notAllowedEntries.add(new NotAllowedEntry(NotAllowedEntry.Types.MOD, line));

                    }
                }

                for (String line : payload.packList().split("\\R")) {
                    String lowerLine = line.toLowerCase();

                    if (lowerLine.contains(keyword)
                            && DataManager.whitelistContent.stream()
                            .map(String::toLowerCase) .noneMatch(lowerLine::contains)) {
                        notAllowedEntries.add(new NotAllowedEntry(NotAllowedEntry.Types.PACK, line));

                    }
                }
            }

            if (!notAllowedEntries.isEmpty()) {
                DataManager.lastLoginPlayerBannedModifications.put(playerName, String.join("\n", notAllowedEntries.stream()
                        .map(entry -> entry.type.formatted + entry.modName)
                        .toList()));
                DataManager.save();
                PacketReinforcer.disconnectBannedAssets((ServerPlayer) ctx.player(), notAllowedEntries);
            }

            clearcheck.LOGGER.info(playerName + " authentication completed successfully.");
        });
    }

}
