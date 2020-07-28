package dev.nero.aimassistance.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;

public class Offsets {

    /**
     * It allows the aim assistance to aim the stomach of a zombie instead of its head for example. By default, the aim
     * assistance will aim at the entity's position. The offsets allows it to shift the point that will be aimed.
     *
     * @param entityClass the entity class
     * @return the yaw / pitch offsets for a given entity
     */
    public static float[] getOffsets(Class<? extends Entity> entityClass) {
        if (entityClass == ZombieEntity.class) {
            return new float[] { 0, 10 };
        }

        return new float[] { 0, 0 };
    }
}
