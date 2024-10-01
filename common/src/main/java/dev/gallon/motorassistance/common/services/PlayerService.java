package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.domain.Position;
import dev.gallon.motorassistance.common.domain.Rotation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerService extends EntityService {
    public PlayerService(@NotNull Player entity) {
        super(entity);
    }

    public void setRotation(Rotation rotation) {
        this.entity.setXRot(rotation.pitch());
        this.entity.setYRot(rotation.yaw());
    }

    public boolean canInteract() {
        return this.entity.isPickable();
    }

    public List<EntityService> findMobsAroundPlayer(double range) {
        return entity
                .level()
                .getEntitiesOfClass(
                        Mob.class,
                        new AABB(
                                entity.getX() - range,
                                entity.getY() - range,
                                entity.getZ() - range,
                                entity.getX() + range,
                                entity.getY() + range,
                                entity.getZ() + range
                        )
                )
                .stream()
                .map(EntityService::new)
                .toList();
    }

    public BlockService rayTrace(double reach, Position source, Rotation direction) {
        float f2 = Mth.cos((float) (-direction.yaw() * (Math.PI / 180.0) - Math.PI));
        float f3 = Mth.sin((float) (-direction.yaw() * (Math.PI / 180.0) - Math.PI));
        float f4 = -Mth.cos((float) (-direction.pitch() * (Math.PI / 180.0)));
        float f5 = Mth.sin((float) (-direction.pitch() * (Math.PI / 180.0)));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector = source.toVec3().add(f6 * reach, f5 * reach, f7 * reach);

        return new BlockService(entity.level().clip(
                new ClipContext(
                        source.toVec3(),
                        vector,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        entity
                )
        ));
    }
}
