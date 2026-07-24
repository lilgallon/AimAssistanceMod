package dev.gallon.motorassistance.common;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import dev.gallon.motorassistance.common.event.RenderEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import dev.gallon.motorassistance.common.event.TickEvent;
import dev.gallon.motorassistance.common.services.MotorAssistanceService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class MotorAssistance {
    private static MotorAssistanceService motorAssistance = null;
    private static MotorAssistanceConfig config = null;

    public static void start(MotorAssistanceConfig modConfig) {
        config = modConfig;
        SingleEventBus.listen(TickEvent.class, e -> {
            initOrResetMotorAssistance(modConfig);
            if (motorAssistance != null) {
                motorAssistance.analyseEnvironment();
                motorAssistance.analyseBehavior();
            }
        });

        SingleEventBus.listen(RenderEvent.class, e -> {
            if (motorAssistance != null) {
                motorAssistance.assistIfPossible();
            }
        });
    }

    public static void extractIndicatorRenderState(GuiGraphicsExtractor graphics) {
        if (config == null) {
            return;
        }

        AssistanceStatus status;
        if (!config.getAimBlock() && !config.getAimEntity()) {
            status = AssistanceStatus.DISABLED;
        } else if (motorAssistance != null && motorAssistance.isAssisting()) {
            status = AssistanceStatus.ASSISTING;
        } else {
            status = AssistanceStatus.READY;
        }
        AssistanceIndicator.extractRenderState(graphics, config, status);
    }

    private static void initOrResetMotorAssistance(MotorAssistanceConfig config) {
        if (motorAssistance == null && Minecraft.getInstance().player != null) {
            motorAssistance = new MotorAssistanceService(config);
        } else if (motorAssistance != null && Minecraft.getInstance().player == null) {
            motorAssistance = null;
        }
    }
}
