package dev.gallon.motorassistance.common.domain;

import java.util.Objects;

public class MotorAssistanceConfig {
    private static final Boolean DEFAULT_SHOW_HUD_INDICATOR = true;
    private static final Double DEFAULT_FOV = 60.0;
    private static final Boolean DEFAULT_AIM_BLOCK = true;
    private static final Double DEFAULT_BLOCK_RANGE = 7.0;
    private static final Long DEFAULT_MINING_INTERACTION_DURATION = 500L;
    private static final Long DEFAULT_MINING_ASSISTANCE_DURATION = 600L;
    private static final Double DEFAULT_MINING_AIM_FORCE = 7.0;
    private static final Boolean DEFAULT_AIM_ENTITY = true;
    private static final Double DEFAULT_ENTITY_RANGE = 5.0;
    private static final Double DEFAULT_ATTACK_INTERACTION_SPEED = 0.5;
    private static final Long DEFAULT_ATTACK_INTERACTION_DURATION = 1000L;
    private static final Long DEFAULT_ATTACK_ASSISTANCE_DURATION = 1100L;
    private static final Double DEFAULT_ATTACK_AIM_FORCE = 7.0;
    private static final Boolean DEFAULT_STOP_ATTACK_ON_REACHED = false;

    private Boolean showHudIndicator = DEFAULT_SHOW_HUD_INDICATOR;
    private Double fov = DEFAULT_FOV;
    private Boolean aimBlock = DEFAULT_AIM_BLOCK;
    private Double blockRange = DEFAULT_BLOCK_RANGE;
    private Long miningInteractionDuration = DEFAULT_MINING_INTERACTION_DURATION;
    private Long miningAssistanceDuration = DEFAULT_MINING_ASSISTANCE_DURATION;
    private Double miningAimForce = DEFAULT_MINING_AIM_FORCE;
    private Boolean aimEntity = DEFAULT_AIM_ENTITY;
    private Double entityRange = DEFAULT_ENTITY_RANGE;
    private Double attackInteractionSpeed = DEFAULT_ATTACK_INTERACTION_SPEED;
    private Long attackInteractionDuration = DEFAULT_ATTACK_INTERACTION_DURATION;
    private Long attackAssistanceDuration = DEFAULT_ATTACK_ASSISTANCE_DURATION;
    private Double attackAimForce = DEFAULT_ATTACK_AIM_FORCE;
    private Boolean stopAttackOnReached = DEFAULT_STOP_ATTACK_ON_REACHED;

    public boolean getShowHudIndicator() {
        return Objects.requireNonNullElse(showHudIndicator, DEFAULT_SHOW_HUD_INDICATOR);
    }

    public void setShowHudIndicator(Boolean showHudIndicator) {
        this.showHudIndicator = Objects.requireNonNullElse(showHudIndicator, DEFAULT_SHOW_HUD_INDICATOR);
    }

    public double getFov() {
        return Objects.requireNonNullElse(fov, DEFAULT_FOV);
    }

    public void setFov(Double fov) {
        this.fov = Objects.requireNonNullElse(fov, DEFAULT_FOV);
    }

    public boolean getAimBlock() {
        return Objects.requireNonNullElse(aimBlock, DEFAULT_AIM_BLOCK);
    }

    public void setAimBlock(Boolean aimBlock) {
        this.aimBlock = Objects.requireNonNullElse(aimBlock, DEFAULT_AIM_BLOCK);
    }

    public double getBlockRange() {
        return Objects.requireNonNullElse(blockRange, DEFAULT_BLOCK_RANGE);
    }

    public void setBlockRange(Double blockRange) {
        this.blockRange = Objects.requireNonNullElse(blockRange, DEFAULT_BLOCK_RANGE);
    }

    public long getMiningInteractionDuration() {
        return Objects.requireNonNullElse(miningInteractionDuration, DEFAULT_MINING_INTERACTION_DURATION);
    }

    public void setMiningInteractionDuration(Long miningInteractionDuration) {
        this.miningInteractionDuration = Objects.requireNonNullElse(
                miningInteractionDuration,
                DEFAULT_MINING_INTERACTION_DURATION
        );
    }

    public long getMiningAssistanceDuration() {
        return Objects.requireNonNullElse(miningAssistanceDuration, DEFAULT_MINING_ASSISTANCE_DURATION);
    }

    public void setMiningAssistanceDuration(Long miningAssistanceDuration) {
        this.miningAssistanceDuration = Objects.requireNonNullElse(
                miningAssistanceDuration,
                DEFAULT_MINING_ASSISTANCE_DURATION
        );
    }

    public double getMiningAimForce() {
        return Objects.requireNonNullElse(miningAimForce, DEFAULT_MINING_AIM_FORCE);
    }

    public void setMiningAimForce(Double miningAimForce) {
        this.miningAimForce = Objects.requireNonNullElse(miningAimForce, DEFAULT_MINING_AIM_FORCE);
    }

    public boolean getAimEntity() {
        return Objects.requireNonNullElse(aimEntity, DEFAULT_AIM_ENTITY);
    }

    public void setAimEntity(Boolean aimEntity) {
        this.aimEntity = Objects.requireNonNullElse(aimEntity, DEFAULT_AIM_ENTITY);
    }

    public double getEntityRange() {
        return Objects.requireNonNullElse(entityRange, DEFAULT_ENTITY_RANGE);
    }

    public void setEntityRange(Double entityRange) {
        this.entityRange = Objects.requireNonNullElse(entityRange, DEFAULT_ENTITY_RANGE);
    }

    public double getAttackInteractionSpeed() {
        return Objects.requireNonNullElse(attackInteractionSpeed, DEFAULT_ATTACK_INTERACTION_SPEED);
    }

    public void setAttackInteractionSpeed(Double attackInteractionSpeed) {
        this.attackInteractionSpeed = Objects.requireNonNullElse(
                attackInteractionSpeed,
                DEFAULT_ATTACK_INTERACTION_SPEED
        );
    }

    public long getAttackInteractionDuration() {
        return Objects.requireNonNullElse(attackInteractionDuration, DEFAULT_ATTACK_INTERACTION_DURATION);
    }

    public void setAttackInteractionDuration(Long attackInteractionDuration) {
        this.attackInteractionDuration = Objects.requireNonNullElse(
                attackInteractionDuration,
                DEFAULT_ATTACK_INTERACTION_DURATION
        );
    }

    public long getAttackAssistanceDuration() {
        return Objects.requireNonNullElse(attackAssistanceDuration, DEFAULT_ATTACK_ASSISTANCE_DURATION);
    }

    public void setAttackAssistanceDuration(Long attackAssistanceDuration) {
        this.attackAssistanceDuration = Objects.requireNonNullElse(
                attackAssistanceDuration,
                DEFAULT_ATTACK_ASSISTANCE_DURATION
        );
    }

    public double getAttackAimForce() {
        return Objects.requireNonNullElse(attackAimForce, DEFAULT_ATTACK_AIM_FORCE);
    }

    public void setAttackAimForce(Double attackAimForce) {
        this.attackAimForce = Objects.requireNonNullElse(attackAimForce, DEFAULT_ATTACK_AIM_FORCE);
    }

    public boolean getStopAttackOnReached() {
        return Objects.requireNonNullElse(stopAttackOnReached, DEFAULT_STOP_ATTACK_ON_REACHED);
    }

    public void setStopAttackOnReached(Boolean stopAttackOnReached) {
        this.stopAttackOnReached = Objects.requireNonNullElse(
                stopAttackOnReached,
                DEFAULT_STOP_ATTACK_ON_REACHED
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
}
