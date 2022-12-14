package tech.justcoding.homburglobby.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Config {
    private static YamlConfiguration configuration;
    private static File file;
    public static void registerConfiguration() {
        File dir = new File("./plugins/Homburg");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir, "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static boolean contains(String path) {
        return configuration.contains(path);
    }
    public static void set(String path, Object value) {
        configuration.set(path, value);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object get(String path) {
        try {
            return configuration.get(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object get(String path, Object normalValue) {
        if (!Config.contains(path)) {
            Config.set(path, normalValue);
        }
        return contains(path);
    }

    public static void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String path) {
        try {
            return configuration.getString(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String path, Object normalValue) {
        if (!Config.contains(path) || !Config.isString(path)) {
            Config.set(path, normalValue);
        }
        return configuration.getString(path);
    }

    public static boolean getBoolean(String path) {
        try {
            return configuration.getBoolean(path);
        } catch (Exception e) {
            return false;
        }
    }

    public static int getInt(String path, Object normalValue) {
        if (!Config.contains(path) || !Config.isInteger(path)) {
            Config.set(path, normalValue);
        }
        return configuration.getInt(path);
    }

    public static int getInt(String path) {
        return configuration.getInt(path);
    }

    public static boolean getBoolean(String path, Object normalValue) {
        if (!Config.contains(path) || !Config.isBoolean(path)) {
            Config.set(path, normalValue);
        }
        try {
            return getBoolean(path);
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    public static List<String> getStringList(String path, Object normalValue) {
        if (!Config.contains(path)) {
            Config.set(path, normalValue);
        }
        return getStringList(path);
    }

    public static boolean isBoolean(String path) {
        return configuration.isBoolean(path);
    }

    public static boolean isString(String path) {
        return configuration.isString(path);
    }

    public static boolean isInteger(String path) {
        return configuration.isInt(path);
    }

    public static List<ConfigurationSection> getSections(String rootSection) {
        Set<String> sections = configuration.getConfigurationSection(rootSection).getKeys(false);
        List<ConfigurationSection> configurationSections = new ArrayList<>();
        for (String key : sections) {
            configurationSections.add(configuration.getConfigurationSection(rootSection + "." + key));
        }
        return configurationSections;
    }

    public static ConfigurationSection getSection(String sectionName) {
        if (configuration.getConfigurationSection(sectionName) == null) {
            return configuration.createSection(sectionName);
        }
        return configuration.getConfigurationSection(sectionName);
    }
}
