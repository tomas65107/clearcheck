package com.tomas65107.clearcheck.ui;

import com.tomas65107.clearcheck.Configs;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static com.tomas65107.clearcheck.Configs.HIDE_CONSENT_SCREEN;

public class ClearCheckTransparencyScreen extends Screen {

    private final Screen parent;

    public ClearCheckTransparencyScreen(Screen goBackTo) {
        super(Component.empty());
        this.parent = goBackTo;
    }

    @Override
    protected void init() {
        super.init();

        LinearLayout layout = LinearLayout.vertical().spacing(8);
        layout.defaultCellSetting().alignHorizontallyCenter();

        layout.addChild(new StringWidget(Component.translatable("gui.clearcheck.consent_screen.title").withStyle(ChatFormatting.BOLD), this.font));
        layout.addChild(new MultiLineTextWidget(Component.translatable("gui.clearcheck.consent_screen.message"), this.font).setCentered(true).setMaxWidth(this.width - 90));

        layout.addChild(Button.builder(Component.translatable("mco.snapshotRealmsPopup.urlText"), n -> Util.getPlatform().openUri("https://docs.tomas65107.dev/clearcheck/transparency")).width(200).build());

        LinearLayout buttonLayout = LinearLayout.horizontal().spacing(8);
        buttonLayout.addChild(Button.builder(CommonComponents.GUI_BACK, b -> this.onClose()).build());
        buttonLayout.addChild(Button.builder(CommonComponents.GUI_ACKNOWLEDGE, b -> {
            HIDE_CONSENT_SCREEN.set(true);
            HIDE_CONSENT_SCREEN.save();
            Minecraft.getInstance().setScreen(parent);
        }).build());
        layout.addChild(buttonLayout);

        layout.arrangeElements();
        layout.setPosition(this.width / 2 - layout.getWidth() / 2, this.height / 2 - layout.getHeight() / 2);
        layout.visitWidgets(this::addRenderableWidget);
    }
}
