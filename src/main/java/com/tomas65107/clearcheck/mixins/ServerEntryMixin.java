package com.tomas65107.clearcheck.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tomas65107.clearcheck.clearcheckClient;
import com.tomas65107.managers.TokenManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.LanServer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
        ServerSelectionList.OnlineServerEntry.class,
        ServerSelectionList.NetworkServerEntry.class
})
public class ServerEntryMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderIcon(
            GuiGraphics guiGraphics,
            int index,
            int top,
            int left,
            int width,
            int height,
            int mouseX,
            int mouseY,
            boolean hovering,
            float partialTick,
            CallbackInfo ci
    ) {
        if ((Object) this instanceof ServerSelectionList.NetworkServerEntry entry) {
            LanServer serverData = entry.getServerData();

            clearcheckClient.renderServerIcon(
                    top,
                    left,
                    width,
                    guiGraphics,
                    serverData.getAddress(),
                    mouseX,
                    mouseY
            );

        } else if ((Object) this instanceof ServerSelectionList.OnlineServerEntry entry) {
            ServerData serverData = entry.getServerData();

            clearcheckClient.renderServerIcon(
                    top,
                    left,
                    width,
                    guiGraphics,
                    serverData.ip,
                    mouseX,
                    mouseY
            );
        }
    }
}