package dev.gallon.motorassistance.common.domain;

import java.util.Objects;

public class MotorAssistanceConfig {
    /**
     * Kept out of the instance's fields: config GUI libraries (AutoConfig) enumerate every declared
     * field of {@link MotorAssistanceConfig}, including statics, and fail on save when they try to write them.
     */
    public static final class Bounds {
        public static final double MIN_FOV = 0.0;
        public static final double MAX_FOV = 180.0;
        public static final double MIN_RANGE = 0.0;
        public static final double MAX_RANGE = 64.0;
        public static final double MIN_AIM_FORCE = 0.0;
        public static final double MAX_AIM_FORCE = 100.0;
        public static final double MIN_ATTACK_INTERACTION_SPEED = 0.0;
        public static final double MAX_ATTACK_INTERACTION_SPEED = 100.0;
        public static final long MIN_DURATION = 0L;
        public static final long MIN_ATTACK_INTERACTION_DURATION = 1L;
        public static final long MAX_DURATION = 60_000L;

        private Bounds() {
        }
    }

    private static final class Defaults {
        static final Boolean SHOW_HUD_INDICATOR = true;
        static final Double FOV = 60.0;
        static final Boolean AIM_BLOCK = true;
        static final Double BLOCK_RANGE = 7.0;
        static final Long MINING_INTERACTION_DURATION = 500L;
        static final Long MINING_ASSISTANCE_DURATION = 600L;
        static final Double MINING_AIM_FORCE = 7.0;
        static final Boolean AIM_ENTITY = true;
        static final Double ENTITY_RANGE = 5.0;
        static final Double ATTACK_INTERACTION_SPEED = 0.5;
        static final Long ATTACK_INTERACTION_DURATION = 1000L;
        static final Long ATTACK_ASSISTANCE_DURATION = 1100L;
        static final Double ATTACK_AIM_FORCE = 7.0;
        static final Boolean STOP_ATTACK_ON_REACHED = false;
    }

    private Boolean showHudIndicator = Defaults.SHOW_HUD_INDICATOR;
    private Double fov = Defaults.FOV;
    private Boolean aimBlock = Defaults.AIM_BLOCK;
    private Double blockRange = Defaults.BLOCK_RANGE;
    private Long miningInteractionDuration = Defaults.MINING_INTERACTION_DURATION;
    private Long miningAssistanceDuration = Defaults.MINING_ASSISTANCE_DURATION;
    private Double miningAimForce = Defaults.MINING_AIM_FORCE;
    private Boolean aimEntity = Defaults.AIM_ENTITY;
    private Double entityRange = Defaults.ENTITY_RANGE;
    private Double attackInteractionSpeed = Defaults.ATTACK_INTERACTION_SPEED;
    private Long attackInteractionDuration = Defaults.ATTACK_INTERACTION_DURATION;
    private Long attackAssistanceDuration = Defaults.ATTACK_ASSISTANCE_DURATION;
    private Double attackAimForce = Defaults.ATTACK_AIM_FORCE;
    private Boolean stopAttackOnReached = Defaults.STOP_ATTACK_ON_REACHED;

    public boolean getShowHudIndicator() {
        return Objects.requireNonNullElse(showHudIndicator, Defaults.SHOW_HUD_INDICATOR);
    }

    public void setShowHudIndicator(Boolean showHudIndicator) {
        this.showHudIndicator = Objects.requireNonNullElse(showHudIndicator, Defaults.SHOW_HUD_INDICATOR);
    }

    public double getFov() {
        return sanitizeDouble(fov, Defaults.FOV, Bounds.MIN_FOV, Bounds.MAX_FOV);
    }

    public void setFov(Double fov) {
        this.fov = sanitizeDouble(fov, Defaults.FOV, Bounds.MIN_FOV, Bounds.MAX_FOV);
    }

    public boolean getAimBlock() {
        return Objects.requireNonNullElse(aimBlock, Defaults.AIM_BLOCK);
    }

    public void setAimBlock(Boolean aimBlock) {
        this.aimBlock = Objects.requireNonNullElse(aimBlock, Defaults.AIM_BLOCK);
    }

    public double getBlockRange() {
        return sanitizeDouble(blockRange, Defaults.BLOCK_RANGE, Bounds.MIN_RANGE, Bounds.MAX_RANGE);
    }

    public void setBlockRange(Double blockRange) {
        this.blockRange = sanitizeDouble(blockRange, Defaults.BLOCK_RANGE, Bounds.MIN_RANGE, Bounds.MAX_RANGE);
    }

