package ca.q0r.sponge.mchat.commands;

import ca.q0r.sponge.mchat.api.Writer;
import ca.q0r.sponge.mchat.config.locale.LocaleType;
import ca.q0r.sponge.mchat.types.InfoEditType;
import ca.q0r.sponge.mchat.types.InfoType;
import ca.q0r.sponge.mchat.util.CommandUtil;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NonnullByDefault
public class InfoAlterCommand implements CommandCallable {
    private InfoType type;
    private String cmd;

    public InfoAlterCommand(String command, InfoType infoType) {
        type = infoType;
        cmd = command;
    }

    public Optional<CommandResult> process(CommandSource source, String raw) throws CommandException {
        String[] args = raw.split(" ");

        /*if (!command.getName().equalsIgnoreCase(cmd)) {
            return Optional.of(CommandResult.success());
        }*/

        if (args.length == 0) {
            MessageUtil.sendMessage(source, "Use '/" + cmd + " add/edit/remove' for more info.");
            return Optional.of(CommandResult.success());
        }

        InfoEditType editType;

        String alterType = "user";
        String typeName = "player";
        String textName = "Player";
        String uuid = "";

        if (type == InfoType.GROUP) {
            alterType = "group";
            typeName = "group";
            textName = "Group";
        }

        if (args.length > 2) {
            if (type == InfoType.GROUP) {
                uuid = args[2];
            } else {
                Player player = ServerUtil.getPlayer(args[2]);

                if (player == null) {
                    MessageUtil.sendMessage(source, "Player '" + args[2] + "' is offline for this command.");
                    MessageUtil.sendMessage(source, "Until Proper API's have been released Players will have to be online.");
                    return Optional.of(CommandResult.success());
                } else {
                    uuid = player.getUniqueId().toString();
                }
            }
        }

        if (args[0].equalsIgnoreCase("a")
                || args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/" + cmd + " add':\n" +
                        "    - /" + cmd + " add " + typeName + " <" + textName + ">\n" +
                        "    - /" + cmd + " add ivar <" + textName + "> <Variable> [Value]\n" +
                        "    - /" + cmd + " add world <" + textName + "> <World>\n" +
                        "    - /" + cmd + " add wvar <" + textName + "> <World> <Variable> [Value]");
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase(typeName.substring(0, 1))
                    || args[1].equalsIgnoreCase(typeName)) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".add." + typeName)) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.ADD_BASE;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.addBase(uuid, type);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("iVar")
                    || args[1].equalsIgnoreCase("infoVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".add.ivar")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.ADD_INFO_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.setInfoVar(uuid, type, args[3], combineArgs(args, 4));
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("w")
                    || args[1].equalsIgnoreCase("world")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".add.world")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.ADD_WORLD;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.addWorld(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("wVar")
                    || args[1].equalsIgnoreCase("worldVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".add.wvar")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.ADD_WORLD_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.setWorldVar(uuid, type, args[3], args[4], combineArgs(args, 5));
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            }
        } else if (args[0].equalsIgnoreCase("r")
                || args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/" + cmd + " remove':\n" +
                        "    - /" + cmd + " remove " + typeName + " <" + textName + ">\n" +
                        "    - /" + cmd + " remove ivar <" + textName + "> <Variable>\n" +
                        "    - /" + cmd + " remove world <" + textName + "> <World>\n" +
                        "    - /" + cmd + " remove wvar <" + textName + "> <World> <Variable>");
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase(typeName.substring(0, 1))
                    || args[1].equalsIgnoreCase(typeName)) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".remove." + typeName)) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.REMOVE_BASE;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.removeBase(uuid, type);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("iVar")
                    || args[1].equalsIgnoreCase("infoVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".remove.ivar")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.REMOVE_INFO_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.removeInfoVar(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("w")
                    || args[1].equalsIgnoreCase("world")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".remove.world")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.REMOVE_WORLD;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.removeWorld(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("wVar")
                    || args[1].equalsIgnoreCase("worldVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + alterType + ".remove.wvar")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.REMOVE_WORLD_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.removeWorldVar(uuid, type, args[3], args[4]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            }
        } else if (type == InfoType.USER
                && (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("set"))) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/mchat user set':\n" +
                        "    - /" + cmd + " set group <Player> <" + textName + ">");
                return Optional.of(CommandResult.success());
            } else if (args[1].equalsIgnoreCase("g")
                    || args[1].equalsIgnoreCase("group")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat.user.set.group")) {
                    return Optional.of(CommandResult.success());
                }

                editType = InfoEditType.SET_GROUP;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return Optional.of(CommandResult.success());
                }

                Writer.setGroup(uuid, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return Optional.of(CommandResult.success());
            }
        }

        return Optional.of(CommandResult.empty());
    }

    public boolean testPermission(CommandSource commandSource) {
        return true;
    }

    public Optional<Text> getShortDescription(CommandSource commandSource) {
        return Optional.of(Texts.of(new Object[]{"MChat Info Alter Commands"}));
    }

    public Optional<Text> getHelp(CommandSource commandSource) {
        return Optional.absent();
    }

    public Text getUsage(CommandSource commandSource) {
        if (type == InfoType.USER) {
            return Texts.of("/<command> = Show Info User Help.");
        } else if (type == InfoType.GROUP) {
            return Texts.of("/<command> = Show Info Group Help.");
        }

        return Texts.of();
    }

    public List<String> getSuggestions(CommandSource source, String raw) throws CommandException {
        String[] args = raw.split(" ");

        if (args.length == 1) {
            if (type == InfoType.USER) {
                return Arrays.asList("add", "set", "remove");
            } else {
                return Arrays.asList("add", "remove");
            }
        }

        String t = "player";

        if (type == InfoType.GROUP) {
            t = "group";
        }

        if (args.length == 2) {
            if (type == InfoType.USER && (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("set"))) {
                return Collections.singletonList("group");
            } else {
                return Arrays.asList(t, "ivar", "world", "wvar");
            }
        }

        List<String> uuids = new ArrayList<String>();

        if (args.length > 2 && !args[2].isEmpty()) {
            if (type == InfoType.GROUP) {
                uuids.add(args[2]);
            } else {
                for (Player player : ServerUtil.getServer().getOnlinePlayers()) {
                    if (player.getUniqueId().toString().startsWith(args[2].toLowerCase())) {
                        uuids.add(player.getUniqueId().toString());
                    } else if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                        uuids.add(player.getName());
                    }
                }
            }
        }

        if (args.length == 3) {
            return uuids;
        }

        return Collections.emptyList();
    }

    private String combineArgs(String[] args, Integer startingPoint) {
        String argString = "";

        for (int i = startingPoint; i < args.length; ++i)
            argString += args[i] + " ";

        return argString.trim();
    }
}