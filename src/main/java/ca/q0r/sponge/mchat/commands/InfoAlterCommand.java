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
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NonnullByDefault
public class InfoAlterCommand implements CommandCallable {
    private InfoType type;
    private String cmd;

    public InfoAlterCommand(String command, InfoType infoType) {
        type = infoType;
        cmd = command;
    }

    public boolean call(CommandSource source, String raw, List<String> list) throws CommandException {
        String[] args = raw.split(" ");

        //if (!command.getName().equalsIgnoreCase(cmd)) {
        //    return true;
        //}

        if (args.length == 0) {
            MessageUtil.sendMessage(source, "Use '/" + cmd + " add/edit/remove' for more info.");
            return true;
        }

        InfoEditType editType;

        String p = "user";
        String t = "player";
        String T = "Player";
        String uuid = "";

        if (type == InfoType.GROUP) {
            p = "group";
            t = "group";
            T = "Group";
        }

        if (args.length > 2) {
            if (type == InfoType.GROUP) {
                uuid = args[2];
            } else {
                Player player = ServerUtil.getPlayer(args[2]);

                if (player == null) {
                    MessageUtil.sendMessage(source, "Player '" + args[2] + "' is offline for this command.");
                    MessageUtil.sendMessage(source, "Until Proper API's have been released Players will have to be online.");
                    return true;
                } else {
                    uuid = player.getUniqueId().toString();
                }
            }
        }

        if (args[0].equalsIgnoreCase("a")
                || args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/" + cmd + " add':\n" +
                        "    - /" + cmd + " add " + t + " <" + T + ">\n" +
                        "    - /" + cmd + " add ivar <" + T + "> <Variable> [Value]\n" +
                        "    - /" + cmd + " add world <" + T + "> <World>\n" +
                        "    - /" + cmd + " add wvar <" + T + "> <World> <Variable> [Value]");
                return true;
            } else if (args[1].equalsIgnoreCase(t.substring(0, 1))
                    || args[1].equalsIgnoreCase(t)) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".add." + t)) {
                    return true;
                }

                editType = InfoEditType.ADD_BASE;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.addBase(uuid, type);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("iVar")
                    || args[1].equalsIgnoreCase("infoVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".add.ivar")) {
                    return true;
                }

                editType = InfoEditType.ADD_INFO_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.setInfoVar(uuid, type, args[3], combineArgs(args, 4));
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("w")
                    || args[1].equalsIgnoreCase("world")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".add.world")) {
                    return true;
                }

                editType = InfoEditType.ADD_WORLD;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.addWorld(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("wVar")
                    || args[1].equalsIgnoreCase("worldVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".add.wvar")) {
                    return true;
                }

                editType = InfoEditType.ADD_WORLD_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.setWorldVar(uuid, type, args[3], args[4], combineArgs(args, 5));
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            }
        } else if (args[0].equalsIgnoreCase("r")
                || args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/" + cmd + " remove':\n" +
                        "    - /" + cmd + " remove " + t + " <" + T + ">\n" +
                        "    - /" + cmd + " remove ivar <" + T + "> <Variable>\n" +
                        "    - /" + cmd + " remove world <" + T + "> <World>\n" +
                        "    - /" + cmd + " remove wvar <" + T + "> <World> <Variable>");
                return true;
            } else if (args[1].equalsIgnoreCase(t.substring(0, 1))
                    || args[1].equalsIgnoreCase(t)) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".remove." + t)) {
                    return true;
                }

                editType = InfoEditType.REMOVE_BASE;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.removeBase(uuid, type);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("iVar")
                    || args[1].equalsIgnoreCase("infoVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".remove.ivar")) {
                    return true;
                }

                editType = InfoEditType.REMOVE_INFO_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.removeInfoVar(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("w")
                    || args[1].equalsIgnoreCase("world")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".remove.world")) {
                    return true;
                }

                editType = InfoEditType.REMOVE_WORLD;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.removeWorld(uuid, type, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            } else if (args[1].equalsIgnoreCase("wVar")
                    || args[1].equalsIgnoreCase("worldVariable")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat." + p + ".remove.wvar")) {
                    return true;
                }

                editType = InfoEditType.REMOVE_WORLD_VAR;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.removeWorldVar(uuid, type, args[3], args[4]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            }
        } else if (type == InfoType.USER
                && (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("set"))) {
            if (args.length == 1) {
                MessageUtil.sendMessage(source, "Usage for '/mchat user set':\n" +
                        "    - /" + cmd + " set group <Player> <" + T + ">");
                return true;
            } else if (args[1].equalsIgnoreCase("g")
                    || args[1].equalsIgnoreCase("group")) {
                if (!CommandUtil.hasCommandPerm(source, "mchat.user.set.group")) {
                    return true;
                }

                editType = InfoEditType.SET_GROUP;

                if (args.length < editType.getLength()) {
                    editType.sendMsg(source, cmd, type);
                    return true;
                }

                Writer.setGroup(uuid, args[3]);
                MessageUtil.sendMessage(source, LocaleType.MESSAGE_INFO_ALTERATION.getVal());
                return true;
            }
        }

        return false;
    }

    public boolean testPermission(CommandSource commandSource) {
        return true;
    }

    public Optional<String> getShortDescription() {
        return Optional.of("MChat Info Alter Commands");
    }

    public Optional<String> getHelp() {
        return Optional.absent();
    }

    public String getUsage() {
        if (type == InfoType.USER) {
            return "/<command> = Show Info User Help.";
        } else if (type == InfoType.GROUP) {
            return "/<command> = Show Info Group Help.";
        }

        return "";
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
                return Arrays.asList("group");
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

        return Arrays.asList();
    }

    private String combineArgs(String[] args, Integer startingPoint) {
        String argString = "";

        for (int i = startingPoint; i < args.length; ++i)
            argString += args[i] + " ";

        return argString.trim();
    }
}