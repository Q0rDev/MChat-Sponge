package ca.q0r.sponge.mchat.util;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.config.locale.LocaleType;
import ca.q0r.sponge.mchat.types.IndicatorType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandSource;

import java.util.UUID;

/**
 * Command Utility Class. Used for Command related tasks.
 */
public class CommandUtil {
    /**
     * Command Permission Checker
     *
     * @param sender     Object Sending Command.
     * @param permission Permissions attached to command.
     * @return Result of Permission check.
     */
    public static Boolean hasCommandPerm(CommandSource sender, String permission) {
        if (!API.checkPermissions(sender, permission)) {
            MessageUtil.sendMessage(sender, API.replace(LocaleType.MESSAGE_NO_PERMS.getVal(), "permission", permission, IndicatorType.LOCALE_VAR));
            return false;
        }

        return true;
    }

    /**
     * Checks if player is online before sending command.
     *
     * @param sender Object sending command.
     * @param player Player to check for.
     * @return Result of Online Check.
     */
    @Deprecated
    public static Boolean isOnlineForCommand(CommandSource sender, String player) {
        if (ServerUtil.getPlayer(player) == null) {
            MessageUtil.sendMessage(sender, "&4Player &e'" + player + "'&4 not Found.");
            return false;
        }

        return true;
    }

    /**
     * Checks if player is online before sending command.
     *
     * @param sender Object sending command.
     * @param uuid   UUID to check for.
     * @return Result of Online Check.
     */
    public static Boolean isOnlineForCommand(CommandSource sender, UUID uuid) {
        if (ServerUtil.getPlayer(uuid.toString()) == null) {
            MessageUtil.sendMessage(sender, "&4Player &e'" + uuid + "'&4 not Found.");
            return false;
        }

        return true;
    }

    /**
     * Checks if player is online before sending command.
     *
     * @param sender Object sending command.
     * @param player Player to check for.
     * @return Result of Online Check.
     */
    public static Boolean isOnlineForCommand(CommandSource sender, Player player) {
        if (player == null) {
            MessageUtil.sendMessage(sender, "&4Player not Found.");
            return false;
        }

        return true;
    }
}