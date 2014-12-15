package ca.q0r.sponge.mchat.commands;

import ca.q0r.sponge.mchat.events.custom.MeEvent;
import ca.q0r.sponge.mchat.util.CommandUtil;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

@NonnullByDefault
public class MeCommand implements CommandCallable {
    public MeCommand() {
    }

    public boolean call(CommandSource source, String raw, List<String> list) throws CommandException {
        String[] args = raw.split(" ");

        if (!list.contains("mchatme")) {
            return true;
        }

        if (args.length > 0) {
            String message = "";

            for (String arg : args) {
                message += " " + arg;
            }

            message = message.trim();

            if (source instanceof Player) {
                Player player = (Player) source;
                World world = player.getWorld();

                MeEvent event = new MeEvent(player.getUniqueId(), world.getName(), message);

                ServerUtil.getGame().getEventManager().post(event);

                if (!event.isCancelled()) {
                    ServerUtil.getGame().getServer().get().broadcastMessage(Messages.builder(event.getFormat()).build());
                }

                return true;
            } else {
                String senderName = "Console";
                ServerUtil.getGame().getServer().get().broadcastMessage(Messages.builder("* " + senderName + " " + message).build());
                MessageUtil.log("* " + senderName + " " + message);
                return true;
            }
        }

        return false;
    }

    public boolean testPermission(CommandSource source) {
        return CommandUtil.hasCommandPerm(source, "mchat.me");
    }

    public Optional<String> getShortDescription() {
        return Optional.of("MChat /me Implementation");
    }

    public Optional<String> getHelp() {
        return Optional.absent();
    }

    public String getUsage() {
        return "/<command> [Message] - Displays message.";
    }

    public List<String> getSuggestions(CommandSource source, String raw) throws CommandException {
        return Arrays.asList();
    }
}