package ca.q0r.sponge.mchat.config.info;

import ca.q0r.sponge.mchat.config.Config;

public class InfoConfig extends Config {
    public InfoConfig() {
        super("info.conf");
    }

    public void loadDefaults() {
        if (config.getNode("users").isVirtual()) {
            set(new String[] {"users", "MiracleM4n", "group"}, "admin");
            set(new String[] {"users", "MiracleM4n", "worlds", "DtK", "prefix"}, "");
            set(new String[] {"users", "MiracleM4n", "info", "suffix"}, "");
            set(new String[] {"users", "MiracleM4n", "info", "prefix"}, "");
        }

        if (config.getNode("groups").isVirtual()) {
            set(new String[] {"groups", "admin", "worlds", "DtK", "prefix"}, "");
            set(new String[] {"groups", "admin", "info", "prefix"}, "");
            set(new String[] {"groups", "admin", "info", "suffix"}, "");
            set(new String[] {"groups", "admin", "info", "custVar"}, "");
        }

        if (config.getNode("groupnames").isVirtual()) {
            set(new String[] {"groupnames", "admin"}, "[a]");
            set(new String[] {"groupnames", "sadmin"}, "[sa]");
            set(new String[] {"groupnames", "jadmin"}, "[ja]");
            set(new String[] {"groupnames", "member"}, "[m]");
        }

        if (config.getNode("worldnames").isVirtual()) {
            set(new String[] {"worldnames", "D3GN"}, "[D]");
            set(new String[] {"worldnames", "DtK"}, "[DtK]");
            set(new String[] {"worldnames", "Nether"}, "[N]");
            set(new String[] {"worldnames", "Hello"}, "[H]");
        }

        if (config.getNode("mname").isVirtual()) {
            set(new String[] {"mname", "MiracleM4n"}, "M1r4c13M4n");
            set(new String[] {"mname", "Jessica_RS"}, "M1r4c13M4n's Woman");
        }

        save();
    }
}