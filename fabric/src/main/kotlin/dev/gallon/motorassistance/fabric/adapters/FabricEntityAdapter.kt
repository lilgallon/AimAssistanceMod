package dev.gallon.motorassistance.fabric.adapters

import dev.gallon.motorassistance.common.domain.Position
import dev.gallon.motorassistance.common.domain.Rotation
import dev.gallon.motorassistance.common.interfaces.Entity
import net.minecraft.world.entity.Entity as FabricEntity

open class FabricEntityAdapter(
    private val entity: FabricEntity
) : Entity {
    override fun getEyesHeight(): Double = with(entity) {
        getEyeHeight(pose).toDouble()
    }

    override fun getRotations(): Rotation = with(entity) {
        Rotation(yaw = yRot.toDouble(), pitch = xRot.toDouble())
    }

    override fun getPosition(): Position = with(entity) {
        Position(x = x, y = y, z = z)
    }
}
