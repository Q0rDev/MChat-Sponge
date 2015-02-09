package ca.q0r.sponge.mchat.config.main;

import ca.q0r.sponge.mchat.config.Config;
import com.google.common.base.Functions;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfig extends Config {
    private ArrayList<String> meAliases = new ArrayList<String>();

    private HashMap<String, List<String>> aliasMap = new HashMap<String, List<String>>();

    public MainConfig() {
        super("config.conf");
    }

    public void loadDefaults() {
        checkNode(new String[] {"mchat", "api-only"}, false);
        checkNode(new String[] {"mchat", "update-check"}, true);
        checkNode(new String[] {"mchat", "alter", "events"}, true);
        checkNode(new String[] {"mchat", "alter", "list"}, true);
        checkNode(new String[] {"mchat", "chat-distance"}, -1.0);
        checkNode(new String[] {"mchat", "var-indicator"}, "+");
        checkNode(new String[] {"mchat", "cus-var-indicator"}, "-");
        checkNode(new String[] {"mchat", "locale-var-indicator"}, "%");
        checkNode(new String[] {"mchat", "ip-censor"}, true);
        checkNode(new String[] {"mchat", "caps-lock-range"}, 3);

        checkNode(new String[] {"suppress", "use-join"}, false);
        checkNode(new String[] {"suppress", "use-kick"}, false);
        checkNode(new String[] {"suppress", "use-quit"}, false);
        checkNode(new String[] {"suppress", "max-join"}, 30);
        checkNode(new String[] {"suppress", "max-kick"}, 30);
        checkNode(new String[] {"suppress", "max-quit"}, 30);

        checkNode(new String[] {"info", "use-new-info"}, false);
        checkNode(new String[] {"info", "use-leveled-nodes"}, false);
        checkNode(new String[] {"info", "use-old-nodes"}, false);
        checkNode(new String[] {"info", "add-new-players"}, false);
        checkNode(new String[] {"info", "default-group"}, "default");

        loadAliases();

        checkNode(new String[] {"aliases", "mchatme"}, meAliases);

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
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : config.getNode("aliases").getChildrenMap().entrySet()) {
            aliasMap.put(entry.getKey().toString(), config.getNode("aliases", entry.getKey()).getList(Functions.toStringFunction()));
        }
    }
}