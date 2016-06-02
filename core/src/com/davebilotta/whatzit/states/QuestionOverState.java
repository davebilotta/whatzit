package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class QuestionOverState extends State {

	private PlayState previousState;
	private Player winner;
	private int score;

	private Skin skin;
	private Stage stage;

	public QuestionOverState(WhatzIt game, GameStateManager gsm, PlayState st,
			Player winner, int score) {
		super(game, gsm);
		this.previousState = st;
		this.winner = winner;
		this.score = score;

		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();

		Gdx.input.setInputProcessor(stage);

		this.skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas("uiskin.atlas"));
		
		setStage();
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
		table.setFillParent(true);

		String winnerName, winnerScore;

		table.row();
		//table.add("QUESTION OVER!");
		table.add(new Image(this.game.im.getQuestionOverImg()));

		// Winner name
		table.row();
		if (this.winner == null) {
			winnerName = "Winner: None";
			winnerScore = "Points: None";
		} else {
			winnerName = "Winner: " + this.winner.getName();
			winnerScore = "Points: " + this.score;
		}
		table.add(winnerName);

		// # of points
		table.row();
		table.add(winnerScore);

		table.row();
		table.add(" ");
		
		// Continue button
		table.row();
		HorizontalGroup row = new HorizontalGroup();
		TextButton continueButton = new TextButton("Continue", this.skin);
		continueButton.addListener(new UIClickListener("continue"));
		continueButton.setName("nextQuestion");
		continueButton.center();
		
		//table.align(Align.center);
		//table.addActor(continueButton);
		row.addActor(continueButton);
		row.align(Align.center);
		table.add(row);

		//table.pad(10f);
		stage.addActor(table);
		
		//Image bkg = new Image(this.game.im.getBkg());
		//stage.addActor(bkg);
	}

	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}

	public void continueButtonOnClick() {
		stage.dispose();
		this.gsm.set(previousState);
		previousState.waiting = false;
	}

	// @Override
	public void render2(SpriteBatch sb) {
		sb.begin();

		String upperText = "QUESTION OVER";

		drawCenteredText(sb, upperText, this.game.HEIGHT, false);

		String winnerName;
		String winnerScore;

		if (this.winner == null) {
			winnerName = "Winner: None";
			winnerScore = "Points: None";
		} else {
			winnerName = "Winner: " + this.winner.getName();
			// winnerScore = "Points: " + this.winner.getScore();
			winnerScore = "Points: " + this.score;
		}

		drawCenteredText(sb, winnerName, this.game.HEIGHT / 2, true);
		drawCenteredText(sb, winnerScore, this.game.HEIGHT / 2 - 100, false);

		drawCenteredImage(sb, this.game.im.getButton(),
				this.game.HEIGHT / 2 - 200);

		sb.end();
	}

	public void drawCenteredText(SpriteBatch sb, String text, int y,
			boolean drawMedal) {

		// TODO: Need to fix these
		// TextBounds bounds = this.game.im.nameFont.getBounds(text);
		//float w = bounds.width;
		//float h = bounds.height;
        float w = 100;
        float h = 30;

		this.game.im.scoreFont.draw(sb, text, (this.game.WIDTH - w) / 2,
				(y - h - 40));

		if (drawMedal) {
			sb.draw(this.game.im.getMedal(), (this.game.WIDTH - w) / 2 - 100,
					(y - this.game.im.getMedal().getHeight() - 40),
					this.game.im.getMedal().getWidth(), this.game.im.getMedal()
							.getHeight());
		}
	}

	public void drawCenteredImage(SpriteBatch sb, Texture img, int y) {

		// TextBounds bounds = this.game.im.nameFont.getBounds(text);
		float w = img.getWidth();
		float h = img.getHeight();

		sb.draw(img, (this.game.WIDTH - w) / 2, (y - h - 40));

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

	/* 
	 * 	private void createBasicSkin() {
		// Create a font
		BitmapFont font = new BitmapFont();
		skin = new Skin();
		skin.add("default", font);

		// Create a texture
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(140, 60, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background", new Texture(pixmap));

		// Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", Color.ORANGE);
		textButtonStyle.down = skin.newDrawable("background", Color.NAVY);
		textButtonStyle.checked = skin.newDrawable("background", Color.NAVY);
		textButtonStyle.over = skin.newDrawable("background", Color.WHITE);
		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);
	
	} */

    @Override
    public void notifyResponse(Player player, String response) {

    }
}
