package com.tomas65107.networking;

import com.tomas65107.clearcheck.clearcheck;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = clearcheck.MODID)
public class Networking {

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(clearcheck.MODID);

        registrar.playToServer(
                JoinHandshake.TYPE,
                JoinHandshake.STREAM_CODEC,
                JoinHandshakeHandle::handle
        );

        registrar.playToClient(
                TokenStater.TYPE,
                TokenStater.STREAM_CODEC,
                TokenStaterHandle::handle
        );
    }
}