package com.dark.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by darkxue on 16/11/16.
 */
public class PropertiesParser {
    private Properties prop=new Properties();


    public PropertiesParser() {
        String path=Thread.currentThread().getContextClassLoader().getResource("serverConfig.properties").getPath();
        try {
            prop.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertiesParser(Properties prop) {
        this.prop = prop;
    }

    public boolean getBoolean(String key) {
        String value = prop.getProperty(key);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        String value = prop.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }

    public String getString(String key) {
        String value = prop.getProperty(key);
        return value;
    }

    public String getString(String key, String defaultValue) {
        String value = prop.getProperty(key);
        return value == null ? defaultValue : value;
    }

    public int getInt(String key) {
        String value = prop.getProperty(key);
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue) {
        String value = prop.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        String value = prop.getProperty(key);
        return Float.parseFloat(value);
    }

    public float getFloat(String key, float defaultValue) {
        String value = prop.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        String value = prop.getProperty(key);
        return Double.parseDouble(value);
    }

    public double getFloat(String key, double defaultValue) {
        String value = prop.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        String value = prop.getProperty(key);
        return Long.parseLong(value);
    }

    public long getLong(String key, long defaultValue) {
        String value = prop.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
