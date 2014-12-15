package ca.q0r.sponge.mchat.util;

import ca.q0r.sponge.mchat.MChat;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;

import java.util.UUID;

public class ServerUtil {
    private static Game game;
    private static MChat plugin;

    public static void initialize(Game instance, MChat mchat) {
        game = instance;
        plugin = mchat;

    }

    public static Game getGame() {
        return game;
    }

    public static Server getServer() {
        return game.getServer().get();
    }

    public static Player getPlayer(String find) {
        for (Player player : ServerUtil.getServer().getOnlinePlayers()) {
            if (player.getUniqueId().toString().toLowerCase().startsWith(find.toLowerCase())) {
                return player;
            } else if (player.getName().toLowerCase().startsWith(find.toLowerCase())) {
                return player;
            }
        }

        return null;
    }

    public static Player getPlayer(UUID uuid) {
        return getServer().getPlayer(uuid).get();
    }

    public static MChat getPlugin() {
        return plugin;
    }
}