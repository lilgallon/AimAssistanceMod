package dev.gallon.motorassistance.forge.config;

import com.mojang.logging.LogUtils;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class MotorAssistanceConfigScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int LABEL_WIDTH = 190;
    private static final int CONTROL_WIDTH = 140;
    private static final int OPTION_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 95;
    private static final int INVALID_TEXT_COLOR = 0xFFFF5555;
    private static final String OPTION_PREFIX = "text.autoconfig.motorassistancemod.option.modConfig.";

    private final Screen parent;
    private final MotorAssistanceConfig draft;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Set<EditBox> invalidFields = new HashSet<>();
    private Button doneButton;

    public MotorAssistanceConfigScreen(Screen parent) {
        this(parent, copy(TheModConfig.config));
    }

    private MotorAssistanceConfigScreen(Screen parent, MotorAssistanceConfig draft) {
        super(Component.translatable("text.autoconfig.motorassistancemod.title"));
        this.parent = parent;
        this.draft = draft;
    }

    @Override
    protected void init() {
        layout.removeChildren();
        invalidFields.clear();
        doneButton = null;
        layout.addTitleHeader(title, font);

        LinearLayout options = LinearLayout.vertical().spacing(4);
        options.defaultCellSetting().alignHorizontallyCenter();
        options.addChild(booleanOption(
                "showHudIndicator",
                draft.getShowHudIndicator(),
                draft::setShowHudIndicator
        ));
        options.addChild(doubleOption(
                "fov",
                draft.getFov(),
                MotorAssistanceConfig.MIN_FOV,
                MotorAssistanceConfig.MAX_FOV,
                draft::setFov
        ));
        options.addChild(booleanOption("aimBlock", draft.getAimBlock(), draft::setAimBlock));
        options.addChild(doubleOption(
                "blockRange",
                draft.getBlockRange(),
                MotorAssistanceConfig.MIN_RANGE,
                MotorAssistanceConfig.MAX_RANGE,
                draft::setBlockRange
        ));
        options.addChild(longOption(
                "miningInteractionDuration",
                draft.getMiningInteractionDuration(),
                MotorAssistanceConfig.MIN_DURATION,
                MotorAssistanceConfig.MAX_DURATION,
                draft::setMiningInteractionDuration
        ));
        options.addChild(longOption(
                "miningAssistanceDuration",
                draft.getMiningAssistanceDuration(),
                MotorAssistanceConfig.MIN_DURATION,
                MotorAssistanceConfig.MAX_DURATION,
                draft::setMiningAssistanceDuration
        ));
        options.addChild(doubleOption(
                "miningAimForce",
                draft.getMiningAimForce(),
                MotorAssistanceConfig.MIN_AIM_FORCE,
                MotorAssistanceConfig.MAX_AIM_FORCE,
                draft::setMiningAimForce
        ));
        options.addChild(booleanOption("aimEntity", draft.getAimEntity(), draft::setAimEntity));
        options.addChild(doubleOption(
                "entityRange",
                draft.getEntityRange(),
                MotorAssistanceConfig.MIN_RANGE,
                MotorAssistanceConfig.MAX_RANGE,
                draft::setEntityRange
        ));
        options.addChild(doubleOption(
                "attackInteractionSpeed",
                draft.getAttackInteractionSpeed(),
                MotorAssistanceConfig.MIN_ATTACK_INTERACTION_SPEED,
                MotorAssistanceConfig.MAX_ATTACK_INTERACTION_SPEED,
                draft::setAttackInteractionSpeed
        ));
        options.addChild(longOption(
                "attackInteractionDuration",
                draft.getAttackInteractionDuration(),
                MotorAssistanceConfig.MIN_ATTACK_INTERACTION_DURATION,
                MotorAssistanceConfig.MAX_DURATION,
                draft::setAttackInteractionDuration
        ));
        options.addChild(longOption(
                "attackAssistanceDuration",
                draft.getAttackAssistanceDuration(),
                MotorAssistanceConfig.MIN_DURATION,
                MotorAssistanceConfig.MAX_DURATION,
                draft::setAttackAssistanceDuration
        ));
        options.addChild(doubleOption(
                "attackAimForce",
                draft.getAttackAimForce(),
                MotorAssistanceConfig.MIN_AIM_FORCE,
                MotorAssistanceConfig.MAX_AIM_FORCE,
                draft::setAttackAimForce
        ));
        options.addChild(booleanOption(
                "stopAttackOnReached",
                draft.getStopAttackOnReached(),
                draft::setStopAttackOnReached
        ));

        ScrollableLayout scrollableOptions = new ScrollableLayout(
                minecraft,
                options,
                layout.getContentHeight(),
                ScrollableLayout.ReserveStrategy.RIGHT
        );
        scrollableOptions.setMinWidth(LABEL_WIDTH + CONTROL_WIDTH + 16);
        layout.addToContents(scrollableOptions, settings -> settings.alignHorizontallyCenter());

        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(
                Component.translatableWithFallback("motorassistancemod.configuration.reset", "Reset"),
                button -> minecraft.gui.setScreen(new MotorAssistanceConfigScreen(parent, new MotorAssistanceConfig()))
        ).size(BUTTON_WIDTH, OPTION_HEIGHT).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .size(BUTTON_WIDTH, OPTION_HEIGHT)
                .build());
        doneButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> saveAndClose())
                .size(BUTTON_WIDTH, OPTION_HEIGHT)
                .build());

        layout.visitWidgets(this::addRenderableWidget);
        updateDoneButtonState();
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }

    private LinearLayout booleanOption(String option, boolean initialValue, Consumer<Boolean> setter) {
        Component label = Component.translatable(OPTION_PREFIX + option);
        CycleButton<Boolean> control = CycleButton.onOffBuilder(initialValue)
                .displayOnlyValue()
                .create(0, 0, CONTROL_WIDTH, OPTION_HEIGHT, label, (button, value) -> setter.accept(value));
        control.setTooltip(Tooltip.create(Component.translatable(OPTION_PREFIX + option + ".tooltip")));
        return optionRow(label, control);
    }

    private LinearLayout doubleOption(
            String option,
            double initialValue,
            double min,
            double max,
            Consumer<Double> setter
    ) {
        Component label = Component.translatable(OPTION_PREFIX + option);
        EditBox control = numericControl(label, Double.toString(initialValue));
        control.setResponder(value -> {
            try {
                double parsed = Double.parseDouble(value);
                boolean valid = Double.isFinite(parsed) && parsed >= min && parsed <= max;
                setValidity(
                        control,
                        valid,
                        Component.translatable(OPTION_PREFIX + option + ".tooltip"),
                        min,
                        max
                );
                if (valid) {
                    setter.accept(parsed);
                }
            } catch (NumberFormatException ignored) {
                setValidity(
                        control,
                        false,
                        Component.translatable(OPTION_PREFIX + option + ".tooltip"),
                        min,
                        max
                );
            }
        });
        control.setValue(Double.toString(initialValue));
        return optionRow(label, control);
    }

    private LinearLayout longOption(
            String option,
            long initialValue,
            long min,
            long max,
            Consumer<Long> setter
    ) {
        Component label = Component.translatable(OPTION_PREFIX + option);
        EditBox control = numericControl(label, Long.toString(initialValue));
        control.setResponder(value -> {
            try {
                long parsed = Long.parseLong(value);
                boolean valid = parsed >= min && parsed <= max;
                setValidity(
                        control,
                        valid,
                        Component.translatable(OPTION_PREFIX + option + ".tooltip"),
                        min,
                        max
                );
                if (valid) {
                    setter.accept(parsed);
                }
            } catch (NumberFormatException ignored) {
                setValidity(
                        control,
                        false,
                        Component.translatable(OPTION_PREFIX + option + ".tooltip"),
                        min,
                        max
                );
            }
        });
        control.setValue(Long.toString(initialValue));
        return optionRow(label, control);
    }

    private EditBox numericControl(Component label, String initialValue) {
        EditBox control = new EditBox(font, CONTROL_WIDTH, OPTION_HEIGHT, label);
        control.setMaxLength(20);
        control.setValue(initialValue);
        return control;
    }

    private LinearLayout optionRow(Component label, net.minecraft.client.gui.components.AbstractWidget control) {
        LinearLayout row = LinearLayout.horizontal().spacing(8);
        row.defaultCellSetting().alignVerticallyMiddle();
        row.addChild(new StringWidget(LABEL_WIDTH, OPTION_HEIGHT, label, font));
        row.addChild(control);
        return row;
    }

    private void setValidity(EditBox control, boolean valid, Component description, Number min, Number max) {
        if (valid) {
            invalidFields.remove(control);
            control.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
            control.setTooltip(Tooltip.create(description));
        } else {
            invalidFields.add(control);
            control.setTextColor(INVALID_TEXT_COLOR);
            control.setTooltip(Tooltip.create(Component.literal(
                    "Value must be between " + min + " and " + max + "."
            )));
        }
        updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        if (doneButton != null) {
            doneButton.active = invalidFields.isEmpty();
        }
    }

    private void saveAndClose() {
        if (!invalidFields.isEmpty()) {
            return;
        }
        try {
            TheModConfig.applyAndSave(draft);
            minecraft.gui.setScreen(parent);
        } catch (RuntimeException exception) {
            LOGGER.error("Unable to save Motor Assistance Forge client configuration", exception);
        }
    }

    static MotorAssistanceConfig copy(MotorAssistanceConfig source) {
        MotorAssistanceConfig copy = new MotorAssistanceConfig();
        copy.setShowHudIndicator(source.getShowHudIndicator());
        copy.setFov(source.getFov());
        copy.setAimBlock(source.getAimBlock());
        copy.setBlockRange(source.getBlockRange());
        copy.setMiningInteractionDuration(source.getMiningInteractionDuration());
        copy.setMiningAssistanceDuration(source.getMiningAssistanceDuration());
        copy.setMiningAimForce(source.getMiningAimForce());
        copy.setAimEntity(source.getAimEntity());
        copy.setEntityRange(source.getEntityRange());
        copy.setAttackInteractionSpeed(source.getAttackInteractionSpeed());
        copy.setAttackInteractionDuration(source.getAttackInteractionDuration());
        copy.setAttackAssistanceDuration(source.getAttackAssistanceDuration());
        copy.setAttackAimForce(source.getAttackAimForce());
        copy.setStopAttackOnReached(source.getStopAttackOnReached());
        return copy;
    }
}
