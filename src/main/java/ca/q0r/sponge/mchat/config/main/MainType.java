package ca.q0r.sponge.mchat.config.main;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import com.google.common.base.Functions;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for Different Config Values.
 */
public enum MainType {
    /**
     * Runs MChat in API Only mode.
     */MCHAT_API_ONLY("mchat", "api-only"),
    /**
     * Allows MChat to do an update check and alert Admins of updates.
     */MCHAT_UPDATE_CHECK("mchat", "update-check"),
    /**
     * Whether or not to Alter Events.
     */MCHAT_ALTER_EVENTS("mchat", "alter", "events"),
    /**
     * Whether or not to Alter Tabbed List.
     */MCHAT_ALTER_LIST("mchat", "alter", "list"),
    /**
     * Distance Based Chat Value. Negative or 0 disables.
     */MCHAT_CHAT_DISTANCE("mchat", "chat-distance"),
    /**
     * Variable Indicator. Used in Locale config for Variables hardcoded into MChat or Sponge.
     */MCHAT_VAR_INDICATOR("mchat", "var-indicator"),
    /**
     * Locale Variable Indicator. Used in locale config for Variables used by MChat.
     */MCHAT_LOCALE_VAR_INDICATOR("mchat", "locale-var-indicator"),
    /**
     * Custom Variable Indicator. Used in locale config for Variables added by other Plugins.
     */MCHAT_CUS_VAR_INDICATOR("mchat", "cus-var-indicator"),
    /**
     * Whether or not to censor IP's in Chat.
     */MCHAT_IP_CENSOR("mchat", "ip-censor"),
    /**
     * Range for Auto-Lowercasing. Negative or 0 disables.
     */MCHAT_CAPS_LOCK_RANGE("mchat", "caps-lock-range"),

    /**
     * Whether or not to suppress Join Event Message.
     */SUPPRESS_USE_JOIN("suppress", "use-join"),
    /**
     * Whether or not to suppress Kick Event Message.
     */SUPPRESS_USE_KICK("suppress", "use-kick"),
    /**
     * Whether or not to suppress Quit Event Message.
     */SUPPRESS_USE_QUIT("suppress", "use-quit"),
    /**
     * Disables Join Event Message if Value is lower the the amount of Players currently online.
     */SUPPRESS_MAX_JOIN("suppress", "max-join"),
    /**
     * Disables Kick Event Message if Value is lower the the amount of Players currently online.
     */SUPPRESS_MAX_KICK("suppress", "max-kick"),
    /**
     * Disables Quit Event Message if Value is lower the the amount of Players currently online.
     */SUPPRESS_MAX_QUIT("suppress", "max-quit"),

    /**
     * Whether or not to use "New Info" Resolving.
     */INFO_USE_NEW_INFO("info", "use-new-info"),
    /**
     * Whether or not to use "Leveled Nodes" Resolving.
     */INFO_USE_LEVELED_NODES("info", "use-leveled-nodes"),
    /**
     * Whether or not to use "Old Nodes" Resolving.
     */INFO_USE_OLD_NODES("info", "use-old-nodes"),
    /**
     * Whether or not to Add New Players into <b>info config</b> when they join.
     */INFO_ADD_NEW_PLAYERS("info", "add-new-players"),
    /**
     * Default Group to put Players into.
     */INFO_DEFAULT_GROUP("info", "default-group");

    private final Object[] option;

    private MainType(String... option) {
        this.option = option;
    }

    /**
     * Boolean Value.
     *
     * @return Boolean Value of Config Key.
     */
    public Boolean getBoolean() {
        return ConfigManager.getConfig(ConfigType.CONFIG_HOCON).getConfig().getNode(option).getBoolean();
    }

    /**
     * String Value.
     *
     * @return String Value of Config Key.
     */
    public String getString() {
        return MessageUtil.addColour(ConfigManager.getConfig(ConfigType.CONFIG_HOCON).getConfig().getNode(option).getString());
    }

    /**
     * Integer Value.
     *
     * @return Integer Value of Config Key.
     */
    public Integer getInteger() {
        return ConfigManager.getConfig(ConfigType.CONFIG_HOCON).getConfig().getNode(option).getInt();
    }

    /**
     * Double Value.
     *
     * @return Double Value of Config Key.
     */
    public Double getDouble() {
        return ConfigManager.getConfig(ConfigType.CONFIG_HOCON).getConfig().getNode(option).getDouble();
    }

    /**
     * List Value.
     *
     * @return List Value of Config Key.
     */
    public List<String> getList() {
        List<String> list = ConfigManager.getConfig(ConfigType.CONFIG_HOCON).getConfig().getNode(option).getList(Functions.toStringFunction());
        List<String> l = new ArrayList<String>();

        for (String string : list) {
            l.add(MessageUtil.addColour(string));
        }

        return l;
    }
}