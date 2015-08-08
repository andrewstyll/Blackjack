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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private Vector3 touchPos;

    final BlackjackGame game;

    private Deck deck;
    private Sprite cardSprite;
    private Card card;

    private Array<Array<Texture>> cardTextures;
    private Array<Array<Sprite>> cardsInSlot;
    private Array<Sprite> drawnCards;

    private int[] slotCardCount = new int[4];//counts the number of cards in each slot
    private int[] slotScore = new int[4];//holds the current score
    private int[] altScore = new int[4];//holds an alternative score for ace = 11
    private boolean[] aceCount = new boolean[4];//checks for aces

    public static String totalScoreGlobal;
    private int totalScore;//holds numerical total score
    private String score;//the string that will output both the score

    private float time;
    //private int minutes;
    private int seconds;
    //private String displayTime;

    public GameScreen(final BlackjackGame _game) {
        game = _game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        touchPos = new Vector3();

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
        cardsInSlot = new Array<Array<Sprite>>(4);
        for(int i = 0; i < 4; i++) {
            cardsInSlot.add(new Array<Sprite>());
        }

        totalScore = 0;
        score = "0";
        time = 61;
        //minutes = 0;
        seconds = 0;
        //displayTime = "0:0";

        drawCardFromDeck();
    }

    private void drawCardFromDeck() {
        card = deck.draw();
        if (card != null) {
            Texture texture = cardTextures.get(card.getSuit().getValue()).get(card.getRank().getValue() - 1);
            //cardSprite.setTexture(texture);
            cardSprite = new Sprite(texture);
            drawnCards.add(cardSprite);
            float scale = 150 / cardSprite.getWidth();
            cardSprite.setSize(150, cardSprite.getHeight() * scale);
            cardSprite.setPosition(0, 0);
        } else {
            game.setScreen(new ScoreScreen(game));
            dispose();
        }
    }

    private void placeCardInSlot(int slotIndex) {
        slotCardCount[slotIndex]++;
        cardsInSlot.get(slotIndex).add(cardSprite);
        if(card.getRank() == Card.Rank.ACE) {
            aceCount[slotIndex] = true;
        }
        slotScore[slotIndex] += Math.min(card.getRank().getValue(), 10);//calculate score
        Tween movement = Tween.to(cardSprite, SpriteAccessor.POS_XY, 1.0f);
        movement.target(cardSprite.getX() + 145+(160*slotIndex), 480 - cardSprite.getHeight() - 36 * cardsInSlot.get(slotIndex).size);
        movement.start(game.tweenManager);
        drawCardFromDeck();
    }

    private int getScoreWithAce(int score) {
        return score+10;
    }

    private void readInput() {
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

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), 0, 0);
            camera.unproject(touchPos);
            if (touchPos.x >= 145 && touchPos.x <= 295) {
                placeCardInSlot(0);
            } else if (touchPos.x >= 305 && touchPos.x <= 455) {
                placeCardInSlot(1);
            } else if (touchPos.x >= 465 && touchPos.x <= 615) {
                placeCardInSlot(2);
            } else if (touchPos.x >= 625 && touchPos.x <= 775) {
                placeCardInSlot(3);
            }
        }
    }

    private void cleanUp(int index) {
        drawnCards.removeAll(cardsInSlot.get(index), true);
        cardsInSlot.get(index).clear();
        slotScore[index] = 0;
        altScore[index] = 0;
        aceCount[index] = false;
    }

    private int timer() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time -= deltaTime;

        return (int) time;
        //minutes = ((int)time)/60;
        //seconds = ((int)time)%60;
    }

    @Override
    public void render(float delta) {

        readInput();

        seconds = timer();

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
            //update scores here
            if (aceCount[i] == true) {
                altScore[i] = getScoreWithAce(slotScore[i]);
                score = Integer.toString(slotScore[i]) + " / " + Integer.toString(altScore[i]);
            } else {
                altScore[i] = slotScore[i];
                score = Integer.toString(slotScore[i]);
            }
            if (altScore[i] == 21 || slotScore[i] == 21) {
                cleanUp(i);
                score = "21!!!";
                totalScore += 100;
            }
            if (slotScore[i] > 21 && altScore[i] > 21) {
                cleanUp(i);
                score = "BUST!!!";
                totalScore -= 25;
            }
            game.text.draw(game.batch, score, 220 + (i * 160), 470);
        }
        totalScoreGlobal = Integer.toString(totalScore);
        game.text.draw(game.batch, "Time: " + Integer.toString(seconds), 100, 425);
        game.text.draw(game.batch, totalScoreGlobal, 100 , 400);

        game.batch.end();

        if(seconds <= 0) {
            game.setScreen(new ScoreScreen(game));
        }
    }

    @Override
    public void dispose() {
        for(int j = 0; j < 4; j++) {
            for(int i = 0; i < 13; i++) {
                cardTextures.get(j).get(i).dispose();
            }
        }
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
    public void show() {

    }
}
