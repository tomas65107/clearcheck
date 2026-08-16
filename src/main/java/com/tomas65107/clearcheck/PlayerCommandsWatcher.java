package com.tomas65107.clearcheck;

import com.tomas65107.managers.DataManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.CommandEvent;

@EventBusSubscriber(modid = clearcheck.MODID)
public class PlayerCommandsWatcher {

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        if (event.getParseResults().getContext().getSource() == null) return;
        if (event.getParseResults().getContext().getSource().getPlayer() == null) return;

        var player = event.getParseResults().getContext().getSource().getPlayer();
        var command = event.getParseResults().getReader().getString();

        String commandName = command.split("\\s+", 2)[0];

        if (commandName != null) {
            for (var cmd : DataManager.commandsToTrack)
                if (commandName.equalsIgnoreCase(cmd)) {
                    clearcheck.LOGGER.info("Player " + player.getName() + " executed: /" + command);
                    return;
                }
        }
    }

}
