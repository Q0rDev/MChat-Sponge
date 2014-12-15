package ca.q0r.sponge.mchat.config;


import com.typesafe.config.ConfigValueFactory;
import org.spongepowered.api.util.config.ConfigFile;

import java.io.File;

/**
 * Parent class for all HOCON configs.
 */
public abstract class Config {
    /**
     * HOCON Config
     */
    public ConfigFile config;
    /**
     * Config File
     */
    public File file;

    /**
     * Used to instantiate Class.
     *
     * @param file   HOCON Config File to be loaded.
     * @param header Header Comment.
     */
    public Config(File file) {
        this.file = file;

        config = ConfigFile.parseFile(file);

        //TODO Fix Config
        //config.options().indent(4);
        //config.options().header(header);
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
        config = config.withValue(key, ConfigValueFactory.fromAnyRef(obj));
    }

    /**
     * Writes config to disk.
     */
    public void save() {
        config.save(true);
    }

    /**
     * HOCON Config.
     *
     * @return HOCON File loaded from Disk.
     */
    public ConfigFile getConfig() {
        return config;
    }

    /**
     * Check if Option is Set, if not set Value.
     *
     * @param option   Key to check.
     * @param defValue Value to set if Key is not found.
     */
    public void checkOption(String option, Object defValue) {
        if (!config.hasPath(option)) {
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
        if (config.hasPath(oldOption)) {
            set(newOption, config.getAnyRef(oldOption));
            set(oldOption, null);
        }
    }

    /**
     * Remove Key / Value.
     *
     * @param option Key to remove if found.
     */
    public void removeOption(String option) {
        if (config.hasPath(option)) {
            set(option, null);
        }
    }
}