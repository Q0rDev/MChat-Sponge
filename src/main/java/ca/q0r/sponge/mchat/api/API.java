package ca.q0r.sponge.mchat.api;

import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.types.IndicatorType;
import ca.q0r.sponge.mchat.types.PluginType;
import ca.q0r.sponge.mchat.util.ServerUtil;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.world.World;

import java.util.*;

/**
 * Class used for various tasks not covered by other classes.
 */
//TODO Fix Permissions Handling
public class API {
    // Perm Service
    private static PermissionService service;

    // Var Map
    private static final SortedMap<String, String> gVarMap = Collections.synchronizedSortedMap(new TreeMap<String, String>());
    private static final SortedMap<String, String> pVarMap = Collections.synchronizedSortedMap(new TreeMap<String, String>());
    // Maps
    private static HashMap<String, Boolean> spying;

    /**
     * Class Initializer
     */
    public static void initialize() {
        service = ServerUtil.getGame().getServiceManager().provide(PermissionService.class).orNull();
        spying = new HashMap<String, Boolean>();
    }

    /**
     * Global Variable Addition
     *
     * @param var   Name of Variable being added.
     * @param value Value of Variable being added.
     */
    public static void addGlobalVar(String var, String value) {
        if (var == null || var.isEmpty()) {
            return;
        }

        if (value == null) {
            value = "";
        }

        synchronized (gVarMap) {
            gVarMap.put(var, value);
        }
    }

    /**
     * Global Variable Removal
     *
     * @param var Name of Variable being removed.
     */
    public static void removeGlobalVar(String var) {
        synchronized (gVarMap) {
            if (gVarMap.get(var) != null) {
                gVarMap.remove(var);
            }
        }
    }

    /**
     * Global Variable TreeMap.
     *
     * @return Map of Custom Variables.
     */
    public static SortedMap<String, Object> getGlobalVarMap() {
        SortedMap<String, Object> map = new TreeMap<String, Object>();

        synchronized (gVarMap) {
            map.putAll(Collections.unmodifiableSortedMap(gVarMap));

            return map;
        }
    }

    /**
     * Player Variable Addition
     *
     * @param uuid  UUID of Player this Variable is being added for.
     * @param var   Name of Variable being added.
     * @param value Value of Variable being added.
     */
    public static void addUuidVar(UUID uuid, String var, String value) {
        if (var == null || var.isEmpty()) {
            return;
        }

        if (value == null) {
            value = "";
        }

        synchronized (pVarMap) {
            pVarMap.put(uuid.toString() + "|" + var, value);
        }
    }

    /**
     * Player Variable Removal
     *
     * @param uuid UUID of Player this Variable is being removed from.
     * @param var  Name of Variable being removed.
     */
    public static void removeUuidVar(UUID uuid, String var) {
        synchronized (pVarMap) {
            if (pVarMap.get(uuid.toString() + "|" + var) != null) {
                pVarMap.remove(uuid.toString() + "|" + var);
            }
        }
    }

    /**
     * Player Variable TreeMap.
     *
     * @return Map of Custom Variables.
     */
    public static SortedMap<String, String> getUuidVarMap() {
        SortedMap<String, String> map = new TreeMap<String, String>();

        synchronized (pVarMap) {
            map.putAll(Collections.unmodifiableSortedMap(pVarMap));

            return map;
        }
    }

    /**
     * Health Bar Formatting
     *
     * @param player Player the HealthBar is being rendered for.
     *
     * @return Formatted Health Bar.
     */
    public static String createHealthBar(Player player) {
        float maxHealth = 20;
        float barLength = 10;
        float health = new Float(player.getHealth());

        return createBasicBar(health, maxHealth, barLength);
    }

    /**
     * Food Bar Formatting
     *
     * @param player Player the FoodBar is being rendered for.
     *
     * @return Formatted Health Bar.
     */
    public static String createFoodBar(Player player) {
        float maxFood = 20;
        float barLength = 10;
        float food = player.getHunger();

        return createBasicBar(food, maxFood, barLength);
    }

