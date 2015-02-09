package ca.q0r.sponge.mchat.config.censor;

import ca.q0r.sponge.mchat.config.Config;

import java.util.HashMap;
import java.util.Map;

public class CensorConfig extends Config {
    public CensorConfig() {
        super("censor.conf");
    }

    public void loadDefaults() {
        if (config.getValue() == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            
            map.put("fuck", "fawg");
            map.put("cunt", "punt");
            map.put("shit", "feces");
            map.put("dick", "74RG3 P3N1S");
            map.put("miracleman", "MiracleM4n");
            map.put("dretax", "DreTax");

            config.setValue(map);

            save();
        }
    }
}