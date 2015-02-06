package ca.q0r.sponge.mchat.config.locale;

import ca.q0r.sponge.mchat.config.Config;

public class LocaleConfig extends Config {
    public LocaleConfig() {
        super("locale.conf");
    }

    public void loadDefaults() {
        checkOption("format.forward", "[F]");
        checkOption("format.local", "[L]");
        checkOption("format.spy", "[Spy]");

        checkOption("format.chat", "+p+dn+s&f: +m");
        checkOption("format.date", "HH:mm:ss");
        checkOption("format.name", "+p+dn+s&e");
        checkOption("format.custom-name", "+p+dn+s");
        checkOption("format.list-cmd", "+p+dn+s");
        checkOption("format.me", "* +p+dn+s&e +m");

        checkOption("message.general.no-perms", "You do not have '%permission'.");
        checkOption("message.info.alteration", "Info Alteration Successful.");
        checkOption("message.player.still-afk", "You are still AFK.");
        checkOption("message.event.join", "%player has joined the game.");
        checkOption("message.event.leave", "%player has left the game.");
        checkOption("message.event.kick", "%player has been kicked from the game. [ %reason ]");
        checkOption("message.heroes.is-master", "The Great");
        checkOption("message.heroes.not-master", "The Squire");

        save();
    }
}