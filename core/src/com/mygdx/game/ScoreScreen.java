package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScoreScreen implements Screen{
    final BlackjackGame game;

    OrthographicCamera camera;

    private Music sax;

    public ScoreScreen(final BlackjackGame _game) {
        game = _game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        sax = Gdx.audio.newMusic(Gdx.files.internal("Sax.mp3"));

        sax.setLooping(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.2f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.text.draw(game.batch, "Your score was: " + GameScreen.totalScoreGlobal, 100, 150);
        game.batch.end();

        if(Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
        sax.play();
    }

    @Override
    public void dispose() {
        sax.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}