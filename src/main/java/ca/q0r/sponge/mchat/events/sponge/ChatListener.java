package ca.q0r.sponge.mchat.events.sponge;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Parser;
import ca.q0r.sponge.mchat.config.main.MainType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.player.PlayerChatEvent;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;

import java.util.UUID;

public class ChatListener {
    public ChatListener() {
    }


    @Subscribe(order = Order.FIRST)
    public void onPlayerChat(PlayerChatEvent event) {
        //TODO Check if it will be Cancellable
        /*if (event.isCancelled()) {
            return;
        }*/

        Player player = event.getPlayer();

        String world = player.getWorld().getName();
        String msg = event.getMessage();
        String eventFormat = Parser.parseChatMessage(player.getUniqueId(), world, msg);

        if (msg == null) {
            return;
        }

        if (MainType.MCHAT_ALTER_LIST.getBoolean()) {
            setCustomName(player);
        }

        // Chat Distance Stuff
        //TODO Fix Chat Distance
        /*if (MainType.MCHAT_CHAT_DISTANCE.getDouble() > 0) {
            for (Player players : event.getGame().getServer().get().getOnlinePlayers()) {
                if (players.getWorld() != player.getWorld()
                        || players.getLocation().distance(player.getLocation()) > MainType.MCHAT_CHAT_DISTANCE.getDouble()) {
                    if (isSpy(players.getUniqueId(), players.getWorld().getName())) {
                        players.sendMessage(eventFormat.replace(LocaleType.FORMAT_LOCAL.getVal(), LocaleType.FORMAT_FORWARD.getVal()));
                    }

                    event.getRecipients().remove(players);
                }
            }
        }*/

        //TODO FIX MAIN FUNCTION OF MCHAT
        //event.setFormat(eventFormat);
    }

    private Boolean isSpy(UUID uuid, String world) {
        if (API.checkPermissions(uuid, "mchat.spy")) {
            API.getSpying().put(uuid.toString(), true);
            return true;
        }

        API.getSpying().put(uuid.toString(), false);
        return false;
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

        player.setCustomName(listName);
    }
}