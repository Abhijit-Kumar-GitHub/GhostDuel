package io.github.abhijit_kumar_github;

public class GameConfig {

    // Virtual screen dimensions
    public static final float WORLD_WIDTH = 1280;
    public static final float WORLD_HEIGHT = 720;

    // Road dimensions and boundaries
    public static final float ROAD_WIDTH = 400; // The width of the playable road area
    public static final float ROAD_LEFT_BOUNDARY = (WORLD_WIDTH - ROAD_WIDTH) / 2;
    public static final float ROAD_RIGHT_BOUNDARY = ROAD_LEFT_BOUNDARY + ROAD_WIDTH - Player.WIDTH;

}
