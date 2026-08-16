package com.tomas65107.clearcheck.mixins;

import com.tomas65107.clearcheck.Configs;
import com.tomas65107.clearcheck.ui.ClearCheckTransparencyScreen;
import com.tomas65107.clearcheck.ui.SavedTokensScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow
    private Button editButton;

    protected MultiplayerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void transparencyAlert(CallbackInfo ci) {
        if (Configs.HIDE_CONSENT_SCREEN.isFalse()) {
            this.minecraft.setScreen(new ClearCheckTransparencyScreen(this));
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addClearCheckButton(CallbackInfo ci) {

        int x = this.editButton.getX() - 24;
        int y = this.editButton.getY();

        SpriteIconButton spriteiconbutton = this.addRenderableWidget(
                SpriteIconButton.builder(
                                Component.translatable(""),
                                b -> {
                                    assert this.minecraft != null;
                                    this.minecraft.setScreen(new SavedTokensScreen(this));
                                },
                                true
                        )
                        .size(20, 20)
                        .sprite(
                                ResourceLocation.fromNamespaceAndPath(
                                        "clearcheck",
                                        "icon_token_list"
                                ),
                                16,
                                16
                        )
                        .build()
        );

        spriteiconbutton.setPosition(x, y);
    }

    @Unique
    private void clearCheck$addWidget(AbstractWidget widget) {
        this.addRenderableWidget(widget);
    }
}