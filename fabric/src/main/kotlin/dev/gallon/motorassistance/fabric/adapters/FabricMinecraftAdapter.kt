package dev.gallon.motorassistance.fabric.adapters

import com.mrcrayfish.controllable.Controllable
import dev.gallon.motorassistance.common.domain.CONTROLLABLE_MOD_ID
import dev.gallon.motorassistance.common.interfaces.Block
import dev.gallon.motorassistance.common.interfaces.Minecraft
import dev.gallon.motorassistance.common.interfaces.Player
import dev.gallon.motorassistance.fabric.utils.whenModLoaded
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.client.Minecraft as FabricMinecraft

class FabricMinecraftAdapter(
    private val minecraft: FabricMinecraft,
) : Minecraft {

    override fun getPlayer(): Player? = minecraft
        .player
        ?.let(::FabricPlayerAdapter)

    override fun attackKeyPressed(): Boolean =
        minecraft.options.keyAttack.isDown ||
            whenModLoaded(CONTROLLABLE_MOD_ID) {
                Controllable.getController()?.run { rTriggerValue > 0.0F }
            } ?: false

    override fun playerAimingMob(): Boolean =
        minecraft.crosshairPickEntity is Mob

    override fun getPointedBlock(maxRange: Double): Block? =
        minecraft
            .hitResult
            ?.let { target ->
                when (target) {
                    is EntityHitResult -> getPlayer()
                        ?.rayTrace(
                            maxRange,
                            target.entity.eyePosition.toPosition(),
                            target.entity.lookAngle.toRotation(),
                        )

                    is BlockHitResult -> FabricBlockAdapter(target)
                    else -> null
                }
            }
}
