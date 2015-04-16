package ca.q0r.sponge.mchat.config.main;

import ca.q0r.sponge.mchat.config.Config;
import com.google.common.base.Functions;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfig extends Config {
    private List<Object> meAliases = new ArrayList<Object>();

    private HashMap<String, List<String>> aliasMap = new HashMap<String, List<String>>();

    public MainConfig() {
        super("config.conf");
    }

    public void loadDefaults() {
        checkNode(new Object[]{"mchat", "api-only"}, false);
        checkNode(new Object[]{"mchat", "update-check"}, true);
        checkNode(new Object[]{"mchat", "alter", "events"}, true);
        checkNode(new Object[]{"mchat", "alter", "list"}, true);
        checkNode(new Object[]{"mchat", "chat-distance"}, -1.0);
        checkNode(new Object[]{"mchat", "var-indicator"}, "+");
        checkNode(new Object[]{"mchat", "cus-var-indicator"}, "-");
        checkNode(new Object[]{"mchat", "locale-var-indicator"}, "%");
        checkNode(new Object[]{"mchat", "ip-censor"}, true);
        checkNode(new Object[]{"mchat", "caps-lock-range"}, 3);

        checkNode(new Object[]{"suppress", "use-join"}, false);
        checkNode(new Object[]{"suppress", "use-kick"}, false);
        checkNode(new Object[]{"suppress", "use-quit"}, false);
        checkNode(new Object[]{"suppress", "max-join"}, 30);
        checkNode(new Object[]{"suppress", "max-kick"}, 30);
        checkNode(new Object[]{"suppress", "max-quit"}, 30);

        checkNode(new Object[]{"info", "use-new-info"}, false);
        checkNode(new Object[]{"info", "use-leveled-nodes"}, false);
        checkNode(new Object[]{"info", "use-old-nodes"}, false);
        checkNode(new Object[]{"info", "add-new-players"}, false);
        checkNode(new Object[]{"info", "default-group"}, "default");

        loadAliases();

        checkNode(new Object[]{"aliases", "mchatme"}, meAliases);

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
        if (!config.getNode("aliases").isVirtual()) {
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : config.getNode("aliases").getChildrenMap().entrySet()) {
                aliasMap.put(entry.getKey().toString(), config.getNode("aliases", entry.getKey()).getList(Functions.toStringFunction()));
            }
        }
    }
}