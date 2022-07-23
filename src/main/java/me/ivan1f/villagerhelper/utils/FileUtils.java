package me.ivan1f.villagerhelper.utils;

import me.ivan1f.villagerhelper.VillagerHelperMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    private static final String CONFIG_FILE_NAME = VillagerHelperMod.MOD_ID + ".json";

    @SuppressWarnings("deprecation")
    public static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(CONFIG_FILE_NAME).toFile();
    }

    public static void prepareFileDirectories(File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Directory creation failed");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("Directory exists but it's not a directory");
        }
    }
}

