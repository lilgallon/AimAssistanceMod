package dev.gallon.motorassistance.common.domain;

import net.minecraft.world.phys.Vec3;

public record Rotation(float pitch, float yaw) {
    public static Rotation fromVec3(Vec3 vec3) {
        return new Rotation((float) vec3.x, (float) vec3.y);
    }

    public Rotation plus(Rotation other) {
        return new Rotation(this.pitch + other.pitch, this.yaw + other.yaw);
    }

    @Override
    public String toString() {
        return "pitch=" + pitch + " yaw=" + yaw;
    }
}
