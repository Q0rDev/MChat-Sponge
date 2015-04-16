package ca.q0r.sponge.mchat.config.info;

import ca.q0r.sponge.mchat.config.Config;

public class InfoConfig extends Config {
    public InfoConfig() {
        super("info.conf");
    }

    public void loadDefaults() {
        if (config.getNode("users").isVirtual()) {
            set(new Object[]{"users", "MiracleM4n", "group"}, "admin");
            set(new Object[]{"users", "MiracleM4n", "worlds", "DtK", "prefix"}, "");
            set(new Object[]{"users", "MiracleM4n", "info", "suffix"}, "");
            set(new Object[]{"users", "MiracleM4n", "info", "prefix"}, "");
        }

        if (config.getNode("groups").isVirtual()) {
            set(new Object[]{"groups", "admin", "worlds", "DtK", "prefix"}, "");
            set(new Object[]{"groups", "admin", "info", "prefix"}, "");
            set(new Object[]{"groups", "admin", "info", "suffix"}, "");
            set(new Object[]{"groups", "admin", "info", "custVar"}, "");
        }

        if (config.getNode("groupnames").isVirtual()) {
            set(new Object[]{"groupnames", "admin"}, "[a]");
            set(new Object[]{"groupnames", "sadmin"}, "[sa]");
            set(new Object[]{"groupnames", "jadmin"}, "[ja]");
            set(new Object[]{"groupnames", "member"}, "[m]");
        }

        if (config.getNode("worldnames").isVirtual()) {
            set(new Object[]{"worldnames", "D3GN"}, "[D]");
            set(new Object[]{"worldnames", "DtK"}, "[DtK]");
            set(new Object[]{"worldnames", "Nether"}, "[N]");
            set(new Object[]{"worldnames", "Hello"}, "[H]");
        }

        if (config.getNode("mname").isVirtual()) {
            set(new Object[]{"mname", "MiracleM4n"}, "M1r4c13M4n");
            set(new Object[]{"mname", "Jessica_RS"}, "M1r4c13M4n's Woman");
        }

        save();
    }
}