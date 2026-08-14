package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TokenStaterServer(String token, String ip) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TokenStaterServer> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(clearcheck.MODID, "tokenstater"));

    public static final StreamCodec<FriendlyByteBuf, TokenStaterServer> STREAM_CODEC =
            CustomPacketPayload.codec(TokenStaterServer::encode, TokenStaterServer::decode);

    public static void encode(TokenStaterServer msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.token());
        buf.writeUtf(msg.ip());
    }

    public static TokenStaterServer decode(FriendlyByteBuf buf) {
        return new TokenStaterServer(buf.readUtf(), buf.readUtf());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}