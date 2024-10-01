package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.domain.Position;
import dev.gallon.motorassistance.common.domain.Rotation;
import dev.gallon.motorassistance.common.domain.Target;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityService implements Target {
    protected @NotNull Entity entity;

    public EntityService(@NotNull Entity entity) {
        this.entity = entity;
    }

    Position getEyesPosition() {
        return getPosition().plusY(getEyesHeight());
    }

    double getEyesHeight() {
        return entity.getEyeHeight(entity.getPose());
    }

    Rotation getRotation() {
        return new Rotation(entity.getXRot(), entity.getYRot());
    }

    @Override
    public Position getPosition() {
        return new Position(entity.getX(), entity.getY(), entity.getZ());
    }
}
