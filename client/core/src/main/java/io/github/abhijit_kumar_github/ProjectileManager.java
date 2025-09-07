package io.github.abhijit_kumar_github;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

public class ProjectileManager {
    private final Array<Bullet> activeBullets = new Array<>();
    private final Texture bulletTexture;

    public ProjectileManager() {
        // A simple white pixel can be scaled to represent the bullet.
        // For better visuals, you can replace "bullet.png" with a proper texture.
        this.bulletTexture = new Texture("bullet.png");
    }

    /**
     * Spawns a new bullet.
     * @param startX The starting x-coordinate.
     * @param startY The starting y-coordinate.
     * @param targetX The target x-coordinate to shoot towards.
     * @param targetY The target y-coordinate to shoot towards.
     */
    public void spawnBullet(float startX, float startY, float targetX, float targetY) {
        Bullet bullet = new Bullet(startX, startY);
        bullet.setVelocity(targetX, targetY);
        activeBullets.add(bullet);
    }

    public void update(float deltaTime, Rectangle viewBounds) {
        Iterator<Bullet> iterator = activeBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update(deltaTime);

            // If bullet is inactive (hit something) or out of bounds, remove it.
            if (!bullet.active || !viewBounds.contains(bullet.position)) {
                iterator.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Bullet bullet : activeBullets) {
            // The bullet's texture is drawn rotated to face its direction of travel.
            float angle = bullet.velocity.angleDeg() - 90; // -90 degree offset because texture points up
            batch.draw(bulletTexture,
                bullet.position.x - Bullet.WIDTH / 2, bullet.position.y - Bullet.HEIGHT / 2,
                Bullet.WIDTH / 2, Bullet.HEIGHT / 2,
                Bullet.WIDTH, Bullet.HEIGHT,
                1, 1, angle, 0, 0,
                bulletTexture.getWidth(), bulletTexture.getHeight(),
                false, false);
        }
    }

    public Array<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public void clear() {
        activeBullets.clear();
    }

    public void dispose() {
        bulletTexture.dispose();
    }
}
