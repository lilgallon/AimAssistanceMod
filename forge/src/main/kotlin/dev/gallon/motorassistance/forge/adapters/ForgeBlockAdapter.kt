package dev.gallon.motorassistance.forge.adapters

import dev.gallon.motorassistance.common.domain.Position
import dev.gallon.motorassistance.common.interfaces.Block
import net.minecraft.core.Direction
import net.minecraft.world.phys.BlockHitResult

class ForgeBlockAdapter(
    private val block: BlockHitResult,
) : Block {
    override fun getFacePosition(): Position = getPosition()
        .run {
            copy(
                x = x.toInt() + 0.5,
                y = y.toInt() + 0.5,
                z = z.toInt() + 0.5,
            )
        }
        .run {
            when (block.direction) {
                Direction.WEST -> copy(x = x - 0.5)
                Direction.EAST -> copy(x = x + 0.5)
                Direction.NORTH -> copy(z = z - 0.5)
                Direction.SOUTH -> copy(z = z + 0.5)
                Direction.DOWN -> copy(y = y - 0.5)
                Direction.UP -> copy(y = y + 0.5)
                else -> this
            }
        }

    override fun getPosition(): Position = with(block.blockPos) {
        Position(x = x.toDouble(), y = y.toDouble(), z = z.toDouble())
    }
}
