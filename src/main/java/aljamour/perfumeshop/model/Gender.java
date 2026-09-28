package aljamour.perfumeshop.model;

public enum Gender {
    MEN("Herre"),
    WOMEN("Kvinde"),
    UNISEX("Unisex");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
