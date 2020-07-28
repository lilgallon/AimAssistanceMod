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
     * @return the block pos that the player is pointing at (within the range given)
     */
    public static BlockPos getPointedBlock(float maxRange) {
        switch (MC.objectMouseOver.getType()) {
            case BLOCK:
                return ((BlockRayTraceResult) MC.objectMouseOver).getPos();

            case MISS:
                // The block is not within the player's range, so we need to perform a ray trace to find the block the
                // player is pointing to
                BlockRayTraceResult rayTraceResult = Wrapper.rayTrace(maxRange);
                if (rayTraceResult != null) {
                    return rayTraceResult.getPos();
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
            float[] yawPitch = getYawPitchBetween(
                    entity, Wrapper.MC.player,
                    Offsets.getOffsets(entity.getClass())[0],
                    Offsets.getOffsets(entity.getClass())[1]
            );

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
    public static float[] getYawPitchBetween(Entity entityA, Entity entityB, float offsetX, float offsetY) {
        return Wrapper.getYawPitchBetween(
                entityA.getPosX(), entityA.getPosY(), entityA.getPosZ(), entityA.getEyeHeight(),
                entityB.getPosX(), entityB.getPosY(), entityB.getPosZ(), entityB.getEyeHeight(),
                offsetX, offsetY
        );
    }

    /**
     * @param xA x position for A
     * @param yA y position for A
     * @param zA z position for A
     * @param eA eye position for A (shift on y)
     * @param xB x position for B
     * @param yB y position for B
     * @param zB z position for B
     * @param eB eye position for B (shift on y)
     * @param offsetX final offset X
     * @param offsetY final offset Y
     * @return the [yaw, pitch] difference between the two positions
     */
    public static float[] getYawPitchBetween(
            double xA, double yA, double zA, double eA,
            double xB, double yB, double zB, double eB,
            float offsetX, float offsetY) {

        double diffX = xA - xB;
        double diffZ = zA - zB;
        double diffY = yA + eA - (yB + eB);

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F ) + offsetX;
        float pitch = (float) - (Math.atan2(diffY, dist) * 180.0D / Math.PI) + offsetY;

        return new float[] { yaw, pitch };
    }

    /**
     * @param entity the target to aim.
     * @return the [x, y] new positions of the player crosshair
     */
    public static float[] getRotationsNeeded(Entity entity, float fovX, float fovY, float stepX, float stepY) {
        if (entity == null) {
            return null;
        }

        // We calculate the yaw/pitch difference between the entity and the player
        float[] yawPitch = getYawPitchBetween(
                entity, Wrapper.MC.player,
                Offsets.getOffsets(entity.getClass())[0],
                Offsets.getOffsets(entity.getClass())[1]
        );


        // We make sure that it's absolute, because the sign may change if we invert entity and MC.player
        //float yaw = MathHelper.abs(yawPitch[0]);
        //float pitch = MathHelper.abs(yawPitch[1]);
        float yaw = yawPitch[0];
        float pitch = yawPitch[1];

        // We check if the entity is within the FOV of the player
        // yaw and pitch are absolute, not relative to anything. We fix that by calling wrapDegrees and substracting
        // the yaw & pitch to the player's rotation. Now, the yaw, and the pitch are relative to the player's view
        // So we can compare that with the given fov: radiusX, and radiusY (which are both in degrees)
        boolean inFovX = MathHelper.abs(MathHelper.wrapDegrees(yaw - MC.player.rotationYaw)) <= fovX;
        boolean inFovY = MathHelper.abs(MathHelper.wrapDegrees(pitch - MC.player.rotationPitch)) <= fovY;

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        if(inFovX && inFovY) {
            float yawFinal, pitchFinal;
            yawFinal = ((MathHelper.wrapDegrees(yaw - MC.player.rotationYaw)) * stepX) / 100;
            pitchFinal = ((MathHelper.wrapDegrees(pitch - MC.player.rotationPitch)) * stepY) / 100;

            return new float[] { MC.player.rotationYaw + yawFinal, MC.player.rotationPitch + pitchFinal};
        } else {
            return new float[] { MC.player.rotationYaw, MC.player.rotationPitch};
        }
    }

    /**
     * @param block the block to aim.
     * @return the [x, y] new positions of the player crosshair
     */
    public static float[] getRotationsNeeded(BlockPos block, float fovX, float fovY, float stepX, float stepY) {
        if (block == null) {
            return null;
        }

        // We calculate the yaw/pitch difference between the block and the player
        float[] yawPitch = getYawPitchBetween(
                block.getX(), block.getY(), block.getZ(), 0,
                MC.player.getPosX(), MC.player.getPosY(), MC.player.getPosZ(), MC.player.getEyeHeight(),
                0, 0
        );


        // We make sure that it's absolute, because the sign may change if we invert entity and MC.player
        //float yaw = MathHelper.abs(yawPitch[0]);
        //float pitch = MathHelper.abs(yawPitch[1]);
        float yaw = yawPitch[0];
        float pitch = yawPitch[1];

        // We check if the entity is within the FOV of the player
        // yaw and pitch are absolute, not relative to anything. We fix that by calling wrapDegrees and substracting
        // the yaw & pitch to the player's rotation. Now, the yaw, and the pitch are relative to the player's view
        // So we can compare that with the given fov: radiusX, and radiusY (which are both in degrees)
        boolean inFovX = MathHelper.abs(MathHelper.wrapDegrees(yaw - MC.player.rotationYaw)) <= fovX;
        boolean inFovY = MathHelper.abs(MathHelper.wrapDegrees(pitch - MC.player.rotationPitch)) <= fovY;

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        if(inFovX && inFovY) {
            float yawFinal, pitchFinal;
            yawFinal = ((MathHelper.wrapDegrees(yaw - MC.player.rotationYaw)) * stepX) / 100;
            pitchFinal = ((MathHelper.wrapDegrees(pitch - MC.player.rotationPitch)) * stepY) / 100;

            return new float[] { MC.player.rotationYaw + yawFinal, MC.player.rotationPitch + pitchFinal};
        } else {
            return new float[] { MC.player.rotationYaw, MC.player.rotationPitch};
        }
    }

    /**
     * Sets the position of the crosshair
     * @param yaw horizontal pos (degrees)
     * @param pitch vertical pos (degrees)
     */
    public static void setRotations(float yaw, float pitch) {
        Wrapper.MC.player.rotationYaw = yaw;
        Wrapper.MC.player.rotationPitch = pitch;
    }
}
