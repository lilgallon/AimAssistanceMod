package dev.nero.aimassistance.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class Wrapper {
    
    public static Minecraft MC = Minecraft.getInstance();

    /**
     * @return true if the player is playing
     */
    public static boolean playerPlaying() {
        return Wrapper.MC.player != null;
    }

    /**
     * @return true if the player is pressing the attack key
     */
    public static boolean attackKeyPressed() {
        return Wrapper.MC.gameSettings.keyBindAttack.isKeyDown();
    }

    /**
     * @return the entity that the player is pointing at (null if none)
     */
    public static Entity pointedEntity() {
        return Wrapper.MC.pointedEntity;
    }

    /**
     * @param maxRange max range to find a block
     * @return the block that the player is pointing at (within the range given)
     */
    public static Block getPointedBlock(float maxRange) {
        switch (MC.objectMouseOver.getType()) {
            case BLOCK:
                BlockPos blockPos = ((BlockRayTraceResult) MC.objectMouseOver).getPos();
                return MC.world.getBlockState(blockPos).getBlock();

            case MISS:
                // The block is not within the player's range, so we need to perform a ray trace to find the block the
                // player is pointing to
                BlockRayTraceResult rayTraceResult = Wrapper.rayTrace(maxRange);
                if (rayTraceResult != null) {
                    return MC.world.getBlockState(rayTraceResult.getPos()).getBlock();
                } else {
                    return null;
                }

            default:
                return null;
        }
    }

    /**
     * Copied from Item#rayTrace, and updated
     *
     * @param range range to perform ray tracing
     * @return result of ray tracing from the player's view within the range
     */
    private static BlockRayTraceResult rayTrace(double range) {
        if (Wrapper.MC.player == null) return null;

        float f = Wrapper.MC.player.rotationPitch;
        float f1 = Wrapper.MC.player.rotationYaw;
        Vector3d vector3d = Wrapper.MC.player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = range;
        Vector3d vector3d1 = vector3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);

        return Wrapper.MC.world.rayTraceBlocks(
                new RayTraceContext(
                        vector3d,
                        vector3d1,
                        RayTraceContext.BlockMode.OUTLINE,
                        RayTraceContext.FluidMode.ANY,
                        Wrapper.MC.player
                )
        );
    }

    /**
     * @param range in blocks, defines the range around the player to scan for entities
     * @param entityClass the entity type to look for (Check the Entity class: MobEntity.class for mobs for example)
     * @return
     */
    public static List<Entity> getEntitiesAroundPlayer(float range, Class<? extends Entity> entityClass) {
        AxisAlignedBB area = new AxisAlignedBB(
                Wrapper.MC.player.getPosX() - range,
                Wrapper.MC.player.getPosY() - range,
                Wrapper.MC.player.getPosZ() - range,
                Wrapper.MC.player.getPosX() + range,
                Wrapper.MC.player.getPosY() + range,
                Wrapper.MC.player.getPosZ() + range
        );

        return Wrapper.MC.world.getEntitiesWithinAABB(entityClass, area);
    }

    /**
     * @param entities list of entities to scan
     * @return the closest entity from the list from the player's crosshair
     */
    public static Entity getClosestEntityToCrosshair(List<Entity> entities) {
        float minDist = Float.MAX_VALUE;
        Entity closest = null;

        for(Entity entity : entities){
            // Get distance between the two entities (rotations)
            float[] yawPitch = getYawPitchBetween(entity, Wrapper.MC.player);

            // Compute the distance from the player's crosshair
            float distYaw = MathHelper.abs(MathHelper.wrapDegrees(yawPitch[0] - Wrapper.MC.player.rotationYaw));
            float distPitch = MathHelper.abs(MathHelper.wrapDegrees(yawPitch[1] - Wrapper.MC.player.rotationPitch));
            float dist = MathHelper.sqrt(distYaw*distYaw + distPitch*distPitch);

            // Get the closest entity
            if(dist < minDist) {
                closest = entity;
                minDist = dist;
            }
        }

        return closest;
    }

    /**
     * @param entityA an entity
     * @param entityB an other one
     * @return the [yaw, pitch] difference between the two entities
     */
    public static float[] getYawPitchBetween(Entity entityA, Entity entityB) {
        double diffX = entityA.getPosX() - entityB.getPosX();
        double diffZ = entityA.getPosZ() - entityB.getPosZ();
        double diffY = entityA.getPosY() + entityA.getEyeHeight() - (entityB.getPosY() + entityB.getEyeHeight());

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F );
        float pitch = (float) - (Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[] { yaw, pitch };
    }
}
