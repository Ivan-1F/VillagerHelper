package me.ivan1f.villagerhelper;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VillagerHelperMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_NAME = "Villager Helper";
    public static final String MOD_ID = "villagerhelper";
    public static String VERSION;

    @Override
    public void onInitialize() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
    }
}
