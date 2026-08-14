package com.tomas65107.clearcheck;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.CommandEvent;

@EventBusSubscriber(modid = clearcheck.MODID)
public class PlayerMsgLog {

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        if (event.getParseResults().getContext().getSource() == null) return;
        if (event.getParseResults().getContext().getSource().getPlayer() == null) return;

        clearcheck.LOGGER.info("Player " + event.getParseResults().getContext().getSource().getPlayer().getName().getString() + " executed: /" + event.getParseResults().getReader().getString());
    }

}
