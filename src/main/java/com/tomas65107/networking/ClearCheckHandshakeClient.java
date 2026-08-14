package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClearCheckHandshakeClient(String modList, String packList, String userData, String token) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClearCheckHandshakeClient> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(clearcheck.MODID, "handshake"));

    public static final StreamCodec<FriendlyByteBuf, ClearCheckHandshakeClient> STREAM_CODEC =
            CustomPacketPayload.codec(ClearCheckHandshakeClient::encode, ClearCheckHandshakeClient::decode);

    public static void encode(ClearCheckHandshakeClient msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.modList());
        buf.writeUtf(msg.packList());
        buf.writeUtf(msg.userData());
        buf.writeUtf(msg.token());
    }

    public static ClearCheckHandshakeClient decode(FriendlyByteBuf buf) {
        return new ClearCheckHandshakeClient(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}