package com.trova_app.enums;

public enum Genre {
    ROCK_ARGENTINO("Rock Argentino"),
    TANGO("Tango"),
    JAZZ("Jazz"),
    MUSICA_POPULAR_ARGENTINA("Música Popular Argentina"),
    MUSICA_POPULAR_BRASILENA("Música Popular Brasilena"),
    MUSICA_INSTRUMENTAL("Música Instrumental"),
    HUMOR_MUSICAL("Humor Musical"),
    CHANSON_FRANCESA("Chanson Francesa"),
    TEATRO_MUSICAL("Teatro Musical"),
    FOLCLORE("Folclore"),
    CONTEMPORANEA("Contemporánea"),
    CLASICA("Clásica");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
