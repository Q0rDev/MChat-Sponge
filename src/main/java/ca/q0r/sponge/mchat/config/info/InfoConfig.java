package ca.q0r.sponge.mchat.config.info;

import ca.q0r.sponge.mchat.config.Config;

public class InfoConfig extends Config {
    public InfoConfig() {
        super("info.conf");
    }

    public void loadDefaults() {
        if (config.getNode("users").isVirtual()) {
            set("users.MiracleM4n.group", "admin");
            set("users.MiracleM4n.worlds.DtK.prefix", "");
            set("users.MiracleM4n.info.suffix", "");
            set("users.MiracleM4n.info.prefix", "");
        }

        if (config.getNode("groups").isVirtual()) {
            set("groups.admin.worlds.DtK.prefix", "");
            set("groups.admin.info.prefix", "");
            set("groups.admin.info.suffix", "");
            set("groups.admin.info.custVar", "");
        }

        if (config.getNode("groupnames").isVirtual()) {
            set("groupnames.admin", "[a]");
            set("groupnames.sadmin", "[sa]");
            set("groupnames.jadmin", "[ja]");
            set("groupnames.member", "[m]");
        }

        if (config.getNode("worldnames").isVirtual()) {
            set("worldnames.D3GN", "[D]");
            set("worldnames.DtK", "[DtK]");
            set("worldnames.Nether", "[N]");
            set("worldnames.Hello", "[H]");
        }

        if (config.getNode("mname").isVirtual()) {
            set("mname.MiracleM4n", "M1r4c13M4n");
            set("mname.Jessica_RS", "M1r4c13M4n's Woman");
        }

        save();
    }
}