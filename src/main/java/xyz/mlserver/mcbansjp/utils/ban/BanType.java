package xyz.mlserver.mcbansjp.utils.ban;

public enum BanType {

    GRIEFING("Griefing"),
    HACKING("Hacking"),
    OTHER("Other")
    ;

    private final String reason;

    BanType(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
