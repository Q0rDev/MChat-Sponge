package ca.q0r.sponge.mchat.config.censor;

import ca.q0r.sponge.mchat.config.Config;

import java.io.File;

public class CensorConfig extends Config {
    public CensorConfig() {
        super(new File("plugins/MChat/censor.yml"));
    }

    public void loadDefaults() {
        if (!file.exists()) {
            set("fuck", "fawg");
            set("cunt", "punt");
            set("shit", "feces");
            set("dick", "74RG3 P3N1S");
            set("miracleman", "MiracleM4n");
            set("dretax", "DreTax");

            save();
        }
    }
}