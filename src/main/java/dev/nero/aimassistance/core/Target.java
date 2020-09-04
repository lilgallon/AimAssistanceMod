package dev.nero.aimassistance.core;

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
    private final TargetType TYPE;

    /**
     * The target if it's a block
     */
    private BlockRayTraceResult TARGET_BLOCK = null;

    /**
     * The target if it's an entity
     */
    private Entity TARGET_ENTITY = null;

    /**
     * Creates an empty target
     */
    public Target() {
        this.TYPE = TargetType.NONE;
    }

    /**
     * Creates a target
     * @param block the target
     */
    public Target(BlockRayTraceResult block) {
        this.TYPE = TargetType.BLOCK;
        this.TARGET_BLOCK = block;
        this.TARGET_ENTITY = null;
    }

    /**
     * Creates a target
     * @param entity the target
     */
    public Target(Entity entity) {
        this.TYPE = TargetType.ENTITY;
        this.TARGET_BLOCK = null;
        this.TARGET_ENTITY = entity;
    }

    /**
     * @return the target
     */
    public Object getTarget() {
        switch (TYPE) {
            case BLOCK:
                return this.TARGET_BLOCK;
            case ENTITY:
                return this.TARGET_ENTITY;
            default:
                return null;
        }
    }

    public double[] getTargetPosition() {
        switch (this.TYPE) {
            case ENTITY:
                return new double[]{
                        this.TARGET_ENTITY.getPosX(),
                        this.TARGET_ENTITY.getPosY() + this.TARGET_ENTITY.getEyeHeight(),
                        this.TARGET_ENTITY.getPosZ()
                };

            case BLOCK:
                Direction face = this.TARGET_BLOCK.getFace();

                float x = this.TARGET_BLOCK.getPos().getX() + 0.5f;
                float y = this.TARGET_BLOCK.getPos().getY() + 0.5f;
                float z = this.TARGET_BLOCK.getPos().getZ() + 0.5f;

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
        return this.TYPE;
    }
}
