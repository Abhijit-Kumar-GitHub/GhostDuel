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
    private Player player;
    private EnemyManager enemyManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture groundTexture;

    public GameScreen() {
        camera = new OrthographicCamera();
        // The Viewport ensures our game world's aspect ratio is preserved, preventing distortion.
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        playerTexture = new Texture("player_ghost.png");
        groundTexture = new Texture("ground.png");

        // Set the ground texture to repeat (tile) for infinite scrolling. This is crucial.
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Start the player at the center of the world
        player = new Player(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2);
        enemyManager = new EnemyManager();

    }

    @Override
    public void render(float delta) {
        // --- LOGIC UPDATES ---
        handleInput();
        player.update(delta);
        enemyManager.update(delta, player.position);

        // --- CAMERA UPDATE ---
        // Make the camera follow the player's position.
        camera.position.set(player.position.x, player.position.y, 0);
        camera.update();

        // --- RENDERING ---
        ScreenUtils.clear(Color.valueOf("333333")); // A dark slate gray background

        // Tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // --- CORRECTED INFINITE TILING LOGIC ---
        // This method correctly scrolls the texture without stretching it.
        // It calculates a region of the texture (u,v) to draw based on the player's position.
        // This ensures the background moves 1:1 with the world, removing perceptual speed differences.
        float u = player.position.x / groundTexture.getWidth();
        float v = player.position.y / groundTexture.getHeight();
        float u2 = u + GameConfig.WORLD_WIDTH / groundTexture.getWidth();
        float v2 = v + GameConfig.WORLD_HEIGHT / groundTexture.getHeight();

        // We draw the texture onto a static quad that always fills the camera's view.
        batch.draw(groundTexture,
            camera.position.x - GameConfig.WORLD_WIDTH / 2,
            camera.position.y - GameConfig.WORLD_HEIGHT / 2,
            GameConfig.WORLD_WIDTH,
            GameConfig.WORLD_HEIGHT,
            u, v, u2, v2);
        // --------------------------------

        enemyManager.draw(batch);

        // Draw the player centered on its position
        batch.draw(playerTexture, player.position.x - Player.WIDTH / 2, player.position.y - Player.HEIGHT / 2, Player.WIDTH, Player.HEIGHT);

        batch.end();
    }

    private void handleInput() {
        player.velocity.set(0, 0);
        // Corrected Y-axis movement: W should be positive Y (up), S should be negative Y (down).
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.velocity.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.velocity.y = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.velocity.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.velocity.x = -1;

        // Normalize velocity to ensure constant speed in all directions.
        // Using len2() > 0 is slightly more efficient than len() > 0 as it avoids a square root.
        if (player.velocity.len2() > 0) {
            player.velocity.nor().scl(Player.MOVE_SPEED);
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport and center the camera correctly when the window is resized.
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        groundTexture.dispose();
        enemyManager.dispose();
    }

    // Other required Screen interface methods
    @Override
    public void show() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
