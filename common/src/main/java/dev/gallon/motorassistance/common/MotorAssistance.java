package dev.gallon.motorassistance.common;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import dev.gallon.motorassistance.common.event.RenderEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import dev.gallon.motorassistance.common.event.TickEvent;
import dev.gallon.motorassistance.common.services.MotorAssistanceService;
import net.minecraft.client.Minecraft;

public class MotorAssistance {
    private static MotorAssistanceService motorAssistance = null;

    public static void start(MotorAssistanceConfig config) {
        SingleEventBus.listen(TickEvent.class, e -> {
            initOrResetMotorAssistance(config);
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

    private static void initOrResetMotorAssistance(MotorAssistanceConfig config) {
        if (motorAssistance == null && Minecraft.getInstance().player != null) {
            motorAssistance = new MotorAssistanceService(config);
        } else if (motorAssistance != null && Minecraft.getInstance().player == null) {
            motorAssistance = null;
        }
    }
}
