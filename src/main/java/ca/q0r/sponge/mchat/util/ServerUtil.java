package ca.q0r.sponge.mchat.util;

import ca.q0r.sponge.mchat.MChat;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.config.ConfigService;

import java.io.File;
import java.util.UUID;

public class ServerUtil {
    private static MChat mchat;
    private static Game game;

    public static void initialize(MChat plugin, Game instance) {
        mchat = plugin;
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

    public static File getConfigDir() {
        ConfigService service = ServerUtil.getGame().getServiceManager().provide(ConfigService.class).orNull();

        return service != null ? service.getSharedConfig(mchat).getDirectory() : null;
    }
}