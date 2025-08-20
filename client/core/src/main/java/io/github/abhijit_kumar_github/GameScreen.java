package io.github.abhijit_kumar_github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private Player player;
    private ShapeRenderer shapeRenderer;

    // New variable to control the game's downward scroll speed
    private float scrollSpeed = 150f; // Speed in pixels per second

    public GameScreen() {
        // Start the player in the bottom-middle of the screen
        player = new Player(1280 / 2f - Player.WIDTH / 2f, 50);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // 1. Handle Input
        handleInput();

        // 2. Update Game State
        player.update(delta);

        // In the future, obstacles will be updated here, moving down at scrollSpeed

        // 3. Render Graphics
        ScreenUtils.clear(Color.BLACK); // Clear the screen

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Change player color when dematerialized to give visual feedback
        if (player.dematerialized) {
            shapeRenderer.setColor(Color.CYAN); // Ghostly color
        } else {
            shapeRenderer.setColor(Color.WHITE);
        }

        shapeRenderer.rect(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);
        shapeRenderer.end();
    }

    private void handleInput() {
        // Horizontal Movement
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.velocity.x = -Player.MOVE_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.velocity.x = Player.MOVE_SPEED;
        } else {
            player.velocity.x = 0;
        }

        // Dematerialize Superpower
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.dematerialize();
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    // Other required methods remain empty for now
    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
