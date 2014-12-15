package ca.q0r.sponge.mchat.config;

import ca.q0r.sponge.mchat.config.censor.CensorConfig;
import ca.q0r.sponge.mchat.config.info.InfoConfig;
import ca.q0r.sponge.mchat.config.locale.LocaleConfig;
import ca.q0r.sponge.mchat.config.main.MainConfig;

/**
 * Used to Manage all HOCON Configs.
 */
public class ConfigManager {
    static CensorConfig censorConfig;
    static MainConfig mainConfig;
    static InfoConfig infoConfig;
    static LocaleConfig localeConfig;

    /**
     * Class Initializer.
     */
    public static void initialize() {
        censorConfig = new CensorConfig();
        censorConfig.loadDefaults();

        mainConfig = new MainConfig();
        mainConfig.loadDefaults();

        infoConfig = new InfoConfig();
        infoConfig.loadDefaults();

        localeConfig = new LocaleConfig();
        localeConfig.loadDefaults();
    }

    /**
     * HOCON retriever.
     *
     * @param type Type of Config to get.
     * @return HOCON Config.
     */
    public static Config getConfig(ConfigType type) {
        switch (type) {
            case CENSOR_HOCON:
                return censorConfig;
            case CONFIG_HOCON:
                return mainConfig;
            case INFO_HOCON:
                return infoConfig;
            case LOCALE_HOCON:
                return localeConfig;
        }

        return null;
    }

    /**
     * HOCON Reloader.
     *
     * @param type Type of Config to reload.
     */
    public static void reloadConfig(ConfigType type) {
        switch (type) {
            case CENSOR_HOCON:
                censorConfig = new CensorConfig();
                censorConfig.loadDefaults();
                break;
            case CONFIG_HOCON:
                mainConfig = new MainConfig();
                mainConfig.loadDefaults();
                break;
            case INFO_HOCON:
                infoConfig = new InfoConfig();
                infoConfig.loadDefaults();
                break;
            case LOCALE_HOCON:
                localeConfig = new LocaleConfig();
                localeConfig.loadDefaults();
                break;
        }
    }

    /**
     * HOCON Unloader. Unloads all configs.
     */
    public static void unload() {
        censorConfig = null;
        mainConfig = null;
        infoConfig = null;
        localeConfig = null;
    }
}