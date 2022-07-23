package me.ivan1f.villagerhelper.utils;

import me.ivan1f.villagerhelper.VillagerHelperMod;
import net.minecraft.util.Identifier;

public class RegistryUtils {
    public static Identifier id(String name) {
        return new Identifier(VillagerHelperMod.MOD_ID, name);
    }
}
