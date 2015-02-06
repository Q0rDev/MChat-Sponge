package ca.q0r.sponge.mchat.config;


import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.typesafe.config.ConfigValueFactory;
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
        this.file = new File(ServerUtil.getConfigDir(), name);

        loader = HoconConfigurationLoader.builder().setFile(file).build();

        try {
            config = loader.load();
        } catch (IOException e) {
            MessageUtil.logFormatted("Issues loading: " + file.getName());
            MessageUtil.logFormatted(e.getStackTrace());
        }
    }

    /**
     * Used to load Default Config values.
     */
    public abstract void loadDefaults();

    /**
     * Sets key / value pair to config.
     *
     * @param key Key to be set.
     * @param obj Value to be set.
     */
    public void set(String key, Object obj) {
        config = config.getNode(key).setValue(ConfigValueFactory.fromAnyRef(obj));
    }

    /**
     * Writes config to disk.
     */
    public void save() {
        try {
            loader.save(config);
        } catch (IOException e) {
            MessageUtil.logFormatted("Issues saving: " + file.getName());
            MessageUtil.logFormatted(e.getStackTrace());
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
     * Check if Option is Set, if not set Value.
     *
     * @param option   Key to check.
     * @param defValue Value to set if Key is not found.
     */
    public void checkOption(String option, Object defValue) {
        if (config.getNode(option).isVirtual()) {
            set(option, defValue);
        }
    }

    /**
     * Edit Key.
     *
     * @param oldOption Key to be changed.
     * @param newOption Key to change to if found.
     */
    public void editOption(String oldOption, String newOption) {
        if (!config.getNode(oldOption).isVirtual()) {
            set(newOption, config.getNode(oldOption).getValue());
            set(oldOption, null);
        }
    }

    /**
     * Remove Key / Value.
     *
     * @param option Key to remove if found.
     */
    public void removeOption(String option) {
        if (!config.getNode(option).isVirtual()) {
            set(option, null);
        }
    }
}