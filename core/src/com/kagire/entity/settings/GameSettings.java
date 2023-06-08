package com.kagire.entity.settings;

public class GameSettings {

    public MapSettings mapSettings;
    public GameRules gameRules;

    public static GameSettings defaultSettings() {
        var settings = new GameSettings();
        settings.mapSettings = new MapSettings();
        settings.gameRules = new GameRules();
        settings.mapSettings.mapHeight = 5;
        settings.mapSettings.mapWidth = 5;
        settings.gameRules.gameSpeed = 3;
        return settings;
    }

    public static class MapSettings {
        public int mapWidth;
        public int mapHeight;
    }

    public static class GameRules {
        public int gameSpeed; // <-- tiles/sec
    }
}
