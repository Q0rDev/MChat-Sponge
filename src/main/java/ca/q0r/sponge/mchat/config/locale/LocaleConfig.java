package ca.q0r.sponge.mchat.config.locale;

import ca.q0r.sponge.mchat.config.Config;

public class LocaleConfig extends Config {
    public LocaleConfig() {
        super("locale.conf");
    }

    public void loadDefaults() {
        checkNode(new Object[]{"format", "forward"}, "[F]");
        checkNode(new Object[]{"format", "local"}, "[L]");
        checkNode(new Object[]{"format", "spy"}, "[Spy]");

        checkNode(new Object[]{"format", "chat"}, "+p+dn+s&f: +m");
        checkNode(new Object[]{"format", "date"}, "HH:mm:ss");
        checkNode(new Object[]{"format", "name"}, "+p+dn+s&e");
        checkNode(new Object[]{"format", "custom-name"}, "+p+dn+s");
        checkNode(new Object[]{"format", "list-cmd"}, "+p+dn+s");
        checkNode(new Object[]{"format", "me"}, "* +p+dn+s&e +m");

        checkNode(new Object[]{"message", "general", "no-perms"}, "You do not have '%permission'.");
        checkNode(new Object[]{"message", "info", "alteration"}, "Info Alteration Successful.");
        checkNode(new Object[]{"message", "player", "still-afk"}, "You are still AFK.");
        checkNode(new Object[]{"message", "event", "join"}, "%player has joined the game.");
        checkNode(new Object[]{"message", "event", "leave"}, "%player has left the game.");
        checkNode(new Object[]{"message", "event", "kick"}, "%player has been kicked from the game. [ %reason ]");
        checkNode(new Object[]{"message", "heroes", "is-master"}, "The Great");
        checkNode(new Object[]{"message", "heroes", "not-master"}, "The Squire");

        save();
    }
}