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
    private int[] slotScore = new int[4];
    private boolean[] aceCount = new boolean[4];

    private Deck deck;
    private Sprite cardSprite;
    private Card card;

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
        card = deck.draw();
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

    private void placeCardInSlot(int slotIndex) {
        slotCardCount[slotIndex]++;
        if(card.getRank() == Card.Rank.ACE) {
            aceCount[slotIndex] = true;
        }
        slotScore[slotIndex] += Math.min(card.getRank().getValue(), 10);
        Tween movement = Tween.to(cardSprite, SpriteAccessor.POS_XY, 1.0f);
        movement.target(cardSprite.getX() + 145+(160*slotIndex), 480 - cardSprite.getHeight() - 36 * slotCardCount[slotIndex]);
        movement.start(game.tweenManager);
        drawCardFromDeck();
    }

    private int getScoreWithAce(int score) {
        return score+10;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            placeCardInSlot(0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            placeCardInSlot(1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            placeCardInSlot(2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            placeCardInSlot(3);
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
        for(int i = 0; i < 4; i++) {
            int altScore = 0;
            String score = Integer.toString(slotScore[i]);
            if (aceCount[i] == true) {
                altScore = getScoreWithAce(slotScore[i]);
                score = Integer.toString(slotScore[i])+ " / " +Integer.toString(altScore);
            }
            if (altScore > 21 || slotScore[i] > 21) {
                score = "BUST!!!";
            }
            game.text.draw(game.batch, score, 220 + (i * 160), 470);
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