    /**
     * Basic Bar Formatting
     *
     * @param currentValue Current Value of Bar.
     * @param maxValue     Max Value of Bar.
     * @param barLength    Length of Bar.
     *
     * @return Formatted Health Bar.
     */
    public static String createBasicBar(float currentValue, float maxValue, float barLength) {
        int fill = Math.round((currentValue / maxValue) * barLength);

        String barColor = (fill <= (barLength / 4)) ? "&4" : (fill <= (barLength / 7)) ? "&e" : "&2";

        StringBuilder out = new StringBuilder();
        out.append(barColor);

        for (int i = 0; i < barLength; i++) {
            if (i == fill)
                out.append("&8");

            out.append("|");
        }

        out.append("&f");

        return out.toString();
    }

    /**
     * Permission Checking
     *
     * @param player Player being checked.
     * @param node   Permission Node being checked.
     *
     * @return Player has Node.
     */
    public static Boolean checkPermissions(Player player, String node) {
        return player.hasPermission(node);// || player.isOp();
    }

    /**
     * Permission Checking
     *
     * @param player Player being checked.
     * @param context Context of Player's World.
     * @param node   Permission Node being checked.
     *
     * @return Player has Node.
     */
    public static Boolean checkPermissions(Player player, Context context, String node) {
        HashSet<Context> cont = new HashSet<Context>();

        cont.add(context);

        return player.hasPermission(cont, node);// || player.isOp();
    }

    /**
     * Permission Checking
     *
     * @param pName Name of Player being checked.
     * @param wName Name of Player's World.
     * @param node  Permission Node being checked.
     *
     * @return Player has Node.
     */
    @Deprecated
    public static Boolean checkPermissions(String pName, String wName, String node) {
        Player player = ServerUtil.getServer().getPlayer(pName).orNull();
        World world = ServerUtil.getServer().getWorld(wName).orNull();

        return (player != null && world != null)&& checkPermissions(player, world.getContext(), node);
    }

    /**
     * Permission Checking
     *
     * @param uuid UUID of Player being checked.
     * @param node Permission Node being checked.
     *
     * @return Player has Node.
     */
    public static Boolean checkPermissions(UUID uuid, String node) {
        Player player = ServerUtil.getPlayer(uuid);
        return player != null && checkPermissions(player, node);
    }

    /**
     * Permission Checking
     *
     * @param subject Subject being checked.
     * @param node   Permission Node being checked.
     *
     * @return Sender has Node.
     */
    public static Boolean checkPermissions(Subject subject, String node) {
        return subject.hasPermission(node);
    }

    /**
     * Variable Replacer
     *
     * @param source  String being modified.
     * @param changes Map of Search / Replace pairs.
     * @param type    Type of Variable.
     *
     * @return Source with variables replaced.
     */
    public static String replace(String source, TreeMap<String, String> changes, IndicatorType type) {
        NavigableMap<String, String> changed = changes.descendingMap();

        for (NavigableMap.Entry<String, String> entry : changed.entrySet()) {
            source = source.replace(type.getValue() + entry.getKey(), entry.getValue());
        }

        return source;
    }

    /**
     * Variable Replacer
     *
     * @param source  String being modified.
     * @param search  String being searched for.
     * @param replace String search term is to be replaced with.
     * @param type    Type of Variable.
     *
     * @return Source with variable replaced.
     */
    public static String replace(String source, String search, String replace, IndicatorType type) {
        return source.replace(type.getValue() + search, replace);
    }

    /**
     * Used to check if Plugin is enabled.
     *
     * @param type Plugin to be checked.
     *
     * @return <code>true</code> if plugin is enabled <code>false</code> if not.
     */
    public static Boolean isPluginEnabled(PluginType type) {
        if (type == null) {
            return false;
        }

        switch (type) {
            case LEVELED_NODES:
                return MainType.INFO_USE_LEVELED_NODES.getBoolean();
            case OLD_NODES:
                return MainType.INFO_USE_OLD_NODES.getBoolean();
            case NEW_INFO:
                return MainType.INFO_USE_NEW_INFO.getBoolean();
            default:
                return false;
        }
    }

    /**
     * Spying HashMap.
     *
     * @return Map of Spying statuses.
     */
    public static HashMap<String, Boolean> getSpying() {
        return spying;
    }
}