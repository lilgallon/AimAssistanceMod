package dev.gallon.motorassistance.common.domain

data class MotorAssistanceConfig(
    // common
    val fov: Double = 60.0,
    val onlyAssistController: Boolean = false,

    // block
    val aimBlock: Boolean = true,
    val blockRange: Double = 7.0,
    val miningInteractionDuration: Long = 500,
    val miningAssistanceDuration: Long = 600,
    val miningAimForce: Double = 7.0,

    // entity
    val aimEntity: Boolean = true,
    val entityRange: Double = 5.0,
    val attackInteractionSpeed: Double = 0.5,
    val attackInteractionDuration: Long = 1000,
    val attackAssistanceDuration: Long = 1100,
    val attackAimForce: Double = 7.0,
    val stopAttackOnReached: Boolean = false
)
