package dev.gallon.motorassistance.common.interfaces

interface Input {
    fun wasAttackClicked(): Boolean
    fun wasMoved(): Boolean
    fun isControllerUsed(): Boolean
}
