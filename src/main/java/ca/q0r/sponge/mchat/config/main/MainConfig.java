package ca.q0r.sponge.mchat.config.main;

import ca.q0r.sponge.mchat.config.Config;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfig extends Config {
    private ArrayList<String> meAliases = new ArrayList<String>();

    private HashMap<String, List<String>> aliasMap = new HashMap<String, List<String>>();

    public MainConfig() {
        super(new File("plugins/MChat/config.yml"));
    }

    public void loadDefaults() {
        checkOption("mchat.api-only", false);
        checkOption("mchat.update-check", true);
        checkOption("mchat.alter.events", true);
        checkOption("mchat.alter.list", true);
        checkOption("mchat.chat-distance", -1.0);
        checkOption("mchat.var-indicator", "+");
        checkOption("mchat.cus-var-indicator", "-");
        checkOption("mchat.locale-var-indicator", "%");
        checkOption("mchat.ip-censor", true);
        checkOption("mchat.caps-lock-range", 3);

        checkOption("suppress.use-join", false);
        checkOption("suppress.use-kick", false);
        checkOption("suppress.use-quit", false);
        checkOption("suppress.max-join", 30);
        checkOption("suppress.max-kick", 30);
        checkOption("suppress.max-quit", 30);

        checkOption("info.use-new-info", false);
        checkOption("info.use-leveled-nodes", false);
        checkOption("info.use-old-nodes", false);
        checkOption("info.add-new-players", false);
        checkOption("info.default-group", "default");

        loadAliases();

        checkOption("aliases.mchatme", meAliases);

        setupAliasMap();

        save();
    }

    public HashMap<String, List<String>> getAliasMap() {
        return aliasMap;
    }

    private void loadAliases() {
        meAliases.add("me");
    }

    private void setupAliasMap() {
        for (Map.Entry<String, ConfigValue> entry : config.getConfig("aliases").entrySet()) {
            aliasMap.put(entry.getKey(), config.getStringList("aliases." + entry.getValue().render()));
        }
    }
}