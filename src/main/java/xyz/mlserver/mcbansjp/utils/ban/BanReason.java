package xyz.mlserver.mcbansjp.utils.ban;

public enum BanReason {

    GRIEFING("Griefing"),
    ;

    private final String reason;

    BanReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
