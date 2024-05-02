package xyz.mlserver.mcbansjp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlserver.mcbansjp.commands.GlobalBanCmd;
import xyz.mlserver.mcbansjp.commands.LocalBanCmd;
import xyz.mlserver.mcbansjp.commands.TempBanCmd;
import xyz.mlserver.mcbansjp.listeners.BukkitPlayerJoinListener;
import xyz.mlserver.mcbansjp.utils.CustomConfiguration;

public final class MCBansJP extends JavaPlugin {

    private static JavaPlugin plugin;

    public static CustomConfiguration config;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
