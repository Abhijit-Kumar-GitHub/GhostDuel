package io.github.abhijit_kumar_github;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public static final float WIDTH = 40;
    public static final float HEIGHT = 40;
    public static final float MOVE_SPEED = 300f;

    public final Vector2 position;
    public final Vector2 velocity;
    public final Rectangle bounds;

    public Player(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.bounds = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    /**
     * Updates the player's position based on its current velocity.
     * @param deltaTime The time since the last frame.
     */
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        bounds.setPosition(position.x - WIDTH / 2, position.y - HEIGHT / 2);
    }

    /**
     * Resets the player's state to an initial position.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void reset(float x, float y) {
        position.set(x, y);
        velocity.set(0, 0);
        bounds.setPosition(position.x - WIDTH / 2, position.y - HEIGHT / 2);
    }
}
