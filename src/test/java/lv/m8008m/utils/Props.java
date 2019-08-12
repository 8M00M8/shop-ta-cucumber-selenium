package lv.m8008m.utils;

public class Props {
    public static String getProperty(String key) {
        String sysProp = System.getProperty(key);
        return (sysProp != null) ? sysProp : ConfigProperties.getProperty(key);
    }

    public static String getProperty(String key, String def) {
        String prop = getProperty(key);
        return (prop != null) ? prop : def;
    }
}
