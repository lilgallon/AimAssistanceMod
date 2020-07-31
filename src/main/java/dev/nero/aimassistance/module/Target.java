package dev.nero.aimassistance.module;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;

public class Target {

    /**
     * The null target (type = none)
     */
    public static Target NULL_TARGET = new Target();

    /**
     * The type of the target
     */
    private TargetType type;

    /**
     * The target if it's a block
     */
    private BlockRayTraceResult targetBlock = null;

    /**
     * The target if it's an entity
     */
    private Entity targetEntity = null;

    /**
     * Creates an empty target
     */
    public Target() {
        this.type = TargetType.NONE;
    }

    /**
     * Creates a target
     * @param block the target
     */
    public Target(BlockRayTraceResult block) {
        this.type = TargetType.BLOCK;
        this.targetBlock = block;
    }

    /**
     * Creates a target
     * @param entity the target
     */
    public Target(Entity entity) {
        this.type = TargetType.ENTITY;
        this.targetEntity = entity;
    }

    /**
     * @return the target
     */
    public Object getTarget() {
        switch (type) {
            case BLOCK:
                return this.targetBlock;
            case ENTITY:
                return this.targetEntity;
            default:
                return null;
        }
    }

    public double[] getTargetPosition() {
        switch (this.type) {
            case ENTITY:
                return new double[]{
                        this.targetEntity.getPosX(),
                        this.targetEntity.getPosY() + this.targetEntity.getEyeHeight(),
                        this.targetEntity.getPosZ()
                };

            case BLOCK:
                Direction face = this.targetBlock.getFace();

                float x = this.targetBlock.getPos().getX() + 0.5f;
                float y = this.targetBlock.getPos().getY() + 0.5f;
                float z = this.targetBlock.getPos().getZ() + 0.5f;

                // currently, (x, y, z) represents the middle of the block

                // X ++: east
                // X --: west
                // Y ++: up
                // y --: down
                // Z ++: south
                // Z --: north

                switch (face) {
                    case NORTH:
                        z -= 0.5f;
                        break;
                    case SOUTH:
                        z += 0.5f;
                        break;
                    case EAST:
                        x += 0.5f;
                        break;
                    case WEST:
                        x -= 0.5f;
                        break;
                    case UP:
                        y += 0.5f;
                        break;
                    case DOWN:
                        y -= 0.5f;
                        break;
                }

                return new double[] { x, y, z };
        }

        return null;
    }

    /**
     * @return the type of that target
     */
    public TargetType getType() {
        return this.type;
    }
}