    public long getMiningInteractionDuration() {
        return sanitizeLong(
                miningInteractionDuration,
                Defaults.MINING_INTERACTION_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public void setMiningInteractionDuration(Long miningInteractionDuration) {
        this.miningInteractionDuration = sanitizeLong(
                miningInteractionDuration,
                Defaults.MINING_INTERACTION_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public long getMiningAssistanceDuration() {
        return sanitizeLong(
                miningAssistanceDuration,
                Defaults.MINING_ASSISTANCE_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public void setMiningAssistanceDuration(Long miningAssistanceDuration) {
        this.miningAssistanceDuration = sanitizeLong(
                miningAssistanceDuration,
                Defaults.MINING_ASSISTANCE_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public double getMiningAimForce() {
        return sanitizeDouble(miningAimForce, Defaults.MINING_AIM_FORCE, Bounds.MIN_AIM_FORCE, Bounds.MAX_AIM_FORCE);
    }

    public void setMiningAimForce(Double miningAimForce) {
        this.miningAimForce = sanitizeDouble(
                miningAimForce,
                Defaults.MINING_AIM_FORCE,
                Bounds.MIN_AIM_FORCE,
                Bounds.MAX_AIM_FORCE
        );
    }

    public boolean getAimEntity() {
        return Objects.requireNonNullElse(aimEntity, Defaults.AIM_ENTITY);
    }

    public void setAimEntity(Boolean aimEntity) {
        this.aimEntity = Objects.requireNonNullElse(aimEntity, Defaults.AIM_ENTITY);
    }

    public double getEntityRange() {
        return sanitizeDouble(entityRange, Defaults.ENTITY_RANGE, Bounds.MIN_RANGE, Bounds.MAX_RANGE);
    }

    public void setEntityRange(Double entityRange) {
        this.entityRange = sanitizeDouble(entityRange, Defaults.ENTITY_RANGE, Bounds.MIN_RANGE, Bounds.MAX_RANGE);
    }

    public double getAttackInteractionSpeed() {
        return sanitizeDouble(
                attackInteractionSpeed,
                Defaults.ATTACK_INTERACTION_SPEED,
                Bounds.MIN_ATTACK_INTERACTION_SPEED,
                Bounds.MAX_ATTACK_INTERACTION_SPEED
        );
    }

    public void setAttackInteractionSpeed(Double attackInteractionSpeed) {
        this.attackInteractionSpeed = sanitizeDouble(
                attackInteractionSpeed,
                Defaults.ATTACK_INTERACTION_SPEED,
                Bounds.MIN_ATTACK_INTERACTION_SPEED,
                Bounds.MAX_ATTACK_INTERACTION_SPEED
        );
    }

    public long getAttackInteractionDuration() {
        return sanitizeLong(
                attackInteractionDuration,
                Defaults.ATTACK_INTERACTION_DURATION,
                Bounds.MIN_ATTACK_INTERACTION_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public void setAttackInteractionDuration(Long attackInteractionDuration) {
        this.attackInteractionDuration = sanitizeLong(
                attackInteractionDuration,
                Defaults.ATTACK_INTERACTION_DURATION,
                Bounds.MIN_ATTACK_INTERACTION_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public long getAttackAssistanceDuration() {
        return sanitizeLong(
                attackAssistanceDuration,
                Defaults.ATTACK_ASSISTANCE_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public void setAttackAssistanceDuration(Long attackAssistanceDuration) {
        this.attackAssistanceDuration = sanitizeLong(
                attackAssistanceDuration,
                Defaults.ATTACK_ASSISTANCE_DURATION,
                Bounds.MIN_DURATION,
                Bounds.MAX_DURATION
        );
    }

    public double getAttackAimForce() {
        return sanitizeDouble(attackAimForce, Defaults.ATTACK_AIM_FORCE, Bounds.MIN_AIM_FORCE, Bounds.MAX_AIM_FORCE);
    }

    public void setAttackAimForce(Double attackAimForce) {
        this.attackAimForce = sanitizeDouble(
                attackAimForce,
                Defaults.ATTACK_AIM_FORCE,
                Bounds.MIN_AIM_FORCE,
                Bounds.MAX_AIM_FORCE
        );
    }

    public boolean getStopAttackOnReached() {
        return Objects.requireNonNullElse(stopAttackOnReached, Defaults.STOP_ATTACK_ON_REACHED);
    }

    public void setStopAttackOnReached(Boolean stopAttackOnReached) {
        this.stopAttackOnReached = Objects.requireNonNullElse(
                stopAttackOnReached,
                Defaults.STOP_ATTACK_ON_REACHED
        );
    }

    /**
     * Replaces entries that a config serializer could not read with their defaults.
     */
    public void resetInvalidValues() {
        setShowHudIndicator(showHudIndicator);
        setFov(fov);
        setAimBlock(aimBlock);
        setBlockRange(blockRange);
        setMiningInteractionDuration(miningInteractionDuration);
        setMiningAssistanceDuration(miningAssistanceDuration);
        setMiningAimForce(miningAimForce);
        setAimEntity(aimEntity);
        setEntityRange(entityRange);
        setAttackInteractionSpeed(attackInteractionSpeed);
        setAttackInteractionDuration(attackInteractionDuration);
        setAttackAssistanceDuration(attackAssistanceDuration);
        setAttackAimForce(attackAimForce);
        setStopAttackOnReached(stopAttackOnReached);
    }

    private static double sanitizeDouble(Double value, double defaultValue, double min, double max) {
        if (value == null || !Double.isFinite(value)) {
            return defaultValue;
        }
        return Math.max(min, Math.min(max, value));
    }

    private static long sanitizeLong(Long value, long defaultValue, long min, long max) {
        if (value == null) {
            return defaultValue;
        }
        return Math.max(min, Math.min(max, value));
    }
}
