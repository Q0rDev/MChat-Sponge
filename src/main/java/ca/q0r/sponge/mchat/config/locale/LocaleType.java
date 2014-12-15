package ca.q0r.sponge.mchat.config.locale;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.util.MessageUtil;

/**
 * Enum for Different Locale Values.
 */
public enum LocaleType {
    /**
     * Forward Format
     */FORMAT_FORWARD("format.forward"),
    /**
     * Local Format
     */FORMAT_LOCAL("format.local"),
    /**
     * Spy Format
     */FORMAT_SPY("format.spy"),
    /**
     * Chat Format
     */FORMAT_CHAT("format.chat"),
    /**
     * Date Format
     */FORMAT_DATE("format.date"),
    /**
     * Name Format
     */FORMAT_NAME("format.name"),
    /**
     * Tabbed List Format
     */FORMAT_CUSTOM_NAME("format.custom-name"),
    /**
     * List Command Format
     */FORMAT_LIST_CMD("format.list-cmd"),
    /**
     * Me Format
     */FORMAT_ME("format.me"),

    /**
     * Info Alteration Message
     */MESSAGE_INFO_ALTERATION("message.info.alteration"),
    /**
     * No Permissions Message
     */MESSAGE_NO_PERMS("message.general.no-perms"),
    /**
     * Join Event Message
     */MESSAGE_EVENT_JOIN("message.event.join"),
    /**
     * Leave Event Message
     */MESSAGE_EVENT_LEAVE("message.event.leave"),
    /**
     * Kick Event Message
     */MESSAGE_EVENT_KICK("message.event.kick"),
    /**
     * Heroes Mastered Message
     */MESSAGE_HEROES_TRUE("message.heroes.is-master"),
    /**
     * Heroes Not Mastered Message
     */MESSAGE_HEROES_FALSE("message.heroes.not-master");

    private final String option;

    private LocaleType(String option) {
        this.option = option;
    }

    /**
     * Value Retriever.
     *
     * @return Retrieves Value and and Colours it.
     */
    public String getVal() {
        return MessageUtil.addColour(getRaw());
    }

    /**
     * Value Retriever.
     *
     * @return Retrieves Raw Value.
     */
    public String getRaw() {
        if (ConfigManager.getConfig(ConfigType.LOCALE_HOCON).getConfig().hasPath(option)) {
            return ConfigManager.getConfig(ConfigType.LOCALE_HOCON).getConfig().getString(option);
        }

        return "Locale Option '" + option + "' not found!";
    }
}