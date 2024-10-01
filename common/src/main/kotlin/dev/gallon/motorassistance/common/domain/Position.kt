package dev.gallon.motorassistance.common.domain

data class Position(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    operator fun minus(other: Position): Position =
        Position(
            x = x - other.x,
            y = y - other.y,
            z = z - other.z,
        )

    override fun toString(): String = "$x;$y;$z"
}
