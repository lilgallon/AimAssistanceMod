package dev.gallon.motorassistance.common;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import dev.gallon.motorassistance.common.event.RenderEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import dev.gallon.motorassistance.common.event.TickEvent;
import dev.gallon.motorassistance.common.services.MotorAssistanceService;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import org.slf4j.Logger;

import java.util.Objects;

public class MotorAssistance {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static MotorAssistanceService motorAssistance = null;
    private static LocalPlayer activePlayer = null;
    private static MotorAssistanceConfig config = null;
    private static boolean started = false;
    private static final FailureGuard FAILURE_GUARD = new FailureGuard((phase, failure) -> {
        motorAssistance = null;
        activePlayer = null;
        LOGGER.error(
                "Motor Assistance encountered an unexpected error during {} and has been disabled for this session.",
                phase,
                failure
        );
    });

    public static void start(MotorAssistanceConfig modConfig) {
        config = Objects.requireNonNullElseGet(modConfig, MotorAssistanceConfig::new);
        if (started || FAILURE_GUARD.isDisabled()) {
            return;
        }

        FAILURE_GUARD.run("initialization", () -> {
            SingleEventBus.listen(TickEvent.class, e -> FAILURE_GUARD.run("tick", () -> {
                initOrResetMotorAssistance(config);
                if (motorAssistance != null) {
                    motorAssistance.analyseEnvironment();
                    motorAssistance.analyseBehavior();
                }
            }));

            SingleEventBus.listen(RenderEvent.class, e -> FAILURE_GUARD.run("render", () -> {
                if (motorAssistance != null) {
                    motorAssistance.assistIfPossible();
                }
            }));
            started = true;
        });
    }

    public static void extractIndicatorRenderState(GuiGraphicsExtractor graphics) {
        if (config == null || FAILURE_GUARD.isDisabled()) {
            return;
        }

        FAILURE_GUARD.run("HUD rendering", () -> {
            AssistanceStatus status;
            if (!config.getAimBlock() && !config.getAimEntity()) {
                status = AssistanceStatus.DISABLED;
            } else if (motorAssistance != null && motorAssistance.isAssisting()) {
                status = AssistanceStatus.ASSISTING;
            } else {
                status = AssistanceStatus.READY;
            }
            AssistanceIndicator.extractRenderState(graphics, config, status);
        });
    }

    private static void initOrResetMotorAssistance(MotorAssistanceConfig config) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            motorAssistance = null;
            activePlayer = null;
        } else if (motorAssistance == null || activePlayer != player) {
            motorAssistance = new MotorAssistanceService(config);
            activePlayer = player;
        }
    }
}
