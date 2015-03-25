package ca.q0r.sponge.mchat.api;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.locale.LocaleType;
import ca.q0r.sponge.mchat.types.EventType;
import ca.q0r.sponge.mchat.types.PluginType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.google.common.collect.Iterables;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class used to read from <b>info config</b> and various other Plugins.
 */
public class Reader {
    /**
     * Raw Info Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     * @param info  Info Variable being resolved.
     *
     * @return Raw Info.
     */
    public static String getRawInfo(UUID uuid, String world, String info) {
        if (uuid == null) {
            return "";
        }

        if (world == null) {
            world = Iterables.get(ServerUtil.getServer().getWorlds(), 0).getName();
        }

        if (info == null) {
            info = "prefix";
        }

        if (API.isPluginEnabled(PluginType.LEVELED_NODES)) {
            return getLeveledInfo(uuid, info);
        } else if (API.isPluginEnabled(PluginType.OLD_NODES)) {
            return getSpongeInfo(uuid, info);
        } else if (API.isPluginEnabled(PluginType.NEW_INFO)) {
            return getMChatInfo(uuid, world, info);
        }

        return getMChatInfo(uuid, world, info);
    }

    /**
     * Raw Prefix Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Raw Prefix.
     */
    public static String getRawPrefix(UUID uuid, String world) {
        return getRawInfo(uuid, world, "prefix");
    }

    /**
     * Raw Suffix Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Raw Suffix.
     */
    public static String getRawSuffix(UUID uuid, String world) {
        return getRawInfo(uuid, world, "suffix");
    }

    /**
     * Raw Group Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Raw Group.
     */
    public static String getRawGroup(UUID uuid, String world) {
        return getRawInfo(uuid, world, "group");
    }

    /**
     * Raw Info Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Player's World.
     * @param info  Info Variable being resolved.
     *
     * @return Raw Info.
     */
    public static String getInfo(UUID uuid, String world, String info) {
        return MessageUtil.addColour(getRawInfo(uuid, world, info));
    }

    /**
     * Formatted Prefix Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Formatted Prefix.
     */
    public static String getPrefix(UUID uuid, String world) {
        return getInfo(uuid, world, "prefix");
    }

    /**
     * Formatted Suffix Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Formatted Suffix.
     */
    public static String getSuffix(UUID uuid, String world) {
        return getInfo(uuid, world, "suffix");
    }

    /**
     * Formatted Group Resolving
     *
     * @param uuid  Defining value of the InfoType (Also known as name/uuid).
     * @param world Name of the InfoType's World.
     *
     * @return Formatted Group.
     */
    public static String getGroup(UUID uuid, String world) {
        return getInfo(uuid, world, "group");
    }

    private static String getMChatInfo(UUID uuid, String world, String info) {
        if (info.equals("group")) {
            return getMChatGroup(uuid);
        }

        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (!infoConfig.getNode("users", uuid.toString(), "info", info).isVirtual()) {
            return infoConfig.getNode("users", uuid.toString(), "info", info).getString();
        } else if (!infoConfig.getNode("users", uuid.toString(), "worlds", world + "", info).isVirtual()) {
            return infoConfig.getNode("users", uuid.toString(), "worlds", world + "", info).getString();
        } else if (!infoConfig.getNode("users", uuid.toString(), "group").isVirtual()) {
            String group = infoConfig.getNode("users", uuid.toString(), "group").getString();

            if (!infoConfig.getNode("groups", group, "info", info).isVirtual()) {
                return infoConfig.getNode("groups", group, "info", info).getString();
            } else if (!infoConfig.getNode("groups", group, "worlds", world + "", info).isVirtual()) {
                return infoConfig.getNode("groups", group, "worlds", world + "", info).getString();
            }
        }

        return "";
    }

    private static String getMChatGroup(UUID uuid) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (!infoConfig.getNode("users", uuid.toString(), "group").isVirtual()) {
            return infoConfig.getNode("users", uuid.toString(), "group").getString();
        }

        return "";
    }

    private static String getLeveledInfo(UUID uuid, String info) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();
        HashMap<Integer, String> iMap = new HashMap<Integer, String>();

        if (infoConfig.getNode("mchat", info).isVirtual()) {
            return "";
        }

        if (infoConfig.getNode("rank", info).isVirtual()) {
            return getSpongeInfo(uuid, info);
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : infoConfig.getNode("mchat").getChildrenMap().entrySet()) {
            String key = entry.getKey().toString();

            if (key.contains(info)) {
                if (API.checkPermissions(uuid, key)) {
                    String rVal = key.replaceFirst("mchat\\.", "rank.");

                    if (infoConfig.getNode(rVal).isVirtual()) {
                        continue;
                    }

                    try {
                        iMap.put(infoConfig.getNode(rVal).getInt(), entry.getValue().getString());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        for (int i = 0; i < 101; ++i) {
            if (iMap.get(i) != null && !iMap.get(i).isEmpty()) {
                return iMap.get(i);
            }
        }

        return getSpongeInfo(uuid, info);
    }

    private static String getSpongeInfo(UUID uuid, String info) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (infoConfig.getNode("mchat", info).isVirtual()) {
            return "";
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : infoConfig.getNode("mchat").getChildrenMap().entrySet()) {
            String key = entry.getKey().toString();

            if (key.contains(info)) {
                if (API.checkPermissions(uuid, key)) {
                    String infoResolve = entry.getValue().getString();

                    if (infoResolve != null && !info.isEmpty()) {
                        return infoResolve;
                    }

                    break;
                }
            }
        }

        return "";
    }

    /**
     * Group Name Resolver
     *
     * @param group Group to be Resolved.
     *
     * @return Group Name's Alias.
     */
    public static String getGroupName(String group) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (group.isEmpty()) {
            return "";
        }

        if (!infoConfig.getNode("groupnames", group).isVirtual()) {
            return infoConfig.getNode("groupnames", group).getString();
        }

        return group;
    }

    /**
     * World Name Resolver
     *
     * @param world Group to be Resolved.
     *
     * @return World Name's Alias.
     */
    public static String getWorldName(String world) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (world.isEmpty()) {
            return "";
        }

        if (!infoConfig.getNode("worldnames", world).isVirtual()) {
            return infoConfig.getNode("worldnames", world).getString();
        }

        return world;
    }

    /**
     * Player Name Resolver
     *
     * @param uuid UUID of Player to be Resolved.
     *
     * @return Player Name's MChat Alias.
     */
    public static String getMName(UUID uuid) {
        ConfigurationNode infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (!infoConfig.getNode("mname", uuid.toString()).isVirtual()) {
            if (!(infoConfig.getNode("mname", uuid.toString()).getString().isEmpty())) {
                return infoConfig.getNode("mname", uuid.toString()).getString();
            }
        }

        return ServerUtil.getPlayer(uuid).getName();
    }

    /**
     * Event Message Resolver.
     *
     * @param type Type of Event you want to grab.
     *
     * @return Event Message.
     */
    public static String getEventMessage(EventType type) {
        switch (type) {
            case JOIN:
                return LocaleType.MESSAGE_EVENT_JOIN.getRaw();
            case KICK:
                return LocaleType.MESSAGE_EVENT_KICK.getRaw();
            case LEAVE:
                return LocaleType.MESSAGE_EVENT_LEAVE.getRaw();
            case QUIT:
                return LocaleType.MESSAGE_EVENT_LEAVE.getRaw();
        }

        return "";
    }
}