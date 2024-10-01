package dev.gallon.motorassistance.common.domain;

import net.minecraft.world.phys.Vec3;

public record Position(double x, double y, double z) {

    public static Position fromVec3(Vec3 vec3) {
        return new Position(vec3.x, vec3.y, vec3.z);
    }

    public Position minus(Position other) {
        return new Position(x - other.x, y - other.y, z - other.z);
    }

    public Position plus(double amount) {
        return new Position(x + amount, y + amount, z + amount);
    }

    public Position plusX(double amount) {
        return new Position(x + amount, y, z);
    }

    public Position plusY(double amount) {
        return new Position(x, y + amount, z);
    }

    public Position plusZ(double amount) {
        return new Position(x, y, z + amount);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    @Override
    public String toString() {
        return x + ";" + y + ";" + z;
    }
}
