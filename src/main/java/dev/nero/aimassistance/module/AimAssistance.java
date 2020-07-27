package dev.nero.aimassistance.module;

import dev.nero.aimassistance.utils.TimeHelper;
import dev.nero.aimassistance.utils.Wrapper;

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

    // Settings
    private final float INTERACTION_ATTACK_SPEED = 1f / 500f; // (attacks per ms) user faster means user attacking
    private final int INTERACTION_MINING_DURATION = 1000; // (ms) duration the player needs to be mining to assist
    private final int INTERACTION_DURATION = 2000; // (ms) duration during which the assistance will assist (i'm a poet)

    /**
     * Inits attributes
     */
    public AimAssistance() {
        this.target = null;
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
                // TODO

                // Get the closest one to the crosshair
                // TODO

                // If it's in the fov, it's the target
                // TODO
                break;

            case BLOCK:
                // Else, check what block the player is aiming at
                // TODO

                // If no block found, don't do anything
                // TODO
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
        if (this.miningTimer.isStopped() && Wrapper.playerMines()) {
            this.miningTimer.start();
        }
        // Else (means that the player is mining) if the player stopped mining during the timer, then stop it
        else if (!this.miningTimer.isDelayComplete(this.INTERACTION_MINING_DURATION) && !Wrapper.playerMines()) {
            this.miningTimer.stop();
        }
        // Else (means that the player is mining) if the player has been mining for the given duration, then they're
        // interacting
        else if (this.miningTimer.isDelayComplete(this.INTERACTION_MINING_DURATION) && Wrapper.playerMines()) {
            this.miningTimer.stop();
            this.interactingWith = TargetType.BLOCK;
        }

        // ATTACK SECTION (Entity)

        // First time that the player attacks
        if (this.attackCount == 0 && Wrapper.playerAttacks()) {
            this.attackCount += 1;
            this.attackTimer.start();
        }
        // If it's not the first time that the player attacked
        else if (this.attackCount > 0 && Wrapper.playerAttacks()) {
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

                if (this.interactingWith == TargetType.ENTITY) {
                    // If the user is already interacting, then, we can reset the timer to add more time to the
                    // interaction
                    this.interactionTimer.reset();
                } else {
                    // If the user was not interacting, then we start the timer that defines how munch time the user is
                    // interacting
                    this.interactionTimer.start();
                    this.interactingWith = TargetType.ENTITY;
                }

            }
        }

        // COMMON SECTION

        // Stop the interaction once that the delay is reached
        if (this.interactingWith != TargetType.NONE
                && this.interactionTimer.isDelayComplete(this.INTERACTION_DURATION)) {
            this.target = null;
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
        if (this.interactingWith != TargetType.NONE) {
            System.out.println("Aiming!");
        }
    }
}
