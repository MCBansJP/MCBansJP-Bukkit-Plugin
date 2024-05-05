package xyz.mlserver.mcbansjp.utils.bukkit;

import xyz.mlserver.mcbansjp.MCBansJP;
import xyz.mlserver.mcbansjp.utils.CustomConfiguration;

public class ConfigAPI {

    public static void load(CustomConfiguration config) {
        MCBansJP.updateCheck = config.getConfig().getBoolean("update-checker", true);
    }


}
