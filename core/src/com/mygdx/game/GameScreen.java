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
    private Array<Sprite> drawnCards;
    private int[] slotCardCount = new int[4];

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
        drawnCards = new Array<Sprite>();

        deck = new Deck();

        drawCardFromDeck();
    }

    private void drawCardFromDeck() {
        Card card = deck.draw();
        if (card != null) {
            Texture texture = cardTextures.get(card.getSuit().getValue()).get(card.getRank().getValue() - 1);
            //cardSprite.setTexture(texture);
            cardSprite = new Sprite(texture);
            drawnCards.add(cardSprite);
        }
        float scale = 150 / cardSprite.getWidth();
        cardSprite.setSize(150, cardSprite.getHeight() * scale);
        cardSprite.setPosition(0, 0);
    }

    private void placeCardInSlot(int slotValue, int cardCount) {
        Tween movement = Tween.to(cardSprite, SpriteAccessor.POS_XY, 1.0f);

        movement.target(cardSprite.getX() + slotValue, 480 - cardSprite.getHeight() - 36 * cardCount);
        movement.start(game.tweenManager);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            slotCardCount[0]++;
            placeCardInSlot(145, slotCardCount[0]);
            drawCardFromDeck();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            slotCardCount[1]++;
            placeCardInSlot(305, slotCardCount[1]);
            drawCardFromDeck();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            slotCardCount[2]++;
            placeCardInSlot(465, slotCardCount[2]);
            drawCardFromDeck();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            slotCardCount[3]++;
            placeCardInSlot(625, slotCardCount[3]);
            drawCardFromDeck();
        }
        game.tweenManager.update(delta);

        // Reset the screen
        Gdx.gl.glClearColor(0, 0.8f, 0, 0.2f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        for(Sprite drawnCard : drawnCards) {
            drawnCard.draw(game.batch);
        }

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
