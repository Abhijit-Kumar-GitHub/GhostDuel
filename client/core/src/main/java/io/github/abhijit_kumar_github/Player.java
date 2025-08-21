package io.github.abhijit_kumar_github;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    // Constants for the player's properties
    public static final float WIDTH = 50;
    public static final float HEIGHT = 50;
    public static final float MOVE_SPEED = 300f; // Increased speed for responsiveness

    public final Rectangle bounds; // Represents the player's body
    public final Vector2 velocity; // Only the x-component will be used for movement

    public boolean dematerialized = false;
    public float dematerializeTimer = 0f;
    public static final float DEMATERIALIZE_DURATION = 2.0f; // 2 seconds

    public Player(float x, float y) {
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        this.velocity = new Vector2();
    }

    public void update(float deltaTime) {
        // Update horizontal position
        bounds.x += velocity.x * deltaTime;

        // Constrain the player to the road boundaries
        bounds.x = Math.max(GameConfig.ROAD_LEFT_BOUNDARY, bounds.x); // Prevent moving too far left
        bounds.x = Math.min(GameConfig.ROAD_RIGHT_BOUNDARY, bounds.x); // Prevent moving too far right

        // Handle dematerialize timer
        if (dematerialized) {
            dematerializeTimer -= deltaTime;
            if (dematerializeTimer <= 0) {
                dematerialized = false;
            }
        }
    }

    public void dematerialize() {
        if (!dematerialized) {
            dematerialized = true;
            dematerializeTimer = DEMATERIALIZE_DURATION;
        }
    }
}
