package com.mygdx.game;

import aurelienribon.accessors.SpriteAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlackjackGame extends Game {
	public SpriteBatch batch;
	public TweenManager tweenManager;

	public void create () {
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	public void render () {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
