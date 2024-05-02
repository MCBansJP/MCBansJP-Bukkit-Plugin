package xyz.mlserver.mcbansjp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class CustomConfiguration {
    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    /**
     * Create config.yml
     * @param plugin {@link Plugin}
     */
    public CustomConfiguration(Plugin plugin) {
        this(plugin, "config.yml");
    }

    /**
     * 任意の名前.ymlを生成する
     * @param plugin {@link Plugin}
     * @param fileName {@link String} 任意のファイル名前
     */
    public CustomConfiguration(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    /**
     * ワールドフォルダの中に任意の名前.ymlを生成する
     * @param plugin {@link Plugin}
     * @param fileName {@link String} 任意のファイル名前
     * @param worldName {@link String} ワールド名
     * @deprecated ワールドフォルダーに保存すると不安定になる
     */
    public CustomConfiguration(Plugin plugin, String fileName, String worldName) {
        if (Bukkit.getWorld(worldName) == null) {
            this.plugin = plugin;
            this.file = fileName;
            configFile = new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder() + file);
        } else {
            this.plugin = plugin;
            this.file = fileName;
            configFile = new File(Bukkit.getWorld(worldName).getWorldFolder() + file);
        }
    }

    /**
     * New Save Configuration File
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(file, false);
        }
    }

    /**
     * Reload Configuration
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    /**
     * Get Configuration File
     * @return {@link FileConfiguration}
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * Save Configuration File
     */
    public void saveConfig() {
        if (config == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    /**
     * Clear Configuration File
     */
    public void clear() {
        if (config == null) reloadConfig();
        for (String key : config.getConfigurationSection("").getKeys(false)) config.set(key, null);
        saveConfig();
    }

    /**
     * Save {@link Location} to {@link FileConfiguration}
     * @param path {@link String}
     * @param loc {@link Location}
     */
    public void setLocation(String path, Location loc) {
        Location tempLoc = loc.clone();
        if (config == null) reloadConfig();
        try {
            config.set(path + ".W", tempLoc.getWorld().getName());
            config.set(path + ".X", tempLoc.getX());
            config.set(path + ".Y", tempLoc.getY());
            config.set(path + ".Z", tempLoc.getZ());
            config.set(path + ".Yaw", String.valueOf(tempLoc.getYaw()));
            config.set(path + ".Pitch", String.valueOf(tempLoc.getPitch()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get {@link Location} from {@link FileConfiguration}
     * @param path {@link String}
     * @return {@link Location}
     */
    public Location getLocation(String path) {
        if (config == null) reloadConfig();
        if (config.getString(path + ".W") == null) return null;
        if (Bukkit.getWorld(config.getString(path + ".W")) == null) return null;
        if (config.get(path + ".Yaw") == null && config.get(path + ".Pitch") == null) {
            return new Location(
                    Bukkit.getWorld(config.getString(path + ".W")),
                    config.getDouble(path + ".X"),
                    config.getDouble(path + ".Y"),
                    config.getDouble(path + ".Z")
            ).clone();
        } else {
            return new Location(
                    Bukkit.getWorld(config.getString(path + ".W")),
                    config.getDouble(path + ".X"),
                    config.getDouble(path + ".Y"),
                    config.getDouble(path + ".Z"),
                    Float.parseFloat(config.getString(path + ".Yaw")),
                    Float.parseFloat(config.getString(path + ".Pitch"))
            ).clone();
        }
    }
}