package xyz.mlserver.mcbansjp.utils.ban;

public enum BanType {

    LOCAL("local"),
    GLOBAL("global"),
    TEMP("temp");

    private final String type;

    BanType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
