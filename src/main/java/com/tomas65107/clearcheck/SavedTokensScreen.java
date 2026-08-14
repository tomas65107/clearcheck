package com.tomas65107.clearcheck;

import com.ibm.icu.message2.Mf2DataModel;
import com.tomas65107.managers.TokenManager;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarFile;

import static com.tomas65107.clearcheck.TextCutter.cutTextComponent;

public class SavedTokensScreen extends Screen {

    private final Screen parent;
    private SavedServersTokens scrolllist;

    public SavedTokensScreen(Screen parent) {
        super(Component.literal("Saved Clearcheck tokens"));
        this.parent = parent;
    }

    @Override

    protected void init() {
        this.scrolllist = new SavedServersTokens(this.width, this.height);
        this.addRenderableWidget(this.scrolllist);

        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 4;
        int totalWidth = buttonWidth * 3 + spacing * 2;
        int x = (this.width - totalWidth) / 2;
        int y = this.height - 30;
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("gui.back"),
                                b -> onClose()
                        )
                        .bounds(x, y, buttonWidth, buttonHeight)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Open folder"),
                                b -> {
                                    Util.getPlatform().openPath(Path.of(clearcheck.CLEARCHECK_DIR));
                                }
                        )
                        .bounds(x + buttonWidth + spacing, y, buttonWidth, buttonHeight)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("mco.snapshotRealmsPopup.urlText"),
                                button -> ConfirmLinkScreen.confirmLinkNow(this, "https://docs.tomas65107.dev/clearcheck/", true)
                        )
                        .bounds(x + (buttonWidth + spacing) * 2, y, buttonWidth, buttonHeight)
                        .build()
        );

    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        if (scrolllist.isEmpty()) {
            int Yindex = 44;
            for (var comp : cutTextComponent(Component.translatable("gui.clearcheck.no_tokens"), 0, 270, true)) {
                guiGraphics.drawCenteredString(
                        this.font,
                        comp.copy().withStyle(ChatFormatting.RED),
                        this.width / 2,
                        Yindex,
                        0xFFFFFF
                );
                Yindex += Minecraft.getInstance().font.lineHeight+1;
            }

            Yindex+= 10;

            for (var comp : cutTextComponent(Component.translatable("gui.clearcheck.no_tokens_tip"), 0, 270, true)) {
                guiGraphics.drawCenteredString(
                        this.font,
                        comp.copy().withStyle(ChatFormatting.GRAY),
                        this.width / 2,
                        Yindex,
                        0xFFFFFF
                );
                Yindex += Minecraft.getInstance().font.lineHeight+1;
            }
        }
    }

    @Override

    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private static class SavedServersTokens extends ObjectSelectionList<SavedServersTokens.ServerEntry> {
        public SavedServersTokens(
                int width,
                int height
        ) {
            super(
                    Minecraft.getInstance(),
                    width,
                    height - 74,
                    32,
                    24
            );

            for (var server : TokenManager.getSavedServers()) {
                this.addEntry(new ServerEntry(server));
            }

        }

        public boolean isEmpty() {
            return this.getItemCount() == 0;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.width -10;
        }

        @Override
        public int getRowWidth() {
            return 300;
        }

        private static class ServerEntry extends ObjectSelectionList.Entry<ServerEntry> {

            private final String ipAddress;

            public ServerEntry(String text) {
                this.ipAddress = text;
            }

            @Override
            public void render(
                    GuiGraphics guiGraphics,
                    int index,
                    int top,
                    int left,
                    int width,
                    int height,
                    int mouseX,
                    int mouseY,
                    boolean hovered,
                    float partialTick
            ) {
                String token = getTokenCreationDate(TokenManager.retrieveForServer(this.ipAddress));
                List<Component> cutIp = cutTextComponent(Component.literal(this.ipAddress), false);
                MutableComponent ip = cutIp.getFirst().copy();

                if (cutIp.size() > 1) ip.append("...");

                guiGraphics.drawString(
                        net.minecraft.client.Minecraft.getInstance().font,
                        ip,
                        left + 5,
                        top + 5,
                        0xFFFFFF
                );

                guiGraphics.drawString(
                        net.minecraft.client.Minecraft.getInstance().font,
                        Component.literal(token).withStyle(ChatFormatting.GRAY),
                        left + width - 5 - Minecraft.getInstance().font.width(token),
                        top + 5,
                        0xFFFFFF
                );
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }

            @Override
            public boolean mouseClicked(
                    double mouseX,
                    double mouseY,
                    int button
            ) {
                if (button == 0) {
                    return true;
                }
                return false;
            }
        }

        public static String getTokenCreationDate(String token) {
            try {
                String[] parts = token.split("\\.");

                if (parts.length < 3) {
                    return "Unsupported token data";
                }

                long timestamp = Long.parseLong(parts[1]);

                return java.time.Instant.ofEpochMilli(timestamp)
                        .atZone(java.time.ZoneId.systemDefault())
                        .format(java.time.format.DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy HH:mm:ss"
                        ));

            } catch (Exception e) {
                return "Unknown date";
            }
        }

    }
}