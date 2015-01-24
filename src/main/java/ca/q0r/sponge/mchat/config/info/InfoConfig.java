package ca.q0r.sponge.mchat.config.info;

import ca.q0r.sponge.mchat.config.Config;

import java.io.File;

public class InfoConfig extends Config {
    public InfoConfig() {
        super(new File("plugins/MChat/info.conf"));
    }

    public void loadDefaults() {
        if (config.hasPath("users")) {
            set("users.MiracleM4n.group", "admin");
            set("users.MiracleM4n.worlds.DtK.prefix", "");
            set("users.MiracleM4n.info.suffix", "");
            set("users.MiracleM4n.info.prefix", "");
        }

        if (!config.hasPath("groups")) {
            set("groups.admin.worlds.DtK.prefix", "");
            set("groups.admin.info.prefix", "");
            set("groups.admin.info.suffix", "");
            set("groups.admin.info.custVar", "");
        }

        if (!config.hasPath("groupnames")) {
            set("groupnames.admin", "[a]");
            set("groupnames.sadmin", "[sa]");
            set("groupnames.jadmin", "[ja]");
            set("groupnames.member", "[m]");
        }

        if (!config.hasPath("worldnames")) {
            set("worldnames.D3GN", "[D]");
            set("worldnames.DtK", "[DtK]");
            set("worldnames.Nether", "[N]");
            set("worldnames.Hello", "[H]");
        }

        if (!config.hasPath("mname")) {
            set("mname.MiracleM4n", "M1r4c13M4n");
            set("mname.Jessica_RS", "M1r4c13M4n's Woman");
        }

        save();
    }
}