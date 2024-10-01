package dev.gallon.motorassistance.common.interfaces

interface Minecraft {

    fun getPlayer(): Player?

    /**
     * @return true if the attack key is being pressed
     */
    fun attackKeyPressed(): Boolean

    /**
     * @return true if the player is aiming an entity that is a mob (not a player or an item)
     */
    fun playerAimingMob(): Boolean

    /**
     * @param maxRange the maximum range between the player eyes and the block (unit: 1.0 = 1 block)
     * @return the block the user is looking at if there's one
     */
    fun getPointedBlock(maxRange: Double): Block?
}
