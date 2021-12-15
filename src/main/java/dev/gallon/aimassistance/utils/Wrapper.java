package dev.gallon.aimassistance.utils;

import com.mrcrayfish.controllable.Controllable;
import dev.gallon.aimassistance.core.Target;
import dev.gallon.aimassistance.core.TargetType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Wrapper {
    
    public static Minecraft MC = Minecraft.getInstance();
    private static boolean supportForControllable;

    /**
     * @param support if true, it will use the Controllable mod events
     */
    public static void setSupportForControllable(boolean support) {
        supportForControllable = support;
    }

    /**
     * @return true if the player is playing
     */
    public static boolean playerPlaying() {
        return Wrapper.MC.player != null && Wrapper.MC.screen == null;
    }

    /**
     * @return true if the player is pressing the attack key
     */
    public static boolean attackKeyPressed() {
        // could use that as well: GLFW.glfwGetMouseButton(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        boolean attackPressed = Wrapper.MC.options.keyAttack.isDown(); // vanilla
        attackPressed |= supportForControllable && Controllable.getController() != null && Controllable.getController().getRTriggerValue() != 0.0F; // controller
        return attackPressed;
    }

    /**
     * @param maxRange max range to find a block
     * @return the block ray trace result that represents the block that the player is pointing (within the range given)
     */
    public static BlockHitResult getPointedBlock(float maxRange) {
        return switch (MC.hitResult.getType()) {
            case BLOCK -> ((BlockHitResult) MC.hitResult);
            case MISS ->
                    // The block is not within the player's range, so we need to perform a ray trace to find the block the
                    // player is pointing to
                    Wrapper.rayTrace(maxRange);
            default -> null;
        };
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
    public static BlockHitResult rayTrace(double range, Vec3 source, float yaw, float pitch) {
        if (Wrapper.MC.player == null) return null;

        float f2 =  Mth.cos(-yaw   * ((float) Math.PI / 180F) - (float)Math.PI);
        float f3 =  Mth.sin(-yaw   * ((float) Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-pitch * ((float) Math.PI / 180F));
        float f5 =  Mth.sin(-pitch * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector3d1 = source.add((double)f6 * range, (double)f5 * range, (double)f7 * range);

        return Wrapper.MC.level.clip(
                new ClipContext(
                        source,
                        vector3d1,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
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
    private static BlockHitResult rayTrace(double range) {
        return Wrapper.rayTrace(
                range,
                Wrapper.MC.player.getEyePosition(1.0F),
                Wrapper.MC.player.getRotationVector().x, // pitch
                Wrapper.MC.player.getRotationVector().y // yaw
        );
    }

    /**
     * @return true if the player is currently aiming at a mob
     */
    public static boolean isPlayerAimingMob() {
        return Wrapper.MC.crosshairPickEntity instanceof Mob;
    }

    /**
     * @param range in blocks, defines the range around the player to scan for entities
     * @param entityClass the entity type to look for (Check the Entity class: MobEntity.class for mobs for example)
     * @return all the entities that are within the given range from the player
     */
    public static <T extends Entity> List<Entity> getEntitiesAroundPlayer(float range, Class<T> entityClass) {
        AABB area = new AABB(
                Wrapper.MC.player.getX() - range,
                Wrapper.MC.player.getY() - range,
                Wrapper.MC.player.getZ() - range,
                Wrapper.MC.player.getX() + range,
                Wrapper.MC.player.getY() + range,
                Wrapper.MC.player.getZ() + range
        );

        return Wrapper.MC.level.getEntitiesOfClass((Class<Entity>) entityClass, area);
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
            float[] yawPitch = getClosestYawPitchBetween(
                    Wrapper.MC.player, entity
            );

            // Compute the distance from the player's crosshair
            float distYaw = Mth.abs(Mth.wrapDegrees(yawPitch[0] - Wrapper.MC.player.getRotationVector().y));
            float distPitch = Mth.abs(Mth.wrapDegrees(yawPitch[1] - Wrapper.MC.player.getRotationVector().x));
            float dist = Mth.sqrt(distYaw*distYaw + distPitch*distPitch);

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
    public static float[] getClosestYawPitchBetween(Entity source, Entity target) {
        // getPosY returns the ground position
        // getPosY + EyeHeight return the eye's position
        // getPosY + EyeHeight/1.5 returns the upper body position
        //final float SHIFT_FACTOR = 1.25f;

        float[] bestYawPitch = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };

        for (float factor : new float[]{0f, 0.05f, 0.1f, 0.25f, 0.5f, 0.75f, 1.0f}) {
            float[] yawPitch = Wrapper.getYawPitchBetween(
                    // source
                    source.getX(),
                    source.getY() + source.getEyeHeight(),
                    source.getZ(),
                    // target
                    target.getX(),
                    target.getY() + target.getEyeHeight() * factor,
                    target.getZ()
            );

            if (Math.abs(yawPitch[0]) + Math.abs(yawPitch[1]) < Math.abs(bestYawPitch[0]) + Math.abs(bestYawPitch[1])) {
                bestYawPitch = yawPitch;
            }
        }

        return bestYawPitch;
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

        double dist = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));

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
            yawPitch = getClosestYawPitchBetween(
                    Wrapper.MC.player,
                    (Entity) target.getTarget()
            );
        } else {
            yawPitch = getYawPitchBetween(
                    // Player's pos
                    Wrapper.MC.player.getX(),
                    Wrapper.MC.player.getY() + Wrapper.MC.player.getEyeHeight(),
                    Wrapper.MC.player.getZ(),
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
        boolean inFovX = Mth.abs(Mth.wrapDegrees(yaw - MC.player.getRotationVector().y)) <= fovX;
        boolean inFovY = Mth.abs(Mth.wrapDegrees(pitch - MC.player.getRotationVector().x)) <= fovY;

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        if(inFovX && inFovY) {
            float yawFinal, pitchFinal;
            yawFinal = ((Mth.wrapDegrees(yaw - MC.player.getRotationVector().y)) * stepX) / 100;
            pitchFinal = ((Mth.wrapDegrees(pitch - MC.player.getRotationVector().x)) * stepY) / 100;

            return new float[] { MC.player.getRotationVector().y + yawFinal, MC.player.getRotationVector().x + pitchFinal};
        } else {
            return new float[] { MC.player.getRotationVector().y, MC.player.getRotationVector().x};
        }
    }

    /**
     * Sets the position of the crosshair
     * @param yaw horizontal pos (degrees)
     * @param pitch vertical pos (degrees)
     */
    public static void setRotations(float yaw, float pitch) {
        Wrapper.MC.player.setXRot(pitch);
        Wrapper.MC.player.setYRot(yaw);
    }
}
