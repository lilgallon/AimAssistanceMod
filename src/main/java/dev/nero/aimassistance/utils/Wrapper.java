package dev.nero.aimassistance.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Wrapper {

    public static boolean playerAttacks() {
        return Minecraft.getInstance().gameSettings.keyBindAttack.isPressed();
    }

    public static boolean playerMines() {
        return Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown();
    }

    public static Entity playerTarget() {
        return Minecraft.getInstance().pointedEntity;
    }
}
