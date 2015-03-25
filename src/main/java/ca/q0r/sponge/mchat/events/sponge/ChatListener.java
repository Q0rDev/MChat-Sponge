package ca.q0r.sponge.mchat.events.sponge;

import ca.q0r.sponge.mchat.api.API;
import ca.q0r.sponge.mchat.api.Parser;
import ca.q0r.sponge.mchat.config.locale.LocaleType;
import ca.q0r.sponge.mchat.config.main.MainType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;

import java.util.UUID;

public class ChatListener {
    public ChatListener() {
    }

    @Subscribe(order = Order.EARLY)
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        String world = player.getWorld().getName();
        Text text = event.getMessage();

        System.out.println(text);
        System.out.println(text.toString());

        if (event.getMessage() instanceof Text.Literal) {
            Text.Literal msg = (Text.Literal) text;

            Text.Literal eventFormat = Texts.of(Parser.parseChatMessage(player.getUniqueId(), world, msg.getContent()));

            if (MainType.MCHAT_ALTER_LIST.getBoolean()) {
                setCustomName(player);
            }

            // Chat Distance Stuff
            if (MainType.MCHAT_CHAT_DISTANCE.getDouble() > 0) {
                for (Player players : event.getGame().getServer().get().getOnlinePlayers()) {
                    if (players.getWorld() != player.getWorld()
                            || players.getLocation().getPosition().distance(player.getLocation().getPosition()) > MainType.MCHAT_CHAT_DISTANCE.getDouble()) {
                        if (isSpy(players.getUniqueId(), players.getWorld().getName())) {
                            eventFormat = Texts.of(eventFormat.getContent().replace(LocaleType.FORMAT_LOCAL.getVal(), LocaleType.FORMAT_FORWARD.getVal()));
                            players.sendMessage(eventFormat);
                        }

                        //TODO Fix Recipient removal
                        //event.getRecipients().remove(players);
                    }
                }
            }

            event.setMessage(eventFormat);
        }
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