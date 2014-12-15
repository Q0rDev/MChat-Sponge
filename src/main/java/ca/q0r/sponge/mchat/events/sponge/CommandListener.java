package ca.q0r.sponge.mchat.events.sponge;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainConfig;
import ca.q0r.sponge.mchat.util.ServerUtil;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandListener {
    public CommandListener() {
    }

    @Subscribe(order = Order.FIRST)
    public void onCommandEvent(CommandEvent event) {
        String command = event.getCommand();

        for (Map.Entry<String, List<String>> entry : ((MainConfig) ConfigManager.getConfig(ConfigType.CONFIG_HOCON)).getAliasMap().entrySet()) {
            for (String comm : entry.getValue()) {
                if (comm.equalsIgnoreCase(command)) {
                    List<String> list = Arrays.asList(entry.getKey());

                    try {
                        ServerUtil.getGame().getCommandDispatcher().call(event.getSource(), event.getArguments(), list);
                        event.setCancelled(true);
                    } catch (Exception ignored) {
                    }

                    return;
                }
            }
        }
    }
}