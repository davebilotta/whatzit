package com.davebilotta.whatzit.states;
/*
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class GameOverState2 extends State {

	private PlayState previousState;
	private String gameWinner;
	private Player[] players;
	private Player[] winners;
	
    static private Comparator<Player> descScore;

    static {
        descScore = new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getScore(), p1.getScore());
            }
        };
    }	

	public GameOverState2(WhatzIt game, GameStateManager gsm, PlayState st,
			Player[] players) {
		super(game, gsm);
		this.previousState = st;
		
		//players[0].win(-2345);
		
		this.players = players;
		this.winners = players; // initialize as equal to players initially and then sort

        Arrays.sort(winners, descScore);
	}

	@Override
	public void handleInput() {
		if (Gdx.input.justTouched()) {
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
		
		drawCenteredText(sb,"GAME OVER!",this.game.HEIGHT,false);		

		for (int i = 0; i < this.winners.length; i++) { 
			drawCenteredText(sb,this.winners[i].getName() + ": " + this.winners[i].getScore(),this.game.HEIGHT - (40 * i) - 100,false);
		}

		sb.end();
	}

    @Override
    public void notifyResponse(Player player, String response) {

    }

    public void drawCenteredText(SpriteBatch sb, String text, int y,
                                 boolean drawMedal) {
		//TextBounds bounds = this.game.im.nameFont.getBounds(text);
		//float w = bounds.width;
		///float h = bounds.height;
		float w = 100;
		float h = 100;

		this.game.im.scoreFont.draw(sb, text, (this.game.WIDTH - w) / 2,
				(y - h - 40));

		if (drawMedal) {
			sb.draw(this.game.im.getMedal(), (this.game.WIDTH - w) / 2 - 100,
					(y - this.game.im.getMedal().getHeight() - 40),
					this.game.im.getMedal().getWidth(), this.game.im.getMedal()
							.getHeight());
		}
	}
}

*/
