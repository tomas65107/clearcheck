package com.tomas65107.clearcheck;

import com.tomas65107.managers.TokenManager;
import com.tomas65107.networking.ClearCheckHandshakeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.List;
import java.util.Objects;

import static net.neoforged.neoforge.network.PacketDistributor.sendToServer;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = clearcheck.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = clearcheck.MODID, value = Dist.CLIENT)
public class clearcheckClient {
    public clearcheckClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static final ResourceLocation ICON_ID = ResourceLocation.fromNamespaceAndPath("clearcheck", "textures/gui/icon_id.png");
    private static final ResourceLocation ICON_NO_ID = ResourceLocation.fromNamespaceAndPath("clearcheck", "textures/gui/icon_no_id.png");

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        String serverIp = Minecraft.getInstance().getCurrentServer() != null
                ? Minecraft.getInstance().getCurrentServer().ip
                : "singleplayer";

        if (serverIp.equals("singleplayer")) return;

        clearcheck.LOGGER.debug("Joined server: " + serverIp);
        if (TokenManager.retrieveForServer(serverIp) != null) {
            sendToServer(new ClearCheckHandshakeClient(InfoCollector.getMods(), InfoCollector.getResourcePacks(), InfoCollector.getUserInfo(), TokenManager.retrieveForServer(serverIp)));
        } else {
            sendToServer(new ClearCheckHandshakeClient(InfoCollector.getMods(), InfoCollector.getResourcePacks(), InfoCollector.getUserInfo(), "not_stated"));
        }

    }

    public static void renderServerIcon(int top, int left, int width, GuiGraphics guiGraphics, String ip, int mouseX, int mouseY) {
        int iconX =  left + width - 21;
        int iconY = top + 5;

        boolean savedTokenForServer = TokenManager.retrieveForServer(ip) != null;
        if (!savedTokenForServer) return;

        guiGraphics.blit(
                ICON_ID,
                iconX,
                iconY,
                0,
                0,
                16,
                16,
                16,
                16
        );

        if (mouseX >= iconX+3 && mouseX <= iconX+7 + 10 && mouseY >= iconY+4 && mouseY <= iconY+3 + 10) {

            assert Minecraft.getInstance().screen != null;
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(
                    Component.translatable("gui.clearcheck.token_saved")
            );
        }
    }

    public static Component joinComponents(List<Component> components) {
        if (components.isEmpty()) {
            return Component.empty();
        }

        MutableComponent result = components.getFirst().copy();

        for (int i = 1; i < components.size(); i++) {
            result.append("\n");
            result.append(components.get(i));
        }

        System.out.println(result);
        return result;
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
    }
}
