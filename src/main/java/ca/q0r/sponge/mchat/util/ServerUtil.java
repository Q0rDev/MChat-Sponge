package ca.q0r.sponge.mchat.util;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;

import java.util.UUID;

public class ServerUtil {
    private static Game game;

    public static void initialize(Game instance) {
        game = instance;
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
        return getServer().getPlayer(uuid).orNull();
    }
}