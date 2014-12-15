package ca.q0r.sponge.mchat.variables;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.events.custom.ReplaceEvent;
import ca.q0r.sponge.mchat.types.IndicatorType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import ca.q0r.sponge.mchat.variables.vars.MessageVars;
import ca.q0r.sponge.mchat.variables.vars.PlayerVars;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Manager for Variables.
 */
public class VariableManager {
    private static Set<Var> varSet;

    /**
     * Initializes Manager.
     */
    public static void initialize() {
        varSet = new HashSet<Var>();

        // Initialize Player Vars
        PlayerVars.addVars();

        MessageVars.addVars();
    }

    /**
     * Adds Var to VarSet.
     *
     * @param var Variable processor.
     */
    public static void addVar(Var var) {
        if (var != null) {
            varSet.add(var);
        }
    }

    /**
     * Adds Vars to VarSet.
     *
     * @param vars Variable processor Array.
     */
    public static void addVars(Var[] vars) {
        if (vars != null) {
            for (Var var : vars) {
                addVar(var);
            }
        }
    }

    /**
     * Removes Var from VarSet by their Keys.
     *
     * @param key Key of Var to be removed.
     */
    public static void removeVar(String key) {
        Set<Var> set = new HashSet<Var>();

        for (Var var : varSet) {
            Method[] methods = var.getClass().getMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Var.Keys.class)) {
                    Var.Keys vKeys = method.getAnnotation(Var.Keys.class);

                    if (Arrays.asList(vKeys.keys()).contains(key)) {
                        set.add(var);
                    }
                }
            }
        }

        varSet.removeAll(set);
    }

    /**
     * Removes Vars from VarSet by their Keys.
     *
     * @param keys Keys of Vars to be removed.
     */
    public static void removeVars(String[] keys) {
        for (String key : keys) {
            removeVar(key);
        }
    }

    /**
     * Variable Replacer.
     *
     * @param format   String to be replaced.
     * @param uuid     Name of Player being formatted against.
     * @param msg      Message being relayed.
     * @param doColour Whether or not to colour replacement value.
     * @return String with Variables replaced.
     */
    public static String replaceVars(String format, UUID uuid, String msg, Boolean doColour) {
        NavigableMap<String, String> fVarMap = new TreeMap<String, String>();
        NavigableMap<String, String> nVarMap = new TreeMap<String, String>();
        NavigableMap<String, String> lVarMap = new TreeMap<String, String>();

        for (Var var : varSet) {
            Method[] methods = var.getClass().getMethods();

            for (Method method : methods) {
                ResolvePriority priority = ResolvePriority.DEFAULT;
                IndicatorType type = IndicatorType.MISC_VAR;
                String[] keys = {};
                String value = "";

                if (method.isAnnotationPresent(Var.Keys.class)) {
                    Var.Keys vKeys = method.getAnnotation(Var.Keys.class);

                    keys = vKeys.keys();

                    if (msg != null && var instanceof MessageVars.MessageVar) {
                        value = msg;
                    } else {
                        value = var.getValue(uuid);
                    }
                }

                if (method.isAnnotationPresent(Var.Meta.class)) {
                    Var.Meta vMeta = method.getAnnotation(Var.Meta.class);

                    priority = vMeta.priority();
                    type = vMeta.type();
                }

                switch (priority) {
                    case FIRST:
                        for (String key : keys) {
                            fVarMap.put(type.getValue() + key, value);
                        }

                        break;
                    case DEFAULT:
                        for (String key : keys) {
                            nVarMap.put(type.getValue() + key, value);
                        }

                        break;
                    case LAST:
                        for (String key : keys) {
                            lVarMap.put(type.getValue() + key, value);
                        }

                        break;
                    default:
                        for (String key : keys) {
                            nVarMap.put(type + key, value);
                        }

                        break;
                }
            }
        }

        fVarMap = fVarMap.descendingMap();
        nVarMap = nVarMap.descendingMap();
        lVarMap = lVarMap.descendingMap();

        format = replacer(format, fVarMap, doColour);
        format = replacer(format, nVarMap, doColour);
        format = replacer(format, lVarMap, false);

        return format;
    }

    /**
     * Custom Variable Replacer.
     *
     * @param uuid   Player's UUID.
     * @param format String to be replaced.
     * @return String with Custom Variables replaced.
     */
    public static String replaceCustVars(UUID uuid, String format) {
        for (Map.Entry<String, String> entry : API.getUuidVarMap().entrySet()) {
            String pKey = IndicatorType.CUS_VAR.getValue() + entry.getKey().replace(uuid.toString() + "|", "");
            String value = entry.getValue();

            ReplaceEvent event = new ReplaceEvent(pKey, value, format);
            ServerUtil.getGame().getEventManager().post(event);

            if (!event.isCancelled()) {
                format = event.getReplacedFormat();
            }
        }

        for (Map.Entry<String, Object> entry : API.getGlobalVarMap().entrySet()) {
            String gKey = IndicatorType.CUS_VAR.getValue() + entry.getKey();
            String value = entry.getValue().toString();

            ReplaceEvent event = new ReplaceEvent(gKey, value, format);
            ServerUtil.getGame().getEventManager().post(event);

            if (!event.isCancelled()) {
                format = event.getReplacedFormat();
            }
        }

        return format;
    }

    private static String replacer(String format, Map<String, String> map, Boolean doColour) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null) {
                value = "";
            }

            if (doColour) {
                value = MessageUtil.addColour(value);
            }

            ReplaceEvent event = new ReplaceEvent(key, value, format);
            ServerUtil.getGame().getEventManager().post(event);

            if (!event.isCancelled()) {
                format = event.getReplacedFormat();
            }
        }

        return format;
    }
}