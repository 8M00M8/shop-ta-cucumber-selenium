package lv.m8008m.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("config/" + System.getProperty("env") + ".properties")) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            Log.systemLogger.error("", e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String def) {
        return PROPERTIES.getProperty(key, def);
    }
}