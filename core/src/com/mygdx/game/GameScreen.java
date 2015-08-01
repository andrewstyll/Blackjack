package com.mygdx.game;

import aurelienribon.accessors.SpriteAccessor;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    final BlackjackGame game;

    private OrthographicCamera camera;

    private Array<Array<Texture>> cardTextures;

    private Deck deck;
    private Sprite cardSprite;

    public GameScreen(final BlackjackGame _game) {
        game = _game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //cardImage = new Texture(Gdx.files.internal("c12.png"));
        cardTextures = new Array<Array<Texture>>(4);
        String[] suits = {"spades", "hearts", "clubs", "diamonds"};
        for (Card.Suit suit : Card.Suit.values()) {
            //this will grow this array from the first dimension onwards (implicit dimension)
            //from the first index onwards
            cardTextures.add(new Array<Texture>(13));
            for (Card.Rank rank : Card.Rank.values()) {
                String filename = suits[suit.getValue()].charAt(0) + String.format("%02d", rank.getValue()) + ".png";
                cardTextures.get(suit.getValue()).add(new Texture(Gdx.files.internal(filename)));
            }
        }

        deck = new Deck();

        cardSprite = new Sprite(cardTextures.get(3).get(5));
        float scale = 150 / cardSprite.getWidth();
        cardSprite.setSize(150, cardSprite.getHeight() * scale);
        cardSprite.setPosition(0, 0);
    }

    private void drawCardFromDeck() {
        Card card = deck.draw();
        if (card != null) {
            Texture texture = cardTextures.get(card.getSuit().getValue()).get(card.getRank().getValue() - 1);
            cardSprite.setTexture(texture);
            Tween movement = Tween.to(cardSprite, SpriteAccessor.POS_XY, 1.0f);
            movement.target(cardSprite.getX() + 25, cardSprite.getY() + 25);
            movement.start(game.tweenManager);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            drawCardFromDeck();
        }

        game.tweenManager.update(delta);

        // Reset the screen
        Gdx.gl.glClearColor(0, 0.8f, 0, 0.2f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        cardSprite.draw(game.batch);

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
        for(int j = 0; j < 4; j++) {
            for(int i = 0; i < 13; i++) {
                cardTextures.get(j).get(i).dispose();
            }
        }
    }
}
