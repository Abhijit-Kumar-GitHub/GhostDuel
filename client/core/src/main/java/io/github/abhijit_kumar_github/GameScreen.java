package io.github.abhijit_kumar_github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class GameScreen implements Screen {

    // Game State
    private enum GameState {
        RUNNING,
        GAME_OVER
    }
    private GameState gameState = GameState.RUNNING;

    // Core Components
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // Game Objects
    private Player player;
    private EnemyManager enemyManager;
    private ProjectileManager projectileManager;

    // Assets
    private Texture playerTexture;
    private Texture groundTexture;

    // UI
    private BitmapFont font;
    private GlyphLayout glyphLayout; // Used for centering text
    private int score = 0;

    // For converting screen coordinates to world coordinates
    private final Vector3 mousePos = new Vector3();
    private final Rectangle cameraBounds = new Rectangle();

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        // Load assets
        playerTexture = new Texture("player_ghost.png");
        groundTexture = new Texture("ground.png");
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Initialize UI elements
        font = new BitmapFont();
        font.getData().setScale(2); // Make the font bigger
        glyphLayout = new GlyphLayout();

        // Initialize game objects
        player = new Player(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2);
        enemyManager = new EnemyManager();
        projectileManager = new ProjectileManager();
    }

    @Override
    public void render(float delta) {
        // --- LOGIC UPDATES ---
        handleInput(delta);

        if (gameState == GameState.RUNNING) {
            player.update(delta);
            enemyManager.update(delta, player.position);

            // Update camera bounds for projectile culling
            cameraBounds.set(
                    camera.position.x - GameConfig.WORLD_WIDTH / 2,
                    camera.position.y - GameConfig.WORLD_HEIGHT / 2,
                    GameConfig.WORLD_WIDTH,
                    GameConfig.WORLD_HEIGHT
            );
            projectileManager.update(delta, cameraBounds);

            checkCollisions();
        }

        // --- CAMERA UPDATE ---
        camera.position.set(player.position.x, player.position.y, 0);
        camera.update();

        // --- RENDERING ---
        ScreenUtils.clear(Color.valueOf("333333"));
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawBackground();
        enemyManager.draw(batch);
        projectileManager.draw(batch);
        drawPlayer();
        drawUI();
        batch.end();
    }

    private void handleInput(float delta) {
        if (gameState == GameState.RUNNING) {
            // Player movement
            player.velocity.set(0, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.W)) player.velocity.y = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) player.velocity.y = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) player.velocity.x = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) player.velocity.x = -1;
            if (player.velocity.len2() > 0) player.velocity.nor().scl(Player.MOVE_SPEED);

            // Player shooting (mouse)
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                // Convert screen coordinates (mouse click) to world coordinates
                mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);
                projectileManager.spawnBullet(player.position.x, player.position.y, mousePos.x, mousePos.y);
            }

            // --- NEW: Player shooting (keyboard) ---
            if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
                // Fire a bullet straight up from the player's current position.
                // The target is just 1 unit above the player to establish a direction.
                projectileManager.spawnBullet(player.position.x, player.position.y, player.position.x, player.position.y + 1);
            }

        } else if (gameState == GameState.GAME_OVER) {
            // Restart game
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                restartGame();
            }
        }
    }

    private void checkCollisions() {
        // Check for bullet vs enemy collisions
        Iterator<Bullet> bulletIterator = projectileManager.getActiveBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            // Use the new getEnemies() method here
            Iterator<Enemy> enemyIterator = enemyManager.getEnemies().iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (bullet.bounds.overlaps(enemy.bounds)) {
                    enemyIterator.remove();   // Remove enemy
                    bullet.active = false;    // Mark bullet for removal
                    score++;
                    break; // A bullet can only hit one enemy
                }
            }
        }

        // Check for enemy vs player collisions
        // This will now work correctly because getEnemies() is available
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (player.bounds.overlaps(enemy.bounds)) {
                gameState = GameState.GAME_OVER;
                break; // End the game
            }
        }
    }

    private void drawBackground() {
        float u = player.position.x / groundTexture.getWidth();
        float v = player.position.y / groundTexture.getHeight();
        float u2 = u + GameConfig.WORLD_WIDTH / groundTexture.getWidth();
        float v2 = v + GameConfig.WORLD_HEIGHT / groundTexture.getHeight();
        batch.draw(groundTexture, camera.position.x - viewport.getWorldWidth() / 2, camera.position.y - viewport.getWorldHeight() / 2, viewport.getWorldWidth(), viewport.getWorldHeight(), u, v, u2, v2);
    }

    private void drawPlayer() {
        batch.draw(playerTexture, player.position.x - Player.WIDTH / 2, player.position.y - Player.HEIGHT / 2, Player.WIDTH, Player.HEIGHT);
    }

    private void drawUI() {
        // Draw the score in the top-left corner relative to the camera's view
        String scoreText = "Score: " + score;
        font.draw(batch, scoreText, camera.position.x - viewport.getWorldWidth() / 2 + 10, camera.position.y + viewport.getWorldHeight() / 2 - 10);

        if (gameState == GameState.GAME_OVER) {
            // Draw "Game Over" text in the center
            glyphLayout.setText(font, "Game Over");
            float layoutWidth = glyphLayout.width;
            font.draw(batch, "Game Over", camera.position.x - layoutWidth / 2, camera.position.y + glyphLayout.height / 2 + 30);

            // Draw final score below "Game Over"
            String finalScoreText = "Final Score: " + score;
            glyphLayout.setText(font, finalScoreText);
            layoutWidth = glyphLayout.width;
            font.draw(batch, finalScoreText, camera.position.x - layoutWidth / 2, camera.position.y + glyphLayout.height / 2);

            // Draw "Restart" prompt
            String restartText = "Press 'R' to Restart";
            glyphLayout.setText(font, restartText);
            layoutWidth = glyphLayout.width;
            font.draw(batch, restartText, camera.position.x - layoutWidth / 2, camera.position.y - 40);
        }
    }

    private void restartGame() {
        player.reset(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2);
        enemyManager.clear();
        projectileManager.clear();
        score = 0;
        gameState = GameState.RUNNING;
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
        enemyManager.dispose();
        projectileManager.dispose();
        font.dispose();
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

