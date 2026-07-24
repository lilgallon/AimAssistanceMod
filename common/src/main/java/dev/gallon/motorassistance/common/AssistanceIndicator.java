package dev.gallon.motorassistance.common;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public final class AssistanceIndicator {
    private static final int BACKGROUND_COLOR = 0xC0101010;
    private static final int DISABLED_COLOR = 0xFFA0A0A0;
    private static final int READY_COLOR = 0xFF55FF55;
    private static final int ASSISTING_COLOR = 0xFFFFAA00;
    private static final int X = 5;
    private static final int Y = 5;
    private static final int HEIGHT = 13;

    private AssistanceIndicator() {
    }

    public static void extractRenderState(
            GuiGraphicsExtractor graphics,
            MotorAssistanceConfig config,
            AssistanceStatus status
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!config.getShowHudIndicator() || minecraft.player == null || minecraft.gui.screen() != null) {
            return;
        }

        Component label = Component.translatable(switch (status) {
            case DISABLED -> "motorassistancemod.hud.disabled";
            case READY -> "motorassistancemod.hud.ready";
            case ASSISTING -> "motorassistancemod.hud.assisting";
        });
        int color = switch (status) {
            case DISABLED -> DISABLED_COLOR;
            case READY -> READY_COLOR;
            case ASSISTING -> ASSISTING_COLOR;
        };

        Font font = minecraft.font;
        int width = font.width(label) + 11;
        graphics.fill(X, Y, X + width, Y + HEIGHT, BACKGROUND_COLOR);
        graphics.fill(X, Y, X + 2, Y + HEIGHT, color);
        graphics.fill(X + 5, Y + 4, X + 8, Y + 7, color);
        graphics.text(font, label, X + 10, Y + 2, color, true);
    }
}
