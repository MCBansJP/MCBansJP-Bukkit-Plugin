package xyz.mlserver.mcbansjp.utils.ban;

public enum BanType {

    GRIEFING("Griefing", "griefing", "g"),
    HACKING("Hacking", "hacking", "h"),
    Local_Rule("Local Rule", "local_rule", "h"),
    OTHER("Other", "other", "o")
    ;

    private final String reason;
    private final String command_type;
    private final String shortcutCode;

    BanType(String reason, String command_type, String shortcutCode) {
        this.reason = reason;
        this.command_type = command_type;
        this.shortcutCode = shortcutCode;
    }

    public String getReason() {
        return reason;
    }

    public String getCommandType() {
        return command_type;
    }

    public String getShortcutCode() {
        return shortcutCode;
    }

}
