package ca.q0r.sponge.mchat.api;

import ca.q0r.sponge.mchat.config.Config;
import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.types.InfoType;
import org.spongepowered.api.entity.player.Player;

/**
 * Class used to write to <b>info.config</b>.
 */
public class Writer {
    /**
     * Used to set the Base values of an InfoType.
     *
     * @param type Type of Base you want to set.
     * @param name Defining value of the base (Also known as name/uuid).
     */
    public static void addBase(String name, InfoType type) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (type.equals(InfoType.USER)) {
            config.set(base + "." + name + ".group", MainType.INFO_DEFAULT_GROUP.getString());
        }

        config.set(base + "." + name + ".info.prefix", "");
        config.set(base + "." + name + ".info.suffix", "");

        save();

        if (type.equals(InfoType.USER)) {
            checkGroup(MainType.INFO_DEFAULT_GROUP.getString());
        }
    }

    /**
     * Used to add the Base for a Player with a custom DefaultGroup.
     *
     * @param uuid  Player's uuid.
     * @param group Default Group to set to the Base(Only needed if doing for InfoType.USER).
     */
    public static void addBase(String uuid, String group) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);

        addBase(uuid, InfoType.USER);

        config.set("users." + uuid + ".group", group);

        save();

        checkGroup(group);
    }

    /**
     * Used to add a World to a Base.
     *
     * @param name  Defining value of the base (Also known as name/uuid).
     * @param type  Type of Base you want to set.
     * @param world Name of the World you are trying to add.
     */
    public static void addWorld(String name, InfoType type, String world) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (config.getConfig().getNode(base + "." + name).isVirtual()) {
            addBase(name, type);
        }

        config.set(base + "." + name + ".worlds." + world + "prefix", "");
        config.set(base + "." + name + ".worlds." + world + "suffix", "");

        save();
    }

    /**
     * Used to add an Info Variable to a Base.
     *
     * @param name  Defining value of the base (Also known as name/uuid).
     * @param type  Type of Base you want to set.
     * @param var   Name of the Variable you are trying to add.
     * @param value Value of the Variable you are trying to add.
     */
    public static void setInfoVar(String name, InfoType type, String var, Object value) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (config.getConfig().getNode(base + "." + name).isVirtual()) {
            addBase(name, type);
        }

        config.set(base + "." + name + ".info." + var, value);

        save();
    }

    /**
     * Used to add a World Variable to a Base.
     *
     * @param name  Defining value of the base (Also known as name/uuid).
     * @param type  Type of Base you want to set.
     * @param world Name of the World you are trying to add the Variable to.
     * @param var   Name of the Variable you are trying to add.
     * @param value Value of the Variable you are trying to add.
     */
    public static void setWorldVar(String name, InfoType type, String world, String var, Object value) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (config.getConfig().getNode(base + "." + name + ".worlds." + world).isVirtual()) {
            addWorld(name, type, world);
        }

        config.set(base + "." + name + ".worlds." + world + "." + var, value);

        save();
    }

    /**
     * Used to set the Group of a Player.
     *
     * @param uuid  Player's uuid.
     * @param group Group to be set to Player.
     */
    public static void setGroup(String uuid, String group) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);

        if (config.getConfig().getNode(uuid + "." + group).isVirtual()) {
            addBase(uuid, group);
        }

        config.set("users." + uuid + ".group", group);

        save();
    }

    /**
     * Used to remove a Base.
     *
     * @param name Defining value of the base (Also known as name/uuid).
     * @param type Type of Base you want to remove.
     */
    public static void removeBase(String name, InfoType type) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (!config.getConfig().getNode(base + "." + name).isVirtual()) {
            config.set(base + "." + name, null);

            save();
        }
    }

    /**
     * Used to remove an Info Variable from a Base.
     *
     * @param name Defining value of the base (Also known as name/uuid).
     * @param type Type of Base you want to remove from.
     * @param var  Name of the Variable you are trying to remove.
     */
    public static void removeInfoVar(String name, InfoType type, String var) {
        setInfoVar(name, type, var, null);
    }

    /**
     * Used to remove a World from a Base.
     *
     * @param name  Defining value of the base (Also known as name/uuid).
     * @param type  Type of Base you want to remove from.
     * @param world Name of the World you are trying to remove.
     */
    public static void removeWorld(String name, InfoType type, String world) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = type.getConfValue();

        if (!config.getConfig().getNode(base + "." + name).isVirtual()
                && !config.getConfig().getNode(base + "." + name + ".worlds." + world).isVirtual()) {
            config.set(base + "." + name + ".worlds." + world, null);

            save();
        }
    }

    /**
     * Used to remove a World Variable from a Base.
     *
     * @param name  Defining value of the base (Also known as name/uuid).
     * @param type  Type of Base you want to remove from.
     * @param world Name of the World you are trying to remove from.
     * @param var   Name of the Variable you are trying to remove.
     */
    public static void removeWorldVar(String name, InfoType type, String world, String var) {
        setWorldVar(name, type, world, var, null);
    }

    /**
     * Used to convert a Player's Base to UUID format.
     *
     * @param player Player whose Base is going to be converted to UUID format.
     */
    public static void convertBase(Player player) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);
        String base = InfoType.USER.getConfValue();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();

        if (!config.getConfig().getNode(base + "." + name).isVirtual()) {
            config.set(base + "." + uuid, config.getConfig().getNode(base + "." + name));
            config.set(base + "." + name, null);

            save();
        }
    }

    private static void checkGroup(String group) {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);

        if (config.getConfig().getNode("groups." + group).isVirtual()) {
            config.set("groups." + group + ".info.prefix", "");
            config.set("groups." + group + ".info.suffix", "");

            save();
        }
    }

    private static void save() {
        Config config = ConfigManager.getConfig(ConfigType.INFO_HOCON);

        config.save();
    }
}