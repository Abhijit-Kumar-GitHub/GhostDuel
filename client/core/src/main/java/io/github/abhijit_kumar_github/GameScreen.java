package io.github.abhijit_kumar_github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    // Game objects
    private Player player;

    // Rendering tools
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;              // Using SpriteBatch to draw all our game textures

    // Textures
    private Texture roadTexture;
    private Texture playerTexture;          // Added a texture for our player character

    // Game state variables
    private float scrollSpeed = 100f;       // Speed in pixels per second
    private float roadYScroll = 0;

    public GameScreen() {
        // Initialize the camera
        camera = new OrthographicCamera();

        // Initialize the viewport, telling it the size of our virtual world
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);

        // Initialize the SpriteBatch and load all textures
        batch = new SpriteBatch();
        roadTexture = new Texture("road_fullsize.png");         // Use the road texture name
        playerTexture = new Texture("player_ghost.png");        // Use the player texture name

        // This is the crucial line for infinite scrolling
        roadTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);

        // Use GameConfig to center the player on the road
        float startX = GameConfig.ROAD_LEFT_BOUNDARY + (GameConfig.ROAD_WIDTH - Player.WIDTH) / 2;
        player = new Player(startX, 50);
    }

    @Override
    public void render(float delta) {
        // 1. Handle Input
        handleInput();

        // 2. Update Game State
        player.update(delta);
        camera.update();    // It's important to update the camera every frame

        // This line updates our road's vertical scroll position
        roadYScroll -= scrollSpeed * delta;
        roadYScroll %= roadTexture.getHeight();

        // 3. Render Graphics
        ScreenUtils.clear(Color.BLACK); // Clear the screen

        // Tell the SpriteBatch to use our camera's coordinate system
        batch.setProjectionMatrix(camera.combined);

        // Begin drawing with the SpriteBatch
        batch.begin();

        // Set color to white to ensure no tint is applied to the road
        batch.setColor(Color.WHITE);

        // This version explicitly uses integer pixel coordinates (srcX, srcY, srcWidth, srcHeight)
        // and adds the final two 'flip' booleans, ensuring we call the correct method.
        batch.draw(
            roadTexture,
            GameConfig.ROAD_LEFT_BOUNDARY, 0,                                          // Position on screen (x, y)
            GameConfig.ROAD_WIDTH, GameConfig.WORLD_HEIGHT,                            // Size on screen (width, height)
            0, (int) roadYScroll,                                                      // The slice's top-left corner on the texture (srcX, srcY)
            (int) GameConfig.ROAD_WIDTH, (int) GameConfig.WORLD_HEIGHT,                // The slice's dimensions on the texture (srcWidth, srcHeight)
            false, false                                                               // Don't flip the texture horizontally or vertically
        );


        // Change player color and opacity when dematerialized to give visual feedback
        if (player.dematerialized) {
            // Set a ghostly cyan color with 50% transparency (r, g, b, alpha)
            batch.setColor(0.5f, 0.8f, 1f, 0.5f);
        } else {
            // Reset to default white with full opacity
            batch.setColor(Color.WHITE);
        }

        // Draw the player texture onto the player's bounds
        batch.draw(playerTexture, player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);

        // End drawing with the SpriteBatch
        batch.end();
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
        // Dispose of all our textures and the batch to prevent memory leaks
        batch.dispose();
        roadTexture.dispose();
        playerTexture.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport with the new screen size
        viewport.update(width, height, true);
    }

    // Other required methods remain empty for now
    @Override
    public void show() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
