package dev.nero.aimassistance.module;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;

public class Target {

    /**
     * The type of the target
     */
    private TargetType type;

    /**
     * The target if it's a block
     */
    private Block targetBlock = null;

    /**
     * The target if it's an entity
     */
    private LivingEntity targetEntity = null;

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
    public Target(Block block) {
        this.type = TargetType.BLOCK;
        this.targetBlock = block;
    }

    /**
     * Creates a target
     * @param entity the target
     */
    public Target(LivingEntity entity) {
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

    /**
     * @return the type of that target
     */
    public TargetType getType() {
        return this.type;
    }
}
