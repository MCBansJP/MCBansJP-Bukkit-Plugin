package xyz.mlserver.mcbansjp;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private URL checkURL;

    private String currentVersion;
    private String availableVersion;

    private String freeDownloadLink = null;
    private String plusDownloadLink = null;
    private boolean sendOpMessage = false;
    private String donationLink = null;

    private UpdateResult result = UpdateResult.FAIL_SPIGOT;

    public enum UpdateResult {
        NO_UPDATE,
        FAIL_SPIGOT,
        UPDATE_AVAILABLE
    }

    public UpdateChecker(JavaPlugin plugin, Integer resourceId) {
        this.plugin = plugin;
        this.currentVersion = regex(this.plugin.getDescription().getVersion());

        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        } catch (MalformedURLException e) {
            result = UpdateResult.FAIL_SPIGOT;
            return;
        }

        run();
    }

    private void run() {
        URLConnection con = null;
        try {
            con = checkURL.openConnection();
        } catch (IOException e1) {
            result = UpdateResult.FAIL_SPIGOT;
            return;
        }

        try {
            availableVersion = regex(new BufferedReader(new InputStreamReader(con.getInputStream())).readLine());
        } catch (IOException e) {
            result = UpdateResult.FAIL_SPIGOT;
            return;
        }

        if (availableVersion.isEmpty()) {
            result = UpdateResult.FAIL_SPIGOT;
            return;
        } else if (availableVersion.equalsIgnoreCase(currentVersion)) {
            result = UpdateResult.NO_UPDATE;
            return;
        } else if (!availableVersion.equalsIgnoreCase(currentVersion)) {
            result = UpdateResult.UPDATE_AVAILABLE;
            return;
        }

        result = UpdateResult.FAIL_SPIGOT;

    }

    public UpdateResult getResult() {
        return this.result;
    }

    public String getVersion() {
        return regex(this.availableVersion);
    }

    private String regex(String version) {
        return version.replaceAll("[^0-9.]", "");
    }

    public String getFreeDownloadLink() {
        return freeDownloadLink;
    }

    public String getPlusDownloadLink() {
        return plusDownloadLink;
    }

    public String getAvailableVersion() {
        return availableVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean isSendOpMessage() {
        return sendOpMessage;
    }

    public String getDonationLink() {
        return donationLink;
    }

    public UpdateChecker setFreeDownloadLink(String freeDownloadLink) {
        this.freeDownloadLink = freeDownloadLink;
        return this;
    }

    public UpdateChecker setPlusDownloadLink(String plusDownloadLink) {
        this.plusDownloadLink = plusDownloadLink;
        return this;
    }

    public UpdateChecker setSendOpMessage(boolean sendOpMessage) {
        this.sendOpMessage = sendOpMessage;
        return this;
    }

    public UpdateChecker setDonationLink(String donationLink) {
        this.donationLink = donationLink;
        return this;
    }

}