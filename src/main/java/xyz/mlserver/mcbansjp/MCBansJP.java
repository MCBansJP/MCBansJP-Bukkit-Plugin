package xyz.mlserver.mcbansjp;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlserver.mcbansjp.commands.cmds.GlobalBanCmd;
import xyz.mlserver.mcbansjp.commands.cmds.LocalBanCmd;
import xyz.mlserver.mcbansjp.commands.cmds.TempBanCmd;
import xyz.mlserver.mcbansjp.listeners.BukkitPlayerJoinListener;
import xyz.mlserver.mcbansjp.utils.CustomConfiguration;
import xyz.mlserver.mcbansjp.utils.bukkit.ConfigAPI;

public final class MCBansJP extends JavaPlugin {

    private static JavaPlugin plugin;

    public static CustomConfiguration config;

    public static boolean updateCheck;

    private static UpdateChecker updateChecker = null;

    public static UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        MCBansJP.debug = debug;
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = new CustomConfiguration(this);
        config.saveDefaultConfig();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new BukkitPlayerJoinListener(), this);

        getCommand("localban").setExecutor(new LocalBanCmd());
        getCommand("tempban").setExecutor(new TempBanCmd());
        getCommand("globalban").setExecutor(new GlobalBanCmd());

        CustomConfiguration config = new CustomConfiguration(this);
        config.saveDefaultConfig();

        ConfigAPI.load(config);

        checkUpdate(updateCheck);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void checkUpdate(boolean enabled) {
        if (!enabled)
            return;

        plugin.getLogger().info("Checking for updates...");

        updateChecker = new UpdateChecker(plugin, 116561)
                .setFreeDownloadLink("https://www.spigotmc.org/resources/simplegamemode.116561/")
                .setDonationLink("https://github.com/sponsors/Monster2408")
        ;

        final UpdateChecker.UpdateResult result = getUpdateChecker().getResult();

        switch (result) {
            case FAIL_SPIGOT:
                plugin.getLogger().warning("Could not contact Spigot.");
                break;
            case UPDATE_AVAILABLE:
                plugin.getLogger().warning("*********************************************************************************");
                plugin.getLogger().warning("* An update to SimpleGameMode is available!");
                plugin.getLogger().warning("* Your Version: [" + getUpdateChecker().getCurrentVersion() + "]");
                plugin.getLogger().warning("* Latest Version: [" + getUpdateChecker().getAvailableVersion() + "]");
                plugin.getLogger().warning("*");
                plugin.getLogger().warning("* Please update to the newest version.");
                if (getUpdateChecker().getPlusDownloadLink() != null) {
                    plugin.getLogger().warning("*");
                    plugin.getLogger().warning("* Download(Plus)");
                    plugin.getLogger().warning("*   " + getUpdateChecker().getPlusDownloadLink());
                }
                if (getUpdateChecker().getFreeDownloadLink() != null) {
                    plugin.getLogger().warning("*");
                    plugin.getLogger().warning("* Download(Free)");
                    plugin.getLogger().warning("*   " + getUpdateChecker().getFreeDownloadLink());
                }
                if (getUpdateChecker().getDonationLink() != null) {
                    plugin.getLogger().warning("*");
                    plugin.getLogger().warning("* Donate");
                    plugin.getLogger().warning("*   " + getUpdateChecker().getDonationLink());
                }
                plugin.getLogger().warning("*");
                plugin.getLogger().warning("*********************************************************************************");
                break;
            case NO_UPDATE:
                plugin.getLogger().info("You are running the latest version.");
                break;
            default:
                break;
        }
    }
}
