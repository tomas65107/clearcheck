package com.tomas65107.clearcheck.mixins;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tomas65107.clearcheck.ClearcheckDisconnections;
import com.tomas65107.clearcheck.ui.NotAllowedEntry;
import com.tomas65107.clearcheck.TextCutter;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tomas65107.clearcheck.ClearcheckDisconnections.DISALLOWED_ASSETS;
import static com.tomas65107.clearcheck.ClearcheckDisconnections.getMarkerType;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {

    @Shadow @Final
    private DisconnectionDetails details;

    @Shadow @Final
    private LinearLayout layout;

    @Shadow @Final
    private Screen parent;

    protected DisconnectedScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void clearCheck$init(CallbackInfo ci) {
        Component reason = this.details.reason();
        ClearcheckDisconnections disconnectType = getMarkerType(reason.getString());

        if (disconnectType == null) return; //skip
        ci.cancel();

        if (FMLEnvironment.dist.isDedicatedServer()) return;

        layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
        layout.addChild(new StringWidget(Component.translatable("gui.clearcheck.disconnected"), this.font));
        layout.defaultCellSetting().alignHorizontallyCenter().padding(3);
        layout.addChild(new MultiLineTextWidget(Component.translatable("clearcheck."+disconnectType.id+".title").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), this.font).setMaxWidth(this.width - 90).setCentered(true));
        layout.addChild(new MultiLineTextWidget(Component.translatable("clearcheck."+disconnectType.id+".message"), this.font).setMaxWidth(this.width - 90).setCentered(true));
        if (!Component.translatable("clearcheck."+disconnectType.id+".explanation").getString().equals("clearcheck."+disconnectType.id+".explanation"))
            layout.addChild(new MultiLineTextWidget(Component.translatable("clearcheck."+disconnectType.id+".explanation").withStyle(ChatFormatting.GRAY), Minecraft.getInstance().font).setMaxWidth(this.width - 90).setCentered(false));
        layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
        renderPayload(disconnectType, reason.getString());

        if (disconnectType.website != null) this.layout.addChild(Button.builder(Component.translatable("gui.clearcheck.open_website"), n -> Util.getPlatform().openUri(disconnectType.website)).width(200).build());
        layout.defaultCellSetting().alignHorizontallyCenter().padding(2);
        layout.addChild(Button.builder(Component.translatable("gui.toMenu"), b -> {Minecraft.getInstance().setScreen(parent);}).width(200).build());
        layout.arrangeElements();
        layout.visitWidgets(this::clearCheck$addWidget);
        repositionElements();
    }

    private void renderPayload(ClearcheckDisconnections disconnection, String reason) {
        if (disconnection.equals(DISALLOWED_ASSETS)) {
            Pattern pattern = Pattern.compile("^.*\\|#\\|([^|]*)$");
            Matcher matcherRaw = pattern.matcher(reason);
            if (!matcherRaw.find()) return;

            String jsonString = matcherRaw.group(1);

            Gson gson = new Gson();
            List<NotAllowedEntry> list = gson.fromJson(
                    jsonString,
                    new TypeToken<List<NotAllowedEntry>>() {}.getType()

            );

            Pattern modPattern = Pattern.compile("\\[(.*?)\\].*?: ([^@]+)@");
            Pattern packPattern = Pattern.compile("\\[(.*?)\\] (.*?) ::");

            MutableComponent assetsTable = Component.empty();
            assetsTable.append(Component.literal("type        name\n").withStyle(ChatFormatting.DARK_GRAY));
            for (var asset : list) {
                Matcher matcher;

                if (asset.type == NotAllowedEntry.Types.MOD) {
                    matcher = modPattern.matcher(asset.modName);
                } else {
                    matcher = packPattern.matcher(asset.modName);
                }

                assetsTable.append(Component.literal(asset.type.formatted).withStyle(ChatFormatting.GRAY));

                if (!matcher.find()) {
                    var cutComponent = TextCutter.cutTextComponent(Component.literal(asset.modName.substring(3)), false);
                    MutableComponent compont = cutComponent.getFirst().copy();
                    if (cutComponent.size() > 1) compont.append("...");

                    assetsTable.append(compont.withStyle(ChatFormatting.YELLOW));
                    assetsTable.append(Component.literal("\n"));
                } else {
                    var cutComponentGroup2 = TextCutter.cutTextComponent(Component.literal(matcher.group(2)), 0, 180, false);
                    MutableComponent compont2 = cutComponentGroup2.getFirst().copy();
                    if (cutComponentGroup2.size() > 1) compont2.append("...");

                    assetsTable.append(
                            Component.literal(matcher.group(1)).withStyle(ChatFormatting.GOLD)
                                    .append(Component.literal(" - ").withStyle(ChatFormatting.GOLD))
                                    .append(compont2.withStyle(ChatFormatting.YELLOW))
                    );
                    assetsTable.append(Component.literal("\n"));
                }
            }

            assert Minecraft.getInstance().screen != null;
            layout.addChild(new MultiLineTextWidget(assetsTable, this.font).setMaxWidth(this.width - 90).setCentered(false));
        }
    }

    @Unique
    private void clearCheck$addWidget(AbstractWidget widget) {
        this.addRenderableWidget(widget);
    }
}