package ca.q0r.sponge.mchat.config;


import ca.q0r.sponge.mchat.util.MessageUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

/**
 * Parent class for all HOCON configs.
 */
public abstract class Config {
    /**
     * HOCON Config
     */
    public ConfigurationLoader loader;
    public ConfigurationNode config;

    /**
     * Config File
     */
    public File file;

    /**
     * Used to instantiate Class.
     *
     * @param name Name of HOCON Config File to be loaded.
     */
    public Config(String name) {
        this.file = new File("plugins/MChat/", name);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                MessageUtil.logFormatted("Issues creating file: " + file.getName());
                e.printStackTrace();
            }
        }

        loader = HoconConfigurationLoader.builder().setFile(file).build();

        try {
            config = loader.load();
        } catch (IOException e) {
            MessageUtil.logFormatted("Issues loading: " + file.getName());
            e.printStackTrace();
        }
    }

    /**
     * Used to load Default Config values.
     */
    public abstract void loadDefaults();

    /**
     * Sets node / value pair to config.
     *
     * @param node Key to be set.
     * @param value Value to be set.
     */
    public void set(Object[] node, Object value) {
        config.getNode(node).setValue(value);
    }

    /**
     * Writes config to disk.
     */
    public void save() {
        try {
            loader.save(config);
        } catch (IOException e) {
            MessageUtil.logFormatted("Issues saving: " + file.getName());
            e.printStackTrace();
        }
    }

    /**
     * HOCON Config.
     *
     * @return HOCON File loaded from Disk.
     */
    public ConfigurationNode getConfig() {
        return config;
    }

    /**
     * Check if Node is Set, if not set Value.
     *
     * @param node   Node to check.
     * @param defValue Value to set if Node is not found.
     */
    public void checkNode(Object[] node, Object defValue) {
        if (config.getNode(node).isVirtual()) {
            set(node, defValue);
        }
    }

    /**
     * Edit Node.
     *
     * @param oldNode Node to be changed.
     * @param newNode Node to change to if found.
     */
    public void editNode(Object[] oldNode, String[] newNode) {
        if (!config.getNode(oldNode).isVirtual()) {
            set(newNode, config.getNode(oldNode).getValue());
            set(oldNode, null);
        }
    }

    /**
     * Remove Node / Value.
     *
     * @param node Node to remove if found.
     */
    public void removeOption(Object[] node) {
        if (!config.getNode(node).isVirtual()) {
            set(node, null);
        }
    }
}