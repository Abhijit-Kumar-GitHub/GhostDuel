package io.github.abhijit_kumar_github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    // New variable to control the game's downward scroll speed
    private float scrollSpeed = 150f; // Speed in pixels per second

    public GameScreen() {
        // Initialize the camera
        camera = new OrthographicCamera();

        // Initialize the viewport, telling it the size of our virtual world
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

        // Use GameConfig to center the player on the road
        float startX = GameConfig.ROAD_LEFT_BOUNDARY + (GameConfig.ROAD_WIDTH - Player.WIDTH) / 2;
        player = new Player(startX, 50);
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

        // Tell the ShapeRenderer to use our camera's coordinate system
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // to change the color of the road (will later on choose colors or the texture, currently making do with the basic shapes filled with color
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(GameConfig.ROAD_LEFT_BOUNDARY, 0, GameConfig.ROAD_WIDTH, GameConfig.WORLD_HEIGHT);

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
    public void resize(int width, int height) {
        // Update the viewport with the new screen size (to update viewport whenever the screensize changes (automatically at start and when we change screen size )
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
