package ca.q0r.sponge.mchat.commands;

import ca.q0r.sponge.mchat.events.custom.MeEvent;
import ca.q0r.sponge.mchat.util.CommandUtil;
import ca.q0r.sponge.mchat.util.MessageUtil;
import ca.q0r.sponge.mchat.util.ServerUtil;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

@NonnullByDefault
public class MeCommand implements CommandCallable {
    public MeCommand() {
    }

    public Optional<CommandResult> process(CommandSource source, String raw) throws CommandException {
        String[] args = raw.split(" ");

        /*if (!list.contains("mchatme")) {
            return Optional.of(CommandResult.success());
        }*/

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
                    ServerUtil.getGame().getServer().broadcastMessage(Texts.of(event.getFormat()));
                }

                return Optional.of(CommandResult.success());
            } else {
                String senderName = "Console";
                ServerUtil.getGame().getServer().broadcastMessage(Texts.of("* " + senderName + " " + message));
                MessageUtil.log("* " + senderName + " " + message);
                return Optional.of(CommandResult.success());
            }
        }

        return Optional.of(CommandResult.empty());
    }

    public boolean testPermission(CommandSource source) {
        return CommandUtil.hasCommandPerm(source, "mchat.me");
    }

    public Optional<Text> getShortDescription(CommandSource commandSource) {
        return Optional.of(Texts.of(new Object[]{"MChat /me Implementation"}));
    }

    public Optional<Text> getHelp(CommandSource commandSource) {
        return Optional.absent();
    }

    public Text getUsage(CommandSource commandSource) {
        return Texts.of("/<command> [Message] - Displays message.");
    }

    public List<String> getSuggestions(CommandSource source, String raw) throws CommandException {
        return new ArrayList<String>();
    }
}