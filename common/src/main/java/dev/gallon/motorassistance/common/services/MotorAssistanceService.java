package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.domain.*;
import dev.gallon.motorassistance.common.domain.Timer;
import dev.gallon.motorassistance.common.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static net.minecraft.util.Mth.atan2;
import static net.minecraft.util.Mth.wrapDegrees;

public class MotorAssistanceService {
    private final @NotNull MotorAssistanceConfig config;
    private final @NotNull InputService inputService;

    private Target target = null;
    private @NotNull TargetType interactingWith = TargetType.NONE;

    // Used to assist the player for a given amount of time
    private final @NotNull Timer interactionTimer = new Timer();
    // Used to detect that a player is mining
    private final @NotNull Timer miningTimer = new Timer();
    // Used to detect that a player is attacking
    private final @NotNull Timer attackTimer = new Timer();

    private long attackCount = 0;

    public MotorAssistanceService(@NotNull MotorAssistanceConfig config, @NotNull InputService inputService) {
        this.config = config;
        this.inputService = inputService;
    }

    /**
     * This function analyses the player's environment to know what they're aiming at
     */
    public void analyseEnvironment() {
        Optional<PlayerService> player = MinecraftService.getPlayer();
        if (player.isEmpty() || !player.get().canInteract()) return;
        // TODO: controller
        // if (config.onlyAssistController && !input.isControllerUsed()) return

        switch (interactingWith) {
            case TargetType.ENTITY -> computeClosestEntity(
                        player.get(),
                        player.get().findMobsAroundPlayer(config.getEntityRange())
                ).ifPresent(entityService -> target = entityService);
            case TargetType.BLOCK -> MinecraftService
                    .getPointedBlock(config.getBlockRange())
                    .ifPresent(pointedBlock -> target = pointedBlock);
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    public void analyseBehavior() {
        Optional<PlayerService> player = MinecraftService.getPlayer();
        if (player.isEmpty() || !player.get().canInteract()) return;
        // TODO: controller
        // if (config.onlyAssistController && !input.isControllerUsed()) return

        // Common
        boolean attackKeyPressed = MinecraftService.attackKeyPressed();
        boolean interactingWithEntity = interactingWith == TargetType.ENTITY;

        // Mining
        boolean playerMiningTimerElapsed = miningTimer.timeElapsed(config.getMiningInteractionDuration());

        if (miningTimer.stopped() && attackKeyPressed && config.getAimBlock() && !interactingWithEntity) {
            // If the player wasn't doing anything, and is pressing the attack key (= mining), then start the timer
            miningTimer.start();
        } else if (!miningTimer.stopped() && !playerMiningTimerElapsed && !attackKeyPressed && !interactingWithEntity) {
            // Else (means that the player is mining) - if the player stopped mining during the timer, then stop it
            miningTimer.stop();
        } else if (playerMiningTimerElapsed && attackKeyPressed) {
            // Else (means that the player is mining) - if the timer is elapsed, then the player is mining
            attackTimer.stop();
            miningTimer.stop();
            interactionTimer.start();
            interactingWith = TargetType.BLOCK;
        }

        // Attack detection
        boolean wasLeftClicked = inputService.wasAttackClicked();
        if (attackCount == 0 && wasLeftClicked && config.getAimEntity()) {
            attackCount += 1;
            attackTimer.start();
        } else if (attackCount > 0 && wasLeftClicked) {
            attackCount += 1;

            // Calculate the number of attacks per seconds
            double speed = (double) (attackCount - 1) / config.getAttackInteractionDuration() * 1000;

            if (speed > config.getAttackInteractionSpeed()) {
                miningTimer.stop();

                // We need to reset the variables that are used to define if the player is interacting because we know
                // that the user is interacting right now
                attackCount = 0;
                attackTimer.stop();
                interactionTimer.start();// it will reset if already started, so we're all good
                interactingWith = TargetType.ENTITY;
            }
        } else if (attackTimer.timeElapsed(config.getAttackInteractionDuration())) {
            this.attackTimer.stop();
            attackCount = 0;
        }

        // Common

        // Stop the interaction once that the delay is reached
        long duration = switch (interactingWith) {
            case TargetType.ENTITY -> config.getAttackAssistanceDuration();
            case TargetType.BLOCK -> config.getMiningAssistanceDuration();
            case TargetType.NONE -> 0;
        };

        if (interactingWith != TargetType.NONE && interactionTimer.timeElapsed(duration)) {
            target = null;
            interactingWith = TargetType.NONE;
            interactionTimer.stop();
        }
    }

    /**
     * This function will move the player's aim. The faster this function is called, the smoother the aim assistance is.
     */
    public void assistIfPossible() {
        Optional<PlayerService> player = MinecraftService.getPlayer();
        if (player.isEmpty() || !player.get().canInteract()) return;
        // TODO: controller
        // if (config.onlyAssistController && !input.isControllerUsed()) return
        if (target == null) return;

        if (interactingWith != TargetType.NONE) {
            float aimForce = (float) (interactingWith == TargetType.BLOCK ?
                                config.getMiningAimForce() : config.getAttackAimForce());
            Rotation rotation = computeRotationsNeeded(
                    player.get(),
                    target,
                    config.getFov(),
                    config.getFov(),
                    new Rotation(aimForce, aimForce)
            );

            // We need to prevent focusing another block while assisting if the player is not moving his mouse
            boolean assist = false;
            if (interactingWith == TargetType.BLOCK && !inputService.wasMoved()) {
                BlockService nextBlock = player.get().rayTrace(
                        config.getBlockRange(),
                        player.get().getEyesPosition(),
                        rotation
                );
                // If, after moving the mouse, another block is focused, then don't assist
                if (nextBlock != null && target != null) {
                    if (target instanceof BlockService) {
                        Position next = nextBlock.getPosition();
                        Position curr = target.getPosition();
                        assist = (int) next.x() == (int) curr.x() &&
                                (int) next.y() == (int) curr.y() &&
                                (int) next.z() == (int) curr.z();
                    }
                } else {
                    assist = true;
                }
            } else if (interactingWith == TargetType.ENTITY) {
                if (config.getStopAttackOnReached()) {
                    assist = !MinecraftService.playerAimingMob();
                } else {
                    assist = true;
                }
            } else {
                assist = true;
            }

            if (assist) {
                player.get().setRotation(rotation);
            }
        }
    }

    private Optional<EntityService> computeClosestEntity(EntityService source, List<EntityService> entities) {
        return entities
                .stream()
                .map(entity -> new AbstractMap.SimpleEntry<>(entity, computeSmallestRotationBetween(source, entity)))
                .min(Comparator.comparingDouble(entry -> {
                    Rotation rotation = entry.getValue();
                    double distYaw = Math.abs(wrapDegrees(rotation.yaw() - source.getRotation().yaw()));
                    double distPitch = Math.abs(wrapDegrees(rotation.pitch() - source.getRotation().pitch()));
                    return Math.sqrt(distYaw * distYaw + distPitch * distPitch);
                }))
                .map(AbstractMap.SimpleEntry::getKey);
    }

    private Rotation computeSmallestRotationBetween(EntityService source, EntityService target) {
        List<Rotation> rotations = Stream.of(0.0, 0.05, 0.1, 0.25, 0.5, 0.75, 1.0)
                .map(factor ->
                        computeRotationBetween(
                                source.getEyesPosition(),
                                target.getPosition().plusY(target.getEyesHeight() * factor)
                        )
                )
                .toList();

        return Utils.minBy(rotations, rotation -> {
            // pitchToAdd & yawToAdd is the rotation to add to the source to target the given point, we want the
            // closest point from where the source is looking at, so we take the smallest distance that the eyes
            // have to move
            float pitchToAdd = wrapDegrees(rotation.pitch() - source.getRotation().pitch());
            float yawToAdd = wrapDegrees(rotation.yaw() - source.getRotation().yaw());

            return sqrt(pitchToAdd * pitchToAdd + yawToAdd * yawToAdd);
        }).orElseGet(() -> new Rotation(0.0f, 0.0f));
    }

    /**
     * @param source source
     * @param target target
     * @return rotation required to aim the target from the source point in space
     */
    private Rotation computeRotationBetween(Position source, Position target) {
        Position diff = target.minus(source);
        double dist = sqrt(diff.x() * diff.x() + diff.z() * diff.z());
        return new Rotation(
                (float) -(atan2(diff.y(), dist) * 180.0 / Math.PI),
                (float) ((float) (atan2(diff.z(), diff.x()) * 180.0 / Math.PI) - 90.0)
        );
    }

    /**
     * @param source EntityInstance
     * @param target TargetInstance
     * @param fovX   Double
     * @param fovY   Double
     * @param step   Rotation
     * @return Rotation
     */
    private Rotation computeRotationsNeeded(
            EntityService source,
            Target target,
            double fovX,
            double fovY,
            Rotation step
    ) {
        Rotation rotation = switch (target) {
            case BlockService b -> computeRotationBetween(source.getEyesPosition(), b.getFacePosition());
            case EntityService e -> computeSmallestRotationBetween(source, e);
            default -> throw new IllegalStateException("Unexpected target: " + target);
        };

        // We check if the entity is within the FOV of the player
        // yaw and pitch are MathHelper.absolute, not relative to anything. We fix that by calling wrapDegrees and subtracting
        // the yaw & pitch to the player's rotation. Now, the yaw, and the pitch are relative to the player's view
        // So we can compare that with the given fov: radiusX, and radiusY (which are both in degrees)
        boolean inFovX = abs(wrapDegrees(rotation.pitch() - source.getRotation().pitch())) * step.pitch() <= fovX;
        boolean inFovY = abs(wrapDegrees(rotation.yaw() - source.getRotation().yaw())) * step.yaw() <= fovY;

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        Rotation currentRotation = source.getRotation();
        Rotation incrementRotation = new Rotation(
                ((wrapDegrees(rotation.pitch() - currentRotation.pitch())) * step.pitch()) / 100,
                ((wrapDegrees(rotation.yaw() - currentRotation.yaw())) * step.yaw()) / 100
        );
        return inFovX && inFovY ? currentRotation.plus(incrementRotation) : currentRotation;
    }
}
