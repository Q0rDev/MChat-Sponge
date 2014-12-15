package ca.q0r.sponge.mchat;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Writer;
import ca.q0r.sponge.mchat.commands.InfoAlterCommand;
import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.events.sponge.ChatListener;
import ca.q0r.sponge.mchat.events.sponge.CommandListener;
import ca.q0r.sponge.mchat.commands.MChatCommand;
import ca.q0r.sponge.mchat.commands.MeCommand;
import ca.q0r.sponge.mchat.events.sponge.PlayerListener;
import ca.q0r.sponge.mchat.types.InfoType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import ca.q0r.sponge.mchat.util.Timer;
import ca.q0r.sponge.mchat.variables.VariableManager;
import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.event.Subscribe;

@Plugin(id = "MChat", name = "MChat", version = "${version}-${commit}", dependencies = "after:GeoIPTools,Heroes,Towny,Vault,SimpleClans2,SimpleClansChat;")
public class MChat {
    @Subscribe
    public void onEnable(ServerStartingEvent event) {
        // Initialize and Start the Timer
        Timer timer = new Timer();

        // Initialize Game
        ServerUtil.initialize(event.getGame(), this);

        // Load Config
        ConfigManager.initialize();

        // Initialize Classes
        initializeClasses();

        // Register Events
        registerEvents(ServerUtil.getGame().getEventManager());

        // Setup Commands
        setupCommands(ServerUtil.getGame().getCommandDispatcher());

        // Add All Players To Info Config
        if (MainType.INFO_ADD_NEW_PLAYERS.getBoolean()) {
            for (Player player : ServerUtil.getGame().getServer().get().getOnlinePlayers()) {
                if (!ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig().hasPath("users." + player.getUniqueId().toString())) {
                    Writer.addBase(player.getUniqueId().toString(), InfoType.USER);
                }
            }
        }

        // Stop the Timer
        timer.stop();

        // Calculate Startup Timer
        long diff = timer.difference();

        MessageUtil.log("[MChat] MChat v${version}-${commit} is enabled! [" + diff + "ms]");
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent event) {
        // Initialize and Start the Timer
        Timer timer = new Timer();

        event.getGame().getScheduler().getScheduledTasks().clear();

        // Unload Config
        ConfigManager.unload();

        // Stop the Timer
        timer.stop();

        // Calculate Shutdown Timer
        long diff = timer.difference();

        MessageUtil.log("[MChat] MChat v${version}-${commit} is disabled! [" + diff + "ms]");
    }

    private void registerEvents(EventManager mn) {
        if (!MainType.MCHAT_API_ONLY.getBoolean()) {
            mn.register(new ChatListener(), this);
            mn.register(new CommandListener(), this);
            mn.register(new PlayerListener(this), this);
        }
    }

    private void setupCommands(CommandService cd) {
        regCommands(cd, new MChatCommand(), "mchat");

        regCommands(cd, new InfoAlterCommand("mchatuser", InfoType.USER), "mchatuser", "muser");
        regCommands(cd, new InfoAlterCommand("mchatgroup", InfoType.GROUP), "mchatgroup", "mgroup");

        regCommands(cd, new MeCommand(), "mchatme");
    }

    private void regCommands(CommandService cd, CommandCallable callable, String... aliases) {
        cd.register(this, callable, aliases);
    }

    private void initializeClasses() {
        API.initialize();
        VariableManager.initialize();
    }
}