package dev.nero.aimassistance.utils;

import dev.nero.aimassistance.core.Target;
import dev.nero.aimassistance.core.TargetType;
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
        return Wrapper.MC.player != null && Wrapper.MC.currentScreen == null;
    }

    /**
     * @return true if the player is pressing the attack key
     */
    public static boolean attackKeyPressed() {
        return Wrapper.MC.gameSettings.keyBindAttack.isKeyDown();
    }

    /**
     * @param maxRange max range to find a block
     * @return the block ray trace result that represents the block that the player is pointing (within the range given)
     */
    public static BlockRayTraceResult getPointedBlock(float maxRange) {
        switch (MC.objectMouseOver.getType()) {
            case BLOCK:
                return ((BlockRayTraceResult) MC.objectMouseOver);
            case MISS:
                // The block is not within the player's range, so we need to perform a ray trace to find the block the
                // player is pointing to
                return Wrapper.rayTrace(maxRange);
            default:
                return null;
        }
    }

    /**
     * Performs ray tracing
     *
     * @param range the max range to look for blocks - it's actually the vector's length)
     * @param source the source position - it's actually the vector's position)
     * @param yaw the raw of the unit from that source - it's the vector's x/z (horizontal) direction
     * @param pitch the pitch of the vector from that source - it's the vector's y (vertical) direction
     * @return target.getPos() is instance of BlockPos.Mutable if nothing found, else it's an instance of the thing found.
     */
    public static BlockRayTraceResult rayTrace(double range, Vector3d source, float yaw,  float pitch) {
        if (Wrapper.MC.player == null) return null;

        float f2 = MathHelper.cos(- yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(- yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(- pitch * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(- pitch * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vector3d vector3d1 = source.add((double)f6 * range, (double)f5 * range, (double)f7 * range);

        return Wrapper.MC.world.rayTraceBlocks(
                new RayTraceContext(
                        source,
                        vector3d1,
                        RayTraceContext.BlockMode.OUTLINE,
                        RayTraceContext.FluidMode.NONE,
                        Wrapper.MC.player
                )
        );
    }


    /**
     * Performs ray tracing by taking into account the player's view
     *
     * @param range range to perform ray tracing
     * @return result of ray tracing from the player's view within the range
     */
    private static BlockRayTraceResult rayTrace(double range) {
        return Wrapper.rayTrace(
                range,
                Wrapper.MC.player.getEyePosition(1.0F),
                Wrapper.MC.player.rotationPitch,
                Wrapper.MC.player.rotationYaw
        );
    }

    /**
     * @param range in blocks, defines the range around the player to scan for entities
     * @param entityClass the entity type to look for (Check the Entity class: MobEntity.class for mobs for example)
     * @return all the entities that are within the given range from the player
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
                    Wrapper.MC.player, entity
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
     * @param source the source entity
     * @param target the target of the source entity
     */
    public static float[] getYawPitchBetween(Entity source, Entity target) {
        // getPosY returns the ground position
        // getPosY + EyeHeight return the eye's position
        // getPosY + EyeHeight/1.5 returns the upper body position
        final float SHIFT_FACTOR = 1.25f;

        return Wrapper.getYawPitchBetween(
                // source
                source.getPosX(),
                source.getPosY() + source.getEyeHeight(),
                source.getPosZ(),
                // target
                target.getPosX(),
                target.getPosY() + (target.getEyeHeight() / SHIFT_FACTOR),
                target.getPosZ()
        );
    }

    /**
     * @param sourceX x position for source
     * @param sourceY y position for source
     * @param sourceZ z position for source
     * @param targetX x position for target
     * @param targetY y position for target
     * @param targetZ z position for target
     * @return the [yaw, pitch] difference between the source and the target
     */
    public static float[] getYawPitchBetween(
            double sourceX, double sourceY, double sourceZ,
            double targetX, double targetY, double targetZ) {

        double diffX = targetX - sourceX;
        double diffY = targetY - sourceY;
        double diffZ = targetZ - sourceZ;

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F );
        float pitch = (float) - (Math.atan2(diffY, dist) * 180.0D / Math.PI);

        return new float[] { yaw, pitch };
    }

    /**
     * @param target the target to aim.
     * @return the [x, y] new positions of the player crosshair
     */
    public static float[] getRotationsNeeded(Target target, float fovX, float fovY, float stepX, float stepY) {

        // We calculate the yaw/pitch difference between the target and the player
        float[] yawPitch;
        if (target.getType() == TargetType.ENTITY) {
            yawPitch = getYawPitchBetween(
                    Wrapper.MC.player,
                    (Entity) target.getTarget()
            );
        } else {
            yawPitch = getYawPitchBetween(
                    // Player's pos
                    Wrapper.MC.player.getPosX(),
                    Wrapper.MC.player.getPosY() + Wrapper.MC.player.getEyeHeight(),
                    Wrapper.MC.player.getPosZ(),
                    // Target's pos
                    target.getTargetPosition()[0],
                    target.getTargetPosition()[1],
                    target.getTargetPosition()[2]
            );
        }

        // We make sure that it's absolute, because the sign may change if we invert entity and MC.player
        //float yaw = MathHelper.abs(yawPitch[0]);
        //float pitch = MathHelper.abs(yawPitch[1]);
        float yaw = yawPitch[0];
        float pitch = yawPitch[1];

        // We check if the entity is within the FOV of the player
        // yaw and pitch are absolute, not relative to anything. We fix that by calling wrapDegrees and subtracting
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
