package ca.q0r.sponge.mchat.events.sponge;

import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainConfig;
import ca.q0r.sponge.mchat.util.ServerUtil;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.message.CommandEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandListener {
    public CommandListener() {
    }

    @Subscribe(order = Order.EARLY)
    public void onCommandEvent(CommandEvent event) {
        String command = event.getCommand();

        for (Map.Entry<String, List<String>> entry : ((MainConfig) ConfigManager.getConfig(ConfigType.CONFIG_HOCON)).getAliasMap().entrySet()) {
            for (String comm : entry.getValue()) {
                if (comm.equalsIgnoreCase(command)) {
                    List<String> list = Arrays.asList(entry.getKey());

                    try {
                        ServerUtil.getGame().getCommandDispatcher().process(event.getSource(), event.getArguments());
                        event.setCancelled(true);
                    } catch (Exception ignored) {
                    }

                    return;
                }
            }
        }
    }
}