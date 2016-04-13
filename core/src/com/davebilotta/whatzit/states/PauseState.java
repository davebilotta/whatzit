package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class PauseState extends State {

	private PlayState previousState;
	
	public PauseState(WhatzIt game,GameStateManager gsm, PlayState st) {
		super(game,gsm);
		this.previousState = st;
		
	}

	@Override
	public void handleInput() {
		if (Gdx.input.justTouched()) {
			//Utils.log("Resuming");
			this.gsm.set(previousState);
		}
		
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		this.game.im.scoreFont.draw(sb, "GAME PAUSED", this.game.WIDTH/2, this.game.HEIGHT/2);
		sb.end();
	}
}
