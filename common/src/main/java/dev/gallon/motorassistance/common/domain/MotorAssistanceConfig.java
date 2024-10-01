package dev.gallon.motorassistance.common.domain;

import org.jetbrains.annotations.NotNull;

public class MotorAssistanceConfig {
    private double fov = 60.0;
    private boolean onlyAssistController = false;
    private boolean aimBlock = true;
    private double blockRange = 7.0;
    private long miningInteractionDuration = 500;
    private long miningAssistanceDuration = 600;
    private double miningAimForce = 7.0;
    private boolean aimEntity = true;
    private double entityRange = 5.0;
    private double attackInteractionSpeed = 0.5;
    private long attackInteractionDuration = 1000;
    private long attackAssistanceDuration = 1100;
    private double attackAimForce = 7.0;
    private boolean stopAttackOnReached = false;

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public boolean getOnlyAssistController() {
        return onlyAssistController;
    }

    public void setOnlyAssistController(boolean onlyAssistController) {
        this.onlyAssistController = onlyAssistController;
    }

    public boolean getAimBlock() {
        return aimBlock;
    }

    public void setAimBlock(boolean aimBlock) {
        this.aimBlock = aimBlock;
    }

    public double getBlockRange() {
        return blockRange;
    }

    public void setBlockRange(double blockRange) {
        this.blockRange = blockRange;
    }

    public long getMiningInteractionDuration() {
        return miningInteractionDuration;
    }

    public void setMiningInteractionDuration(long miningInteractionDuration) {
        this.miningInteractionDuration = miningInteractionDuration;
    }

    public long getMiningAssistanceDuration() {
        return miningAssistanceDuration;
    }

    public void setMiningAssistanceDuration(long miningAssistanceDuration) {
        this.miningAssistanceDuration = miningAssistanceDuration;
    }

    public double getMiningAimForce() {
        return miningAimForce;
    }

    public void setMiningAimForce(double miningAimForce) {
        this.miningAimForce = miningAimForce;
    }

    public boolean getAimEntity() {
        return aimEntity;
    }

    public void setAimEntity(boolean aimEntity) {
        this.aimEntity = aimEntity;
    }

    public double getEntityRange() {
        return entityRange;
    }

    public void setEntityRange(double entityRange) {
        this.entityRange = entityRange;
    }

    public double getAttackInteractionSpeed() {
        return attackInteractionSpeed;
    }

    public void setAttackInteractionSpeed(double attackInteractionSpeed) {
        this.attackInteractionSpeed = attackInteractionSpeed;
    }

    public long getAttackInteractionDuration() {
        return attackInteractionDuration;
    }

    public void setAttackInteractionDuration(long attackInteractionDuration) {
        this.attackInteractionDuration = attackInteractionDuration;
    }

    public long getAttackAssistanceDuration() {
        return attackAssistanceDuration;
    }

    public void setAttackAssistanceDuration(long attackAssistanceDuration) {
        this.attackAssistanceDuration = attackAssistanceDuration;
    }

    public double getAttackAimForce() {
        return attackAimForce;
    }

    public void setAttackAimForce(double attackAimForce) {
        this.attackAimForce = attackAimForce;
    }

    public boolean getStopAttackOnReached() {
        return stopAttackOnReached;
    }

    public void setStopAttackOnReached(boolean stopAttackOnReached) {
        this.stopAttackOnReached = stopAttackOnReached;
    }
}
