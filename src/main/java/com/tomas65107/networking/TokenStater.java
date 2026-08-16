package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TokenStater(String token, String ip) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TokenStater> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(clearcheck.MODID, "tokenstater"));

    public static final StreamCodec<FriendlyByteBuf, TokenStater> STREAM_CODEC =
            CustomPacketPayload.codec(TokenStater::encode, TokenStater::decode);

    public static void encode(TokenStater msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.token());
        buf.writeUtf(msg.ip());
    }

    public static TokenStater decode(FriendlyByteBuf buf) {
        return new TokenStater(buf.readUtf(), buf.readUtf());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}