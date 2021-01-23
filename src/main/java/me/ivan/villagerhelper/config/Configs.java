package me.ivan.villagerhelper.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ivan.villagerhelper.VillagerHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Configs {
    private static final File CONFIG_FILE = new File("config/" + VillagerHelper.MOD_ID +".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static boolean ENABLE = false;
    public static double RENDER_DISTANCE = 128.0F;

    public static void readConfigFile() {
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            ENABLE = jsonObject.get("enable").getAsBoolean();
            RENDER_DISTANCE = jsonObject.get("render_distance").getAsDouble();
            VillagerHelper.LOGGER.info("Config file loaded.");
        } catch (Exception e) {
            VillagerHelper.LOGGER.warn("Failed to read config file. Recreating the file...");
            writeConfigFile();
        }
    }

    public static void writeConfigFile() {
        try {
            FileOutputStream outputStream = new FileOutputStream(CONFIG_FILE);
            Map<String, Object> map = new HashMap<>();
            map.put("enable", ENABLE);
            map.put("render_distance", RENDER_DISTANCE);
            outputStream.write(GSON.toJson(map).getBytes());
            VillagerHelper.LOGGER.info("Config file saved.");
        } catch (Exception e) {
            e.printStackTrace();
            VillagerHelper.LOGGER.error("Failed to save config file.");
        }
    }
}
