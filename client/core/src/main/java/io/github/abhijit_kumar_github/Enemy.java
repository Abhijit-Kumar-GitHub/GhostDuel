package io.github.abhijit_kumar_github;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public static final float WIDTH = 40;
    public static final float HEIGHT = 40;
    public static final float MOVE_SPEED = 150f; // Slower than the player

    public final Vector2 position;
    public final Rectangle bounds;

    public Enemy(float x, float y) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /**
     * Updates the enemy's position to move towards the player.
     * @param deltaTime The time since the last frame.
     * @param playerPosition The current position of the player to move towards.
     */
    public void update(float deltaTime, Vector2 playerPosition) {
        // Calculate direction vector from enemy to player
        Vector2 direction = new Vector2(playerPosition).sub(position).nor();

        // Move the enemy in that direction
        position.mulAdd(direction, MOVE_SPEED * deltaTime);

        // Update bounds to match
        bounds.setPosition(position);
    }
}
