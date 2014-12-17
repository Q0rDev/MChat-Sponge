package ca.q0r.sponge.mchat.api;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.locale.LocaleType;
import ca.q0r.sponge.mchat.types.EventType;
import ca.q0r.sponge.mchat.types.PluginType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.google.common.collect.Iterables;
import com.typesafe.config.ConfigValue;
import org.spongepowered.api.util.config.ConfigFile;

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

        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (infoConfig.hasPath("users." + uuid.toString() + ".info." + info)) {
            return infoConfig.getString("users." + uuid.toString() + ".info." + info);
        } else if (infoConfig.hasPath("users." + uuid.toString() + ".worlds." + world + "." + info)) {
            return infoConfig.getString("users." + uuid.toString() + ".worlds." + world + "." + info);
        } else if (infoConfig.hasPath("users." + uuid.toString() + ".group")) {
            String group = infoConfig.getString("users." + uuid.toString() + ".group");

            if (infoConfig.hasPath("groups." + group + ".info." + info)) {
                return infoConfig.getString("groups." + group + ".info." + info);
            } else if (infoConfig.hasPath("groups." + group + ".worlds." + world + "." + info)) {
                return infoConfig.getString("groups." + group + ".worlds." + world + "." + info);
            }
        }

        return "";
    }

    private static String getMChatGroup(UUID uuid) {
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (infoConfig.hasPath("users." + uuid.toString() + ".group")) {
            return infoConfig.getString("users." + uuid.toString() + ".group");
        }

        return "";
    }

    private static String getLeveledInfo(UUID uuid, String info) {
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();
        HashMap<Integer, String> iMap = new HashMap<Integer, String>();

        if (!infoConfig.hasPath("mchat." + info)) {
            return "";
        }

        if (!infoConfig.hasPath("rank." + info)) {
            return getSpongeInfo(uuid, info);
        }

        for (Map.Entry<String, ConfigValue> entry : infoConfig.entrySet()) {
            if (entry.getKey().contains("mchat." + info + ".")) {
                if (API.checkPermissions(uuid, entry.getKey())) {
                    String rVal = entry.getKey().replaceFirst("mchat\\.", "rank.");

                    if (!infoConfig.hasPath(rVal)) {
                        continue;
                    }

                    try {
                        iMap.put(infoConfig.getInt(rVal), entry.getValue().toString());
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
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (!infoConfig.hasPath("mchat." + info)) {
            return "";
        }

        for (Map.Entry<String, ConfigValue> entry : infoConfig.entrySet()) {
            if (entry.getKey().contains("mchat." + info + ".")) {
                if (API.checkPermissions(uuid, entry.getKey())) {
                    String infoResolve = entry.getValue().render();

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
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (group.isEmpty()) {
            return "";
        }

        if (infoConfig.hasPath("groupnames." + group)) {
            return infoConfig.getString("groupnames." + group);
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
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (world.isEmpty()) {
            return "";
        }

        if (infoConfig.hasPath("worldnames." + world)) {
            return infoConfig.getString("worldnames." + world);
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
        ConfigFile infoConfig = ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig();

        if (infoConfig.hasPath("mname." + uuid)) {
            if (!(infoConfig.getString("mname." + uuid).isEmpty())) {
                return infoConfig.getString("mname." + uuid);
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