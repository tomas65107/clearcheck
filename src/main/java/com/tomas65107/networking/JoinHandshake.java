package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record JoinHandshake(String modList, String packList, String userData, String token) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<JoinHandshake> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(clearcheck.MODID, "handshake"));

    public static final StreamCodec<FriendlyByteBuf, JoinHandshake> STREAM_CODEC =
            CustomPacketPayload.codec(JoinHandshake::encode, JoinHandshake::decode);

    public static void encode(JoinHandshake msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.modList());
        buf.writeUtf(msg.packList());
        buf.writeUtf(msg.userData());
        buf.writeUtf(msg.token());
    }

    public static JoinHandshake decode(FriendlyByteBuf buf) {
        return new JoinHandshake(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}