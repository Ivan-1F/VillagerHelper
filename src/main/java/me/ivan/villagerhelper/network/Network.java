package me.ivan.villagerhelper.network;

import me.ivan.villagerhelper.VillagerHelper;
import net.minecraft.util.Identifier;

public class Network {
    public static final Identifier CHANNEL = new Identifier(VillagerHelper.MOD_ID);
    public static final Object sync = new Object();
}
