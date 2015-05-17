package ca.q0r.sponge.mchat;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Writer;
import ca.q0r.sponge.mchat.commands.InfoAlterCommand;
import ca.q0r.sponge.mchat.commands.MChatCommand;
import ca.q0r.sponge.mchat.commands.MeCommand;
import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.events.sponge.ChatListener;
import ca.q0r.sponge.mchat.events.sponge.CommandListener;
import ca.q0r.sponge.mchat.events.sponge.PlayerListener;
import ca.q0r.sponge.mchat.types.InfoType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import ca.q0r.sponge.mchat.util.Timer;
import ca.q0r.sponge.mchat.variables.VariableManager;
import ca.q0r.sponge.util.VersionUtil;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.command.CommandCallable;

@Plugin(id = "MChat", name = "MChat", version = VersionUtil.VERSION, dependencies = "after:GeoIPTools,Heroes,Towny,Vault,SimpleClans2,SimpleClansChat;")
public class MChat {
    @Subscribe
    public void onEnable(ServerStartingEvent event) {
        // Initialize and Start the Timer
        Timer timer = new Timer();

        // Initialize Game
        ServerUtil.initialize(this, event.getGame());

        // Load Config
        ConfigManager.initialize();

        // Initialize Classes
        initializeClasses();

        // Register Events
        registerEvents();

        // Setup Commands
        setupCommands();

        // Add All Players To Info Config
        if (MainType.INFO_ADD_NEW_PLAYERS.getBoolean()) {
            for (Player player : ServerUtil.getGame().getServer().getOnlinePlayers()) {
                if (!ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig().getNode("users." + player.getUniqueId().toString()).getBoolean()) {
                    Writer.addBase(player.getUniqueId().toString(), InfoType.USER);
                }
            }
        }

        // Stop the Timer
        timer.stop();

        // Calculate Startup Timer
        long diff = timer.difference();

        MessageUtil.log("[MChat] MChat v" + VersionUtil.VERSION + " is enabled! [" + diff + "ms]");
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent event) {
        // Initialize and Start the Timer
        Timer timer = new Timer();

        event.getGame().getSyncScheduler().getScheduledTasks().clear();

        // Unload Config
        ConfigManager.unload();

        // Stop the Timer
        timer.stop();

        // Calculate Shutdown Timer
        long diff = timer.difference();

        MessageUtil.log("[MChat] MChat v" + VersionUtil.VERSION + " is disabled! [" + diff + "ms]");
    }

    private void registerEvents() {
        if (!MainType.MCHAT_API_ONLY.getBoolean()) {
            EventManager mn = ServerUtil.getGame().getEventManager();

            mn.register(this, new ChatListener());
            mn.register(this, new CommandListener());
            mn.register(this, new PlayerListener(this));
        }
    }

    private void setupCommands() {
        regCommands(new MChatCommand(), "mchat");

        regCommands(new InfoAlterCommand("mchatuser", InfoType.USER), "mchatuser", "muser");
        regCommands(new InfoAlterCommand("mchatgroup", InfoType.GROUP), "mchatgroup", "mgroup");

        regCommands(new MeCommand(), "mchatme");
    }

    private void regCommands(CommandCallable callable, String... aliases) {
        ServerUtil.getGame().getCommandDispatcher().register(this, callable, aliases);
    }

    private void initializeClasses() {
        API.initialize();
        VariableManager.initialize();
    }
}