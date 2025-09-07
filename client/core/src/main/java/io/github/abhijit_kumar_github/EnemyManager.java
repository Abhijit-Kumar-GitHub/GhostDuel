package io.github.abhijit_kumar_github;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class EnemyManager {
    private final Array<Enemy> enemies;
    private final Texture enemyTexture;
    private final Random random;

    private long lastSpawnTime;
    private float spawnInterval = 1.0f; // Spawn a new enemy every second

    public EnemyManager() {
        this.enemies = new Array<>();
        this.enemyTexture = new Texture("enemy.png");
        this.random = new Random();
        this.lastSpawnTime = TimeUtils.nanoTime();
    }

    public void update(float deltaTime, Vector2 playerPosition) {
        // Spawn new enemies
        if (TimeUtils.nanoTime() - lastSpawnTime > spawnInterval * 1_000_000_000L) {
            spawnEnemy(playerPosition);
            lastSpawnTime = TimeUtils.nanoTime();
        }

        // Update all existing enemies
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime, playerPosition);
        }
    }

    public void draw(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            batch.draw(enemyTexture, enemy.position.x - Enemy.WIDTH / 2, enemy.position.y - Enemy.HEIGHT / 2, Enemy.WIDTH, Enemy.HEIGHT);
        }
    }

    private void spawnEnemy(Vector2 playerPosition) {
        // Spawn enemies at a random position just outside the screen borders
        float spawnX, spawnY;
        int edge = random.nextInt(4); // 0: top, 1: bottom, 2: left, 3: right

        if (edge == 0) { // Top
            spawnX = playerPosition.x + random.nextFloat() * GameConfig.WORLD_WIDTH - GameConfig.WORLD_WIDTH / 2;
            spawnY = playerPosition.y + GameConfig.WORLD_HEIGHT / 2 + 50;
        } else if (edge == 1) { // Bottom
            spawnX = playerPosition.x + random.nextFloat() * GameConfig.WORLD_WIDTH - GameConfig.WORLD_WIDTH / 2;
            spawnY = playerPosition.y - GameConfig.WORLD_HEIGHT / 2 - 50;
        } else if (edge == 2) { // Left
            spawnX = playerPosition.x - GameConfig.WORLD_WIDTH / 2 - 50;
            spawnY = playerPosition.y + random.nextFloat() * GameConfig.WORLD_HEIGHT - GameConfig.WORLD_HEIGHT / 2;
        } else { // Right
            spawnX = playerPosition.x + GameConfig.WORLD_WIDTH / 2 + 50;
            spawnY = playerPosition.y + random.nextFloat() * GameConfig.WORLD_HEIGHT - GameConfig.WORLD_HEIGHT / 2;
        }

        enemies.add(new Enemy(spawnX, spawnY));
    }

    /**
     * Returns the list of active enemies for collision detection.
     * This was the missing piece for the Game Over logic.
     * @return The array of active enemies.
     */
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Clears all enemies from the manager, used for restarting the game.
     */
    public void clear() {
        enemies.clear();
    }

    public void dispose() {
        enemyTexture.dispose();
    }
}
