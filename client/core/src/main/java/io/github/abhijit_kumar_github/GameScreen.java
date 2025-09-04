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
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture groundTexture;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        playerTexture = new Texture("player_ghost.png");
        groundTexture = new Texture("ground.png");

        // Set the ground texture to repeat (tile) for infinite scrolling
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        player = new Player(0, 0);
    }

    @Override
    public void render(float delta) {
        handleInput();
        player.update(delta);

        // Make the camera follow the player
        camera.position.set(player.position.x, player.position.y, 0);
        camera.update();

        ScreenUtils.clear(Color.valueOf("333333")); // A dark slate gray background
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // --- Infinite Tiling Logic ---
        // We use the camera's position to calculate which part of the infinite texture to show.
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        batch.draw(groundTexture,
            cameraX - GameConfig.WORLD_WIDTH / 2, cameraY - GameConfig.WORLD_HEIGHT / 2,
            GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT,
            (int)cameraX, (int)cameraY,
            (int)GameConfig.WORLD_WIDTH, (int)GameConfig.WORLD_HEIGHT,
            false, false);
        // --------------------------------

        // Draw the player centered on its position
        batch.draw(playerTexture, player.position.x - Player.WIDTH / 2, player.position.y - Player.HEIGHT / 2, Player.WIDTH, Player.HEIGHT);

        batch.end();
    }

    private void handleInput() {
        player.velocity.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.velocity.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.velocity.y = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.velocity.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.velocity.x = -1;

        // Normalize velocity to ensure constant speed in all directions
        if (player.velocity.len() > 0) {
            player.velocity.nor().scl(Player.MOVE_SPEED);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        groundTexture.dispose();
    }

    // Other required methods
    @Override
    public void show() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
