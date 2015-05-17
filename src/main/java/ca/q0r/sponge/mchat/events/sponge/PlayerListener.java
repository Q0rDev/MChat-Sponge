package ca.q0r.sponge.mchat.events.sponge;

import ca.q0r.sponge.mchat.MChat;
import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Parser;
import ca.q0r.sponge.mchat.api.Writer;
import ca.q0r.sponge.mchat.config.ConfigManager;
import ca.q0r.sponge.mchat.config.ConfigType;
import ca.q0r.sponge.mchat.config.main.MainType;
import ca.q0r.sponge.mchat.types.EventType;
import ca.q0r.sponge.mchat.types.InfoType;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import org.spongepowered.api.data.manipulators.DisplayNameData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.text.Texts;

import java.util.UUID;

public class PlayerListener {
    MChat plugin;

    public PlayerListener(MChat instance) {
        plugin = instance;
    }

    @Subscribe(order = Order.EARLY)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        String world = player.getWorld().getName();
        UUID uuid = player.getUniqueId();

        if (MainType.INFO_ADD_NEW_PLAYERS.getBoolean()) {
            if (ConfigManager.getConfig(ConfigType.INFO_HOCON).getConfig().getNode("users." + uuid.toString()).isVirtual()) {
                Writer.addBase(uuid.toString(), InfoType.USER);
            }
        }


        if (MainType.MCHAT_ALTER_LIST.getBoolean()) {
            event.getGame().getSyncScheduler().runTaskAfter(plugin, new Runnable() {
                public void run() {
                    setCustomName(player);
                }
            }, 20L);
        }

        if (MainType.MCHAT_ALTER_EVENTS.getBoolean()) {
            if (MainType.SUPPRESS_USE_JOIN.getBoolean()) {
                suppressEventMessage(Parser.parseEvent(uuid, world, EventType.JOIN), "mchat.suppress.join", "mchat.bypass.suppress.join", MainType.SUPPRESS_MAX_JOIN.getInteger());
                event.setJoinMessage(Texts.of());
            } else {
                event.setJoinMessage(Texts.of(Parser.parseEvent(uuid, world, EventType.JOIN)));
            }
        }
    }

    //TODO Fix Kick Event
    /*@Subscribe(order = Order.FIRST)
    public void onPlayerKick(PlayerKickEvent event) {
        if (!MainType.MCHAT_ALTER_EVENTS.getBoolean()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        UUID uuid = event.getPlayer().getUniqueId();
        String world = event.getPlayer().getWorld().getName();
        String msg = event.getLeaveMessage();

        if (msg == null) {
            return;
        }

        String reason = event.getReason();

        String kickMsg = Parser.parseEvent(uuid, world, EventType.KICK).replace(IndicatorType.MISC_VAR.getValue() + "reason", reason).replace(IndicatorType.MISC_VAR.getValue() + "r", reason);

        if (MainType.SUPPRESS_USE_KICK.getBoolean()) {
            suppressEventMessage(kickMsg, "mchat.suppress.kick", "mchat.bypass.suppress.kick", MainType.SUPPRESS_MAX_KICK.getInteger());
            event.setLeaveMessage(null);
        } else {
            event.setLeaveMessage(kickMsg);
        }
    }*/

    @Subscribe(order = Order.EARLY)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String world = event.getPlayer().getWorld().getName();

        if (!MainType.MCHAT_ALTER_EVENTS.getBoolean()) {
            return;
        }

        if (MainType.SUPPRESS_USE_QUIT.getBoolean()) {
            suppressEventMessage(Parser.parseEvent(uuid, world, EventType.QUIT), "mchat.suppress.quit", "mchat.bypass.suppress.quit", MainType.SUPPRESS_MAX_QUIT.getInteger());
            event.setQuitMessage(Texts.of());
        } else {
            event.setQuitMessage(Texts.of(Parser.parseEvent(uuid, world, EventType.QUIT)));
        }
    }

    private void suppressEventMessage(String format, String permNode, String overrideNode, Integer max) {
        for (Player player : ServerUtil.getServer().getOnlinePlayers()) {
            if (API.checkPermissions(player.getUniqueId(), overrideNode)) {
                player.sendMessage(Texts.of(format));
                continue;
            }

            if (!(ServerUtil.getServer().getOnlinePlayers().size() > max)) {
                if (!API.checkPermissions(player.getUniqueId(), permNode)) {
                    player.sendMessage(Texts.of(format));
                }
            }
        }

        MessageUtil.log(format);
    }

    private void setCustomName(Player player) {
        String listName = Parser.parseCustomName(player.getUniqueId(), player.getWorld().getName());

        /*try {
            if (listName.length() > 15) {
                listName = listName.substring(0, 16);
                player.setCustomName(listName);
            }

            player.setCustomName(listName);
        } catch (Exception ignored) {
        }*/

        player.getData(DisplayNameData.class).get().setDisplayName(Texts.of(listName));
    }
}