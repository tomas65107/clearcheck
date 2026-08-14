package com.tomas65107.networking;

import com.tomas65107.helpers.Security;
import com.tomas65107.managers.TokenManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class TokenStaterClient {

    public static void handle(TokenStaterServer payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            String serverIp = Minecraft.getInstance().getCurrentServer() != null
                    ? Minecraft.getInstance().getCurrentServer().ip
                    : "singleplayer";

            if (serverIp.equals("singleplayer")) return;

            TokenManager.addNewToClient(serverIp, payload.token());
        });
    }

}
