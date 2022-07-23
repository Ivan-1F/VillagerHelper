package me.ivan1f.villagerhelper.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ivan1f.villagerhelper.VillagerHelperMod;
import me.ivan1f.villagerhelper.utils.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Configs {
    private static final File CONFIG_FILE = FileUtils.getConfigFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static boolean ENABLE = false;
    public static double RENDER_DISTANCE = 128.0F;

    public static void readConfigFile() {
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            ENABLE = jsonObject.get("enable").getAsBoolean();
            RENDER_DISTANCE = jsonObject.get("render_distance").getAsDouble();
        } catch (Exception e) {
            VillagerHelperMod.LOGGER.warn("Failed to read config file. Recreating the file...");
            writeConfigFile();
        }
    }

    public static void writeConfigFile() {
        try {
            FileUtils.prepareFileDirectories(CONFIG_FILE);
            if ((CONFIG_FILE.exists() && CONFIG_FILE.isFile() && CONFIG_FILE.canRead()) || CONFIG_FILE.createNewFile()) {
                FileWriter writer = new FileWriter(CONFIG_FILE);
                Map<String, Object> map = new HashMap<>();
                map.put("enable", ENABLE);
                map.put("render_distance", RENDER_DISTANCE);
                writer.write(GSON.toJson(map));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            VillagerHelperMod.LOGGER.error("Failed to save config file");
        }
    }
}
