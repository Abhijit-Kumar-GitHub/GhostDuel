package io.github.abhijit_kumar_github;

import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GhostDuelClient extends Game {
    @Override
    public void create() {
        // Set our GameScreen as the active screen
        setScreen(new GameScreen());
    }
}
