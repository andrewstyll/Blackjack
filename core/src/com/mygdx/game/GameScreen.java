package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameScreen implements Screen {
    final BlackjackGame game;

    private OrthographicCamera camera;
    private Texture cardImage;
    private Sprite card;

    public GameScreen(final BlackjackGame _game) {
        game = _game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        cardImage = new Texture(Gdx.files.internal("c12.png"));
        card = new Sprite(cardImage);
        float scale = 150 / card.getWidth();
        card.setSize(150, card.getHeight() * scale);
        card.setPosition(0, 0);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Reset the screen
        Gdx.gl.glClearColor(0, 0.8f, 0, 0.2f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        card.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        cardImage.dispose();
    }
}
