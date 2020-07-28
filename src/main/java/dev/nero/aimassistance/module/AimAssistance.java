package dev.nero.aimassistance.module;

import dev.nero.aimassistance.utils.TimeHelper;
import dev.nero.aimassistance.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * These functions should be called periodically. Check their description for details.
 * - analyseBehaviour()
 * - analyseEnvironment()
 * - assistIfPossible()
 */
public class AimAssistance {

    // Main attributes
    private Target target; // it defines what the assistance will target
    private TargetType interactingWith; // defines what the player is interacting with (can be none)

    // Used to keep track of the player's behaviour
    private TimeHelper interactionTimer = new TimeHelper(); // used to toggle the interaction for a given amount of time
    private TimeHelper miningTimer = new TimeHelper();
    private TimeHelper attackTimer = new TimeHelper(); // used to calculate the attack speed of the player
    private int attackCount = 0; // used to calculate the attack speed of the player
    private boolean attackKeyAlreadyPressed = false; // used to handle key press

    // Behaviour settings
    private final float INTERACTION_ATTACK_SPEED = 1f / 1000f; // (attacks per ms) user faster means user attacking
    private final int INTERACTION_ATTACK_DURATION = 3000; // (ms) duration after which we give up
    private final int INTERACTION_MINING_DURATION = 500; // (ms) duration the player needs to be mining to assist
    private final int INTERACTION_DURATION = 800; // (ms) duration during which the assistance will assist (i'm a poet)
    private final float RANGE_TO_SCAN = 5; // (blocks) range to scan from the player to find entities
    private final Class ENTITY_TYPE_TO_SCAN = MobEntity.class; // defines the type of entity to scan
    private final float BLOCK_REACH = 7; // (blocks) reach to find blocks (lower than default -> ignored)

    // Assistance settings
    private final float FORCE = 5; // force of the assistance
    private final float FOV = 60; // field of view

    /**
     * Inits attributes
     */
    public AimAssistance() {
        this.target = Target.NULL_TARGET;
        this.interactingWith = TargetType.NONE;
    }

    /**
     * This function analyses the player's environment to know what they're aiming at.
     */
    public void analyseEnvironment() {
        // idea: Optimization: only perform the analysis when the player is interacting, and do it once, then start a
        // timer to update the target if needed: (if the player is fighting a lot of mobs, they will still be fighting,
        // but the target won't be the same anymore)

        switch (this.interactingWith) {
            case ENTITY:
                // Get all entities around the player
                List<Entity> entities = Wrapper.getEntitiesAroundPlayer(this.RANGE_TO_SCAN, this.ENTITY_TYPE_TO_SCAN);

                // Get the closest one to the crosshair
                Entity closest = Wrapper.getClosestEntityToCrosshair(entities);

                if (closest != null) {
                    this.target = new Target(closest);
                }

                break;

            case BLOCK:
                // Check what block the player is aiming at
                BlockPos target = Wrapper.getPointedBlock(this.BLOCK_REACH);

                if (target != null) {
                    this.target = new Target(target);
                }

                break;
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    public void analyseBehaviour() {

        // MINING SECTION (Block)

        // If the player wasn't doing anything, and is pressing the attack key (same as mining), then start the timer
        if (this.miningTimer.isStopped() && Wrapper.attackKeyPressed()) {
            this.miningTimer.start();
        }
        // Else (means that the player is mining) if the player stopped mining during the timer, then stop it
        else if (!this.miningTimer.isDelayComplete(this.INTERACTION_MINING_DURATION) && !Wrapper.attackKeyPressed()) {
            this.miningTimer.stop();
        }
        // Else (means that the player is mining) if the player has been mining for the given duration, then they're
        // interacting
        else if (this.miningTimer.isDelayComplete(this.INTERACTION_MINING_DURATION) && Wrapper.attackKeyPressed()) {
            this.miningTimer.stop();
            this.interactionTimer.start(); // it will reset if already started, so we're all good
            this.interactingWith = TargetType.BLOCK;
        }

        // ATTACK SECTION (Entity)

        // Event handling (convert "keyDown" to "isPressed". Minecraft has one built-in but using it may break some
        // code in the backend)
        boolean playerAttacks = false;
        if (this.attackKeyAlreadyPressed && !Wrapper.attackKeyPressed()) {
            this.attackKeyAlreadyPressed = false;
        } else if (!this.attackKeyAlreadyPressed && Wrapper.attackKeyPressed()) {
            playerAttacks = true;
            this.attackKeyAlreadyPressed = true;
        }

        // First time that the player attacks
        if (this.attackCount == 0 && playerAttacks) {
            this.attackCount += 1;
            this.attackTimer.start();
        }
        // If it's not the first time that the player attacked
        else if (this.attackCount > 0 && playerAttacks) {
            this.attackCount += 1;

            // Calculate the number of attacks per miliseconds
            float speed = (float) this.attackCount / (float) this.attackTimer.getTimeElapsed();

            // If player's attack speed is greater than the speed given to toggle the assistance, then we can tell to
            // the instance that the player is interacting
            if (speed > this.INTERACTION_ATTACK_SPEED) {
                // We need to reset the variables that are used to define if the player is interacting because we know
                // that the user is interacting right now
                this.attackCount = 0;
                this.attackTimer.stop();

                this.interactionTimer.start(); // it will reset if already started, so we're all good
                this.interactingWith = TargetType.ENTITY;
            }
        }
        // If the player did not attack for that period of time, we give up and reset everything
        else if (this.attackTimer.isDelayComplete(INTERACTION_ATTACK_DURATION)) {
            this.attackTimer.stop();
            this.attackCount = 0;
        }

        // COMMON SECTION

        // Stop the interaction once that the delay is reached
        if (this.interactingWith != TargetType.NONE
                && this.interactionTimer.isDelayComplete(this.INTERACTION_DURATION)) {
            this.target = Target.NULL_TARGET;
            this.interactingWith = TargetType.NONE;
            this.interactionTimer.stop();
        }
    }

    /**
     * This function will move the player's crosshair. The faster this function is called, the smoother the aim
     * assistance is.
     */
    public void assistIfPossible() {
        // Assist the player by taking into account this.target, only if this.isInteracting is true
        if (this.interactingWith != TargetType.NONE && this.target.getType() != TargetType.NONE) {
            float[] rotations;
            switch (this.target.getType()) {
                case ENTITY:
                    rotations = Wrapper.getRotationsNeeded(
                            (Entity) target.getTarget(),
                            FOV, FOV,
                            FORCE, FORCE
                    );

                    if (rotations != null) {
                        Wrapper.setRotations(rotations[0], rotations[1]);
                    }

                    break;

                case BLOCK:
                    rotations = Wrapper.getRotationsNeeded(
                            (BlockPos) target.getTarget(),
                            FOV, FOV,
                            FORCE, FORCE
                    );

                    if (rotations != null) {
                        Wrapper.setRotations(rotations[0], rotations[1]);
                    }

                    break;
            }
        }
    }
}
