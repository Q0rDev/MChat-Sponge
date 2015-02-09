package ca.q0r.sponge.mchat.config.locale;

import ca.q0r.sponge.mchat.config.Config;

public class LocaleConfig extends Config {
    public LocaleConfig() {
        super("locale.conf");
    }

    public void loadDefaults() {
        checkNode(new String[] {"format", "forward"}, "[F]");
        checkNode(new String[] {"format", "local"}, "[L]");
        checkNode(new String[] {"format", "spy"}, "[Spy]");

        checkNode(new String[] {"format", "chat"}, "+p+dn+s&f: +m");
        checkNode(new String[] {"format", "date"}, "HH:mm:ss");
        checkNode(new String[] {"format", "name"}, "+p+dn+s&e");
        checkNode(new String[] {"format", "custom-name"}, "+p+dn+s");
        checkNode(new String[] {"format", "list-cmd"}, "+p+dn+s");
        checkNode(new String[] {"format", "me"}, "* +p+dn+s&e +m");

        checkNode(new String[] {"message", "general", "no-perms"}, "You do not have '%permission'.");
        checkNode(new String[] {"message", "info", "alteration"}, "Info Alteration Successful.");
        checkNode(new String[] {"message", "player", "still-afk"}, "You are still AFK.");
        checkNode(new String[] {"message", "event", "join"}, "%player has joined the game.");
        checkNode(new String[] {"message", "event", "leave"}, "%player has left the game.");
        checkNode(new String[] {"message", "event", "kick"}, "%player has been kicked from the game. [ %reason ]");
        checkNode(new String[] {"message", "heroes", "is-master"}, "The Great");
        checkNode(new String[] {"message", "heroes", "not-master"}, "The Squire");

        save();
    }
}