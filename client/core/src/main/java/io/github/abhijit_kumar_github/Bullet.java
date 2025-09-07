package io.github.abhijit_kumar_github;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public static final float WIDTH = 10;
    public static final float HEIGHT = 20;
    public static final float SPEED = 600f; // Faster than the player or enemies

    public final Vector2 position;
    public final Vector2 velocity;
    public final Rectangle bounds;
    public boolean active = true;

    public Bullet(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.bounds = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    /**
     * Initializes the bullet's velocity based on a target destination.
     * @param targetX The x-coordinate of the target.
     * @param targetY The y-coordinate of the target.
     */
    public void setVelocity(float targetX, float targetY) {
        velocity.set(targetX, targetY).sub(position).nor().scl(SPEED);
    }

    /**
     * Updates the bullet's position based on its velocity.
     * @param deltaTime The time since the last frame.
     */
    public void update(float deltaTime) {
        position.mulAdd(velocity, deltaTime);
        bounds.setPosition(position.x - WIDTH / 2, position.y - HEIGHT / 2);
    }
}
