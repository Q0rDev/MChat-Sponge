package ca.q0r.sponge.mchat.commands;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.util.CommandUtil;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.util.VersionUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NonnullByDefault
public class MChatCommand implements CommandCallable {
    public MChatCommand() {
    }

    public Optional<CommandResult> process(CommandSource source, String raw) throws CommandException {
        String[] args = raw.split(" ");

        /*if (!list.contains("mchat")) {
            return true;
        }*/

        if (args.length == 0) {
            return Optional.of(CommandResult.empty());
        }

        if (args[0].equalsIgnoreCase("version")) {
            if (!CommandUtil.hasCommandPerm(source, "mchat.version")) {
                return Optional.of(CommandResult.success());
            }

            String[] vArray = VersionUtil.VERSION.split("-");

            MessageUtil.sendMessage(source, "&6Full Version: &1" + VersionUtil.VERSION);
            MessageUtil.sendMessage(source, "&6MineCraft Version: &2" + vArray[0]);
            MessageUtil.sendMessage(source, "&6Release Version: &2" + vArray[1]);
            MessageUtil.sendMessage(source, "&Git Commit &5#&6:&2 " + (vArray.length < 4 ? vArray[2] : vArray[3]));
            MessageUtil.sendMessage(source, "&6Release: &2" + (vArray.length < 4));

            return Optional.of(CommandResult.success());
        } else if (args[0].equalsIgnoreCase("reload")
                || args[0].equalsIgnoreCase("r")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("config")
                        || args[1].equalsIgnoreCase("co")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.config")) {
                        return Optional.of(CommandResult.success());
                    }

                    ConfigManager.reloadConfig(ConfigType.CONFIG_HOCON);
                    MessageUtil.sendMessage(source, "Config Reloaded.");
                    return Optional.of(CommandResult.success());
                } else if (args[1].equalsIgnoreCase("info")
                        || args[1].equalsIgnoreCase("i")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.info")) {
                        return Optional.of(CommandResult.success());
                    }

                    ConfigManager.reloadConfig(ConfigType.INFO_HOCON);
                    MessageUtil.sendMessage(source, "Info Reloaded.");
                    return Optional.of(CommandResult.success());
                } else if (args[1].equalsIgnoreCase("censor")
                        || args[1].equalsIgnoreCase("ce")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.censor")) {
                        return Optional.of(CommandResult.success());
                    }

                    ConfigManager.reloadConfig(ConfigType.CENSOR_HOCON);
                    MessageUtil.sendMessage(source, "Censor Reloaded.");
                    return Optional.of(CommandResult.success());
                } else if (args[1].equalsIgnoreCase("locale")
                        || args[1].equalsIgnoreCase("l")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.locale")) {
                        return Optional.of(CommandResult.success());
                    }

                    ConfigManager.reloadConfig(ConfigType.LOCALE_HOCON);
                    MessageUtil.sendMessage(source, "Locale Reloaded.");
                    return Optional.of(CommandResult.success());
                } else if (args[1].equalsIgnoreCase("all")
                        || args[1].equalsIgnoreCase("a")) {
                    if (!CommandUtil.hasCommandPerm(source, "mchat.reload.all")) {
                        return Optional.of(CommandResult.success());
                    }

                    ConfigManager.initialize();
                    MessageUtil.sendMessage(source, "All Config's Reloaded.");
                    return Optional.of(CommandResult.success());
                }
            }
        }

        return Optional.of(CommandResult.empty());
    }

    public boolean testPermission(CommandSource commandSource) {
        return true;
    }

    public Optional<Text> getShortDescription(CommandSource commandSource) {
        return Optional.of(Texts.of(new Object[]{"MChat Reload/Version Commands"}));
    }

    public Optional<Text> getHelp(CommandSource commandSource) {
        return Optional.absent();
    }

    public Text getUsage(CommandSource commandSource) {
        return Texts.of("[MChat] Help Screen\n" +
                "- /<command> reload config = Reload Config.\n" +
                "- /<command> reload info = Reload Info.\n" +
                "- /<command> reload censor = Reload Censor.\n" +
                "- /<command> reload locale = Reload Locale.\n" +
                "- /<command> reload all = Reload All Configs.\n" +
                "- /<command> version = Show MChat Version.");
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