package dev.gallon.motorassistance.fabric.adapters

import dev.gallon.motorassistance.common.domain.Position
import dev.gallon.motorassistance.common.domain.Rotation
import dev.gallon.motorassistance.common.interfaces.Block
import dev.gallon.motorassistance.common.interfaces.Entity
import dev.gallon.motorassistance.common.interfaces.Player
import net.minecraft.client.player.LocalPlayer
import net.minecraft.util.Mth
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.AABB

class FabricPlayerAdapter(
    private val player: LocalPlayer,
) : FabricEntityAdapter(player), Player {

    override fun setRotation(rotations: Rotation) {
        player.xRot = rotations.pitch.toFloat()
        player.yRot = rotations.yaw.toFloat()
    }

    override fun canInteract(): Boolean = player.isPickable

    override fun findMobsAroundPlayer(range: Double): List<Entity> =
        player
            .level()
            .getEntitiesOfClass(
                Mob::class.java,
                AABB(
                    player.x - range,
                    player.y - range,
                    player.z - range,
                    player.x + range,
                    player.y + range,
                    player.z + range,
                ),
            ) { true }
            .map(::FabricEntityAdapter)

    override fun rayTrace(reach: Double, source: Position, direction: Rotation): Block {
        val f2 = Mth.cos((-direction.yaw * (Math.PI / 180.0) - Math.PI).toFloat())
        val f3 = Mth.sin((-direction.yaw * (Math.PI / 180.0) - Math.PI).toFloat())
        val f4 = -Mth.cos((-direction.pitch * (Math.PI / 180.0)).toFloat())
        val f5 = Mth.sin((-direction.pitch * (Math.PI / 180.0)).toFloat())
        val f6 = f3 * f4
        val f7 = f2 * f4
        val vector = source.toVec3d().add(f6 * reach, f5 * reach, f7 * reach)

        return player
            .level()
            .clip(
                ClipContext(
                    source.toVec3d(),
                    vector,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player,
                ),
            )
            .let(::FabricBlockAdapter)
    }
}
