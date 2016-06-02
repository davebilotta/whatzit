package com.davebilotta.whatzit.states;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;
import com.davebilotta.whatzit.states.QuestionOverState.UIClickListener;

public class GameOverState extends State {
	private Skin skin;
	private Stage stage;

	private PlayState previousState;

	private Player[] players;
	private Player[] winners;

	static private Comparator<Player> descScore;

	static {
		descScore = new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				return Integer.compare(p2.getScore(), p1.getScore());
			}
		};
	}

	public GameOverState(WhatzIt game, GameStateManager gsm, PlayState st,
			Player[] players) {
		super(game, gsm);
		this.previousState = st;

		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();

		Gdx.input.setInputProcessor(stage);

		this.players = players;
		this.winners = players; // initialize as equal to players initially and then sort

		Arrays.sort(winners, descScore);
		
		// Test with single player	
		
		this.skin = new Skin(Gdx.files.internal("uiskin.json"),
				new TextureAtlas("uiskin.atlas"));
		setStage();
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

    @Override
    public void notifyResponse(Player player, String response) {

    }

    public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
		table.setFillParent(true);

		table.row();
		//table.add("GAME OVER!");
		table.add(new Image(this.game.im.getGameOverImg()));
		table.add(" ");
		
		boolean singlePlayerMode;
		
		if (winners.length > 1) singlePlayerMode = false;
		else singlePlayerMode = true;
				
		// Winner name
		table.row();
		if (singlePlayerMode) {
			table.add("Points: " + winners[0].getScore());
		} else {
			// Determine if we have a winner 
			
			boolean hasWinner = true;
			int prev = winners[0].getScore();
			for (int i = 1; i < winners.length; i++) { 
				if (winners[i].getScore() == prev) { hasWinner = false; }
			}
			// # of points
			table.row();
			//
			int start = 0;
			if (hasWinner) {
				table.add("Winner: " + winners[0].getName());
				
				table.row();
				table.add(winners[0].getScore() + " points");
				
				start = 1;
			}

			table.row(); 
			table.add(" ");
			
			for (int i = start; i < winners.length; i++) {
				table.row();
				String pt;
				if (winners[i].getScore() == 1) { pt = "point"; 
				}
				else { pt = "points"; 
				}
				
				table.add(winners[i].getName() + ": "
						+ winners[i].getScore() + " " + pt);
			}

		}
		// Continue button
		table.row();
		table.add(" ");
		
		table.row();
		HorizontalGroup row = new HorizontalGroup();
		TextButton continueButton = new TextButton("Continue", this.skin);
		continueButton.addListener(new UIClickListener("continue"));
		continueButton.setName("nextQuestion");
		continueButton.center();

		row.addActor(continueButton);
		row.align(Align.center);
		table.add(row);

		// table.pad(10f);
		stage.addActor(table);
	}

	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}

	public void continueButtonOnClick() {
		stage.dispose();
		//this.gsm.set(previousState);
		previousState.waiting = false;
		this.gsm.set(new MenuState(this.game,gsm));
	}

	public class UIClickListener extends ClickListener {

		private String buttonName;

		public UIClickListener(String buttonName) {
			this.buttonName = buttonName;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {

			super.clicked(event, x, y);

			if (this.buttonName.equals("continue")) {
				continueButtonOnClick();

			}
		}

	}

	@Override
	public void handleInput() {
		stage.act();
	}

}
