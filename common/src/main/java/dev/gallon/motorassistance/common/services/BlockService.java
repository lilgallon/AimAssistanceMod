package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.domain.Position;
import dev.gallon.motorassistance.common.domain.Target;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlockService implements Target {
    private @NotNull BlockHitResult block;

    public BlockService(@NotNull BlockHitResult block) {
        this.block = block;
    }

    public Position getPosition() {
        return new Position(block.getBlockPos().getX(), block.getBlockPos().getY(), block.getBlockPos().getZ());
    }

    public Position getFacePosition() {
        Position blockCenter = getPosition().plus(0.5);
        return switch (block.getDirection()) {
            case WEST -> blockCenter.plusX(-0.5);
            case EAST -> blockCenter.plusX(0.5);
            case DOWN -> blockCenter.plusY(-0.5);
            case UP -> blockCenter.plusY(+0.5);
            case NORTH -> blockCenter.plusZ(-0.5);
            case SOUTH -> blockCenter.plusZ(0.5);
        };
    }
}
