package com.davebilotta.whatzit.states;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.WhatzIt;

public class GameOverState extends State {
	private Skin skin;
	private Stage stage;

    Label.LabelStyle uiFontStyle;
    Label.LabelStyle messageStyle;

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

        uiFontStyle = new Label.LabelStyle();
        uiFontStyle.font = this.game.im.uiFont;
        uiFontStyle.fontColor = Color.BLUE;

        messageStyle = new Label.LabelStyle();
        messageStyle.font = this.game.im.messageFont;
        messageStyle.fontColor = Color.WHITE;

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
        boolean singlePlayerMode;

        if (winners.length > 1) singlePlayerMode = false;
        else singlePlayerMode = true;

		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
        //table.setDebug(true);
        table.align(Align.left);
		table.setFillParent(true);

		table.row();
	    table.add(addLabel("GAME OVER!",uiFontStyle));

		// Winner name
		table.row();
		if (singlePlayerMode) {
			table.add(addLabel("Points: " + winners[0].getScore()));
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
                HorizontalGroup groupH = new HorizontalGroup();
                VerticalGroup groupV = new VerticalGroup();

                groupH.addActor(addImage(this.game.im.getMedal(1)));
				groupH.addActor(addLabel("Winner: " + winners[0].getName()));
                groupH.space(10f);
				groupV.addActor(groupH);
				table.row().padBottom(40f);

                groupV.addActor(addLabel(winners[0].getScore() + " points"));

                table.add(groupV);
				start = 1;
			}

			table.row();
			
			for (int i = start; i < winners.length; i++) {
                HorizontalGroup groupH = new HorizontalGroup();
                VerticalGroup groupV = new VerticalGroup();

				table.row();
				String pt;
				if (winners[i].getScore() == 1) { pt = "point"; 
				}
				else { pt = "points"; 
				}
				
				groupH.addActor(addImage(this.game.im.getMedal(i+1)));
                groupH.addActor(addLabel(winners[i].getName() + ": "
						+ winners[i].getScore() + " " + pt));
                groupH.space(10f);
                groupH.padBottom(20f);
                table.add(groupH);
			}

		}
		// Continue button

		table.row();
		HorizontalGroup row = new HorizontalGroup();
        TextButton continueButton = addButton("Continue");
        continueButton.center();
        table.addActor(continueButton);

		row.addActor(continueButton);
        row.align(Align.center);
        table.add(row).bottom().expand().padBottom(20f);

		// table.pad(10f);
		stage.addActor(table);
	}

    public TextButton addButton(String name) {
        TextButton button = new TextButton(name, this.game.im.getUIButtonStyle());

        button.addListener(new UIClickListener(name.toLowerCase()));

        return button;
    }

    public Image addImage(Texture image) {
      //  return new Image(new TextureRegionDrawable(new TextureRegion(Gdx.files.internal(path))));

        return new Image(image);
    }

    public Label addLabel(String text) {
        return new Label(text,messageStyle);
    }

    public Label addLabel(String text, Label.LabelStyle style) {
        return new Label(text,style);
    }

	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}

	public void continueButtonOnClick() {
		stage.dispose();
		//this.gsm.set(previousState);
		previousState.waiting = false;
		this.gsm.set(new MainMenuState(this.game,gsm));
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
