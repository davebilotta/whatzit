package com.davebilotta.whatzit.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public abstract class State {
	protected GameStateManager gsm;
	protected OrthographicCamera cam;
	protected Vector3 mouse;
	public WhatzIt game;
	
	protected State(WhatzIt game, GameStateManager gsm) {
		this.game = game;
		this.gsm = gsm;
		
		cam = new OrthographicCamera(this.game.WIDTH,this.game.HEIGHT);
		cam.setToOrtho(false);
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render(SpriteBatch sb);
    public abstract void notifyResponse(Player player, String response);

}
