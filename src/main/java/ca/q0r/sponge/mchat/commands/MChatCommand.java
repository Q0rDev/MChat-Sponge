package ca.q0r.sponge.mchat.commands;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.util.CommandUtil;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.util.VersionUtil;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NonnullByDefault
public class MChatCommand implements CommandCallable {
    public MChatCommand() {
    }

    public boolean call(CommandSource source, String raw, List<String> list) throws CommandException {
        String[] args = raw.split(" ");

        if (!list.contains("mchat")) {
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("version")) {
            if (!CommandUtil.hasCommandPerm(source, "mchat.version")) {
                return true;
            }

            String[] vArray = VersionUtil.VERSION.split("-");

            MessageUtil.sendMessage(source, "&6Full Version: &1" + VersionUtil.VERSION);
            MessageUtil.sendMessage(source, "&6MineCraft Version: &2" + vArray[0]);
            MessageUtil.sendMessage(source, "&6Release Version: &2" + vArray[1]);
            MessageUtil.sendMessage(source, "&Git Commit &5#&6:&2 " + (vArray.length < 4 ? vArray[2] : vArray[3]));
            MessageUtil.sendMessage(source, "&6Release: &2" + (vArray.length < 4));

            return true;
        } else if (args[0].equalsIgnoreCase("reload")
                || args[0].equalsIgnoreCase("r")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("config")
                        || args[1].equalsIgnoreCase("co")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.config")) {
                        return true;
                    }

                    ConfigManager.reloadConfig(ConfigType.CONFIG_HOCON);
                    MessageUtil.sendMessage(source, "Config Reloaded.");
                    return true;
                } else if (args[1].equalsIgnoreCase("info")
                        || args[1].equalsIgnoreCase("i")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.info")) {
                        return true;
                    }

                    ConfigManager.reloadConfig(ConfigType.INFO_HOCON);
                    MessageUtil.sendMessage(source, "Info Reloaded.");
                    return true;
                } else if (args[1].equalsIgnoreCase("censor")
                        || args[1].equalsIgnoreCase("ce")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.censor")) {
                        return true;
                    }

                    ConfigManager.reloadConfig(ConfigType.CENSOR_HOCON);
                    MessageUtil.sendMessage(source, "Censor Reloaded.");
                    return true;
                } else if (args[1].equalsIgnoreCase("locale")
                        || args[1].equalsIgnoreCase("l")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.locale")) {
                        return true;
                    }

                    ConfigManager.reloadConfig(ConfigType.LOCALE_HOCON);
                    MessageUtil.sendMessage(source, "Locale Reloaded.");
                    return true;
                } else if (args[1].equalsIgnoreCase("all")
                        || args[1].equalsIgnoreCase("a")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.all")) {
                        return true;
                    }

                    ConfigManager.initialize();
                    MessageUtil.sendMessage(source, "All Config's Reloaded.");
                    return true;
                }
            }
        }

        return false;
    }

    public boolean testPermission(CommandSource commandSource) {
        return true;
    }

    public String getShortDescription(CommandSource commandSource) {
        return "MChat Reload/Version Commands";
    }

    public Text getHelp(CommandSource commandSource) {
        return Texts.of();
    }

    public String getUsage(CommandSource commandSource) {
        return "[MChat] Help Screen\n" +
                "- /<command> reload config = Reload Config.\n" +
                "- /<command> reload info = Reload Info.\n" +
                "- /<command> reload censor = Reload Censor.\n" +
                "- /<command> reload locale = Reload Locale.\n" +
                "- /<command> reload all = Reload All Configs.\n" +
                "- /<command> version = Show MChat Version.";
    }

    public List<String> getSuggestions(CommandSource source, String raw) throws CommandException {
        String[] args = raw.split(" ");

        if (args.length == 1) {
            return Arrays.asList("version", "reload");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r"))) {
            return Arrays.asList("config", "censor", "info", "locale", "all");
        }

        return Collections.emptyList();
    }
}