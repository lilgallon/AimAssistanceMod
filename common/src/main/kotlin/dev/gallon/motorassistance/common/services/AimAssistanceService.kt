package dev.gallon.motorassistance.common.services

import dev.gallon.motorassistance.common.domain.*
import dev.gallon.motorassistance.common.interfaces.*
import dev.gallon.motorassistance.common.interfaces.Target
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class MotorAssistanceService(
    private val minecraft: Minecraft,
    private val input: Input,
    private val config: MotorAssistanceConfig,
) {
    private var target: Target? = null
    private var interactingWith: TargetType = TargetType.NONE

    private val interactionTimer = Timer() // Used to assist the player for a given amount of time
    private val miningTimer = Timer() // Used to detect that a player is mining
    private val attackTimer = Timer() // Used to detect that a player is attacking

    private var attackCount = 0

    /**
     * This function analyses the player's environment to know what they're aiming at
     */
    fun analyseEnvironment() {
        if (minecraft.getPlayer()?.canInteract() != true) return
        if (config.onlyAssistController && !input.isControllerUsed()) return

        when (interactingWith) {
            TargetType.ENTITY -> minecraft.getPlayer()!!
                .findMobsAroundPlayer(range = config.entityRange)
                .let { entities -> computeClosestEntity(minecraft.getPlayer()!!, entities) }
                ?.also { entity -> target = entity }

            TargetType.BLOCK ->
                minecraft
                    .getPointedBlock(maxRange = config.blockRange)
                    ?.also { block -> target = block }

            TargetType.NONE -> {}
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    fun analyseBehavior() {
        if (minecraft.getPlayer()?.canInteract() != true) return
        if (config.onlyAssistController && !input.isControllerUsed()) return

        // Common
        val attackKeyPressed = minecraft.attackKeyPressed()
        val interactingWithEntity = interactingWith == TargetType.ENTITY

        // Mining
        val playerMiningTimerElapsed = miningTimer.timeElapsed(config.miningInteractionDuration)

        if (miningTimer.stopped() && attackKeyPressed && config.aimBlock && !interactingWithEntity) {
            // If the player wasn't doing anything, and is pressing the attack key (= mining), then start the timer
            miningTimer.start()
        } else if (!miningTimer.stopped() && !playerMiningTimerElapsed && !attackKeyPressed && !interactingWithEntity) {
            // Else (means that the player is mining) - if the player stopped mining during the timer, then stop it
            miningTimer.stop()
        } else if (playerMiningTimerElapsed && attackKeyPressed) {
            // Else (means that the player is mining) - if the timer is elapsed, then the player is mining
            attackTimer.stop()
            miningTimer.stop()
            interactionTimer.start()
            interactingWith = TargetType.BLOCK
        }

        // Attack detection
        val wasLeftClicked = input.wasAttackClicked()

        if (attackCount == 0 && wasLeftClicked && config.aimEntity) {
            attackCount += 1
            attackTimer.start()
        } else if (attackCount > 0 && wasLeftClicked) {
            attackCount += 1

            // Calculate the number of attacks per seconds
            val speed = (attackCount.toDouble() - 1) / config.attackInteractionDuration.toDouble() * 1000

            if (speed > config.attackInteractionSpeed) {
                miningTimer.stop()

                // We need to reset the variables that are used to define if the player is interacting because we know
                // that the user is interacting right now
                attackCount = 0
                attackTimer.stop()
                interactionTimer.start() // it will reset if already started, so we're all good
                interactingWith = TargetType.ENTITY
            }
        } else if (attackTimer.timeElapsed(config.attackInteractionDuration)) {
            this.attackTimer.stop()
            attackCount = 0
        }

        // Common

        // Stop the interaction once that the delay is reached
        val duration = when (interactingWith) {
            TargetType.ENTITY -> config.attackAssistanceDuration
            TargetType.BLOCK -> config.miningAssistanceDuration
            TargetType.NONE -> 0
        }

        if (interactingWith != TargetType.NONE && interactionTimer.timeElapsed(duration)) {
            target = null
            interactingWith = TargetType.NONE
            interactionTimer.stop()
        }
    }

    /**
     * This function will move the player's aim. The faster this function is called, the smoother the aim assistance is.
     */
    fun assistIfPossible() {
        if (minecraft.getPlayer()?.canInteract() != true) return
        if (config.onlyAssistController && !input.isControllerUsed()) return
        if (target == null) return

        if (interactingWith != TargetType.NONE) {
            val aimForce = if (interactingWith === TargetType.BLOCK) config.miningAimForce else config.attackAimForce
            val rotation = computeRotationsNeeded(
                minecraft.getPlayer()!!,
                target!!,
                config.fov,
                config.fov,
                Rotation(aimForce, aimForce),
            )

            // We need to prevent focusing another block while assisting if the player is not moving his mouse
            val assist = if (interactingWith === TargetType.BLOCK && !input.wasMoved()) {
                val nextBlock = minecraft.getPlayer()!!.rayTrace(
                    config.blockRange,
                    minecraft.getPlayer()!!.getEyesPosition(),
                    rotation,
                )

                // If, after moving the mouse, another block is focused, then don't assist
                if (nextBlock != null && target != null) {
                    if (target is Block) {
                        val next = nextBlock.getPosition()
                        val curr = target!!.getPosition()
                        next.x.toInt() == curr.x.toInt() &&
                            next.y.toInt() == curr.y.toInt() &&
                            next.z.toInt() == curr.z.toInt()
                    } else {
                        false
                    }
                } else {
                    true
                }
            } else if (interactingWith == TargetType.ENTITY) {
                // Don't assist if the option to stop when reached is on AND if the player is currently aiming at a mob
                if (config.stopAttackOnReached) {
                    !minecraft.playerAimingMob()
                } else {
                    true
                }
            } else {
                true
            }

            if (assist) minecraft.getPlayer()!!.setRotation(rotation)
        }
    }

    /**
     * @param source
     * @param entities
     * @return the closest entity from the source point of view. Null if the entities list is empty.
     */
    private fun computeClosestEntity(source: Entity, entities: List<Entity>): Entity? = entities
        .map { entity -> entity to computeSmallestRotationBetween(source, entity) }
        .minByOrNull { (_, rotation) ->
            val distYaw = abs(wrapDegrees(rotation.yaw - source.getRotations().yaw))
            val distPitch = abs(wrapDegrees(rotation.pitch - source.getRotations().pitch))

            sqrt(distYaw * distYaw + distPitch * distPitch)
        }
        ?.first

    /**
     * @param source
     * @param target
     * @return minimum rotation required to aim the target from the source point of view.
     */
    private fun computeSmallestRotationBetween(source: Entity, target: Entity): Rotation =
        listOf(0.0, 0.05, 0.1, 0.25, 0.5, 0.75, 1.0)
            .map { factor ->
                computeRotationBetween(
                    source.getEyesPosition(),
                    target.getPosition().run { copy(y = y + target.getEyesHeight() * factor) },
                )
            }
            .minBy { rotation ->
                // pitchToAdd & yawToAdd is the rotation to add to the source to target the given point, we want the
                // closest point from where the source is looking at, so we take the smallest distance that the eyes
                // have to move
                val pitchToAdd = wrapDegrees(rotation.pitch - source.getRotations().pitch)
                val yawToAdd = wrapDegrees(rotation.yaw - source.getRotations().yaw)

                sqrt(pitchToAdd * pitchToAdd + yawToAdd * yawToAdd)
            }

    /**
     * @param source
     * @param target
     * @return rotation required to aim the target from the source point in space
     */
    private fun computeRotationBetween(source: Position, target: Position): Rotation = (target - source)
        .let { diff ->
            val dist = sqrt(diff.x * diff.x + diff.z * diff.z)

            Rotation(
                pitch = -(atan2(diff.y, dist) * 180.0 / Math.PI),
                yaw = (atan2(diff.z, diff.x) * 180.0 / Math.PI) - 90.0,
            )
        }

    /**
     * @param source EntityInstance
     * @param target TargetInstance
     * @param fovX Double
     * @param fovY Double
     * @param step Rotation
     * @return Rotation
     */
    private fun computeRotationsNeeded(
        source: Entity,
        target: Target,
        fovX: Double,
        fovY: Double,
        step: Rotation,
    ): Rotation {
        val rotation = when (target) {
            is Block -> computeRotationBetween(source.getEyesPosition(), target.getFacePosition())
            is Entity -> computeSmallestRotationBetween(source, target)
        }

        // We check if the entity is within the FOV of the player
        // yaw and pitch are MathHelper.absolute, not relative to anything. We fix that by calling wrapDegrees and subtracting
        // the yaw & pitch to the player's rotation. Now, the yaw, and the pitch are relative to the player's view
        // So we can compare that with the given fov: radiusX, and radiusY (which are both in degrees)
        val inFovX = abs(wrapDegrees(rotation.pitch - source.getRotations().pitch)) * step.pitch <= fovX
        val inFovY = abs(wrapDegrees(rotation.yaw - source.getRotations().yaw)) * step.yaw <= fovY

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        return source.getRotations()
            .run {
                if (inFovX && inFovY) {
                    copy(
                        yaw = yaw + ((wrapDegrees(rotation.yaw - yaw)) * step.yaw) / 100,
                        pitch = pitch + ((wrapDegrees(rotation.pitch - pitch)) * step.pitch) / 100,
                    )
                } else {
                    this
                }
            }
    }
}

fun wrapDegrees(degrees: Double): Double = (degrees % 360.0)
    .let { if (it >= 180.0) it - 360 else it }
    .let { if (it < -180.0) it + 360 else it }
