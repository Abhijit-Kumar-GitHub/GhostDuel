package io.github.abhijit_kumar_github;

import com.badlogic.gdx.math.Vector2;

public class Player {
    public static final float WIDTH = 50;
    public static final float HEIGHT = 70;
    public static final float MOVE_SPEED = 300f;

    public final Vector2 position;
    public final Vector2 velocity;

    public Player(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
    }

    public void update(float deltaTime) {
        // Adds the scaled velocity to the position vector
        position.mulAdd(velocity, deltaTime);
    }
}
