package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import com.tomas65107.managers.TokenManager;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TokenStaterHandle {

    public static void handle(TokenStater payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            String serverIp = Minecraft.getInstance().getCurrentServer() != null
                    ? Minecraft.getInstance().getCurrentServer().ip : "singleplayer";

            if (serverIp.equals("singleplayer")) return;

            TokenManager.addNewToClient(serverIp, payload.token());
            clearcheck.LOGGER.info("Added a token for server ip: " + serverIp);
        });
    }

}
