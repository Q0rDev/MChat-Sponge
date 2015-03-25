package ca.q0r.sponge.mchat.events.custom;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Reader;
import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.types.IndicatorType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.variables.VariableManager;
import com.typesafe.config.ConfigValueType;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.event.AbstractEvent;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Event that is fired when Parser is used.
 */
public class ParseEvent extends AbstractEvent {
    private UUID uuid;
    private String world, message, format;

    /**
     * Instantiates Event
     *
     * @param uuid    UUID of player that is executing this event.
     * @param world   World the Player is in.
     * @param message Message being relayed.
     * @param format  Format to be parsed.
     */
    public ParseEvent(UUID uuid, String world, String message, String format) {
        this.uuid = uuid;
        this.world = world;
        this.message = message;
        this.format = format;
    }

    /**
     * Gets UUID of Player.
     *
     * @return UUID of Player.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets Message the player is sending.
     *
     * @return Message the player is sending.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets Message being sent.
     *
     * @param message Message being sent.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets format being used.
     *
     * @return Format being used.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets format to be used.
     *
     * @param format Format being set.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets fully parsed message.
     *
     * @return Fully parsed message.
     */
    public String getParsed() {
        return parse(uuid, world, message, format);
    }

    private String parse(UUID uuid, String world, String message, String format) {
        message = message != null ? message : "";

        format = parseVars(format, uuid, world);

        message = message.replaceAll("%", "%%");

        format = format.replaceAll("%", "%%");

        format = MessageUtil.addColour(format);

        Integer cInt = MainType.MCHAT_CAPS_LOCK_RANGE.getInteger();

        if (!API.checkPermissions(uuid, "mchat.bypass.clock") && cInt > 0) {
            message = fixCaps(message, cInt);
        }

        if (format == null) {
            return message;
        }

        if (API.checkPermissions(uuid, "mchat.coloredchat")) {
            message = MessageUtil.addColour(message);
        }

        if (!API.checkPermissions(uuid, "mchat.censorbypass")) {
            message = replaceCensoredWords(message);
        }

        format = VariableManager.replaceCustVars(uuid, format);
        format = VariableManager.replaceVars(format, uuid, message, true);

        return format;
    }


    private String fixCaps(String format, Integer range) {
        if (range < 1) {
            return format;
        }

        Pattern pattern = Pattern.compile("([A-Z]{" + range + ",300})");
        Matcher matcher = pattern.matcher(format);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group().toLowerCase()));
        }

        matcher.appendTail(sb);

        format = sb.toString();

        return format;
    }

    private String parseVars(String format, UUID uuid, String world) {
        String vI = "\\" + IndicatorType.MISC_VAR.getValue();
        Pattern pattern = Pattern.compile(vI + "<(.*?)>");
        Matcher matcher = pattern.matcher(format);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String info = Reader.getRawInfo(uuid, world, matcher.group(1));
            String var = info != null ? info : "";

            matcher.appendReplacement(sb, Matcher.quoteReplacement(var));
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    private String replaceCensoredWords(String msg) {
        if (MainType.MCHAT_IP_CENSOR.getBoolean()) {
            msg = replacer(msg, "([0-9]{1,3}\\.){3}([0-9]{1,3})", "*.*.*.*");
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : ConfigManager.getConfig(ConfigType.CENSOR_HOCON).getConfig().getChildrenMap().entrySet()) {
            if (entry.getValue().getValue() != ConfigValueType.STRING) {
                continue;
            }

            msg = replacer(msg, "(?i)" + entry.getKey(), entry.getValue().toString());
        }

        return msg;
    }

    private String replacer(String msg, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msg);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);

        msg = sb.toString();

        return msg;
    }
}