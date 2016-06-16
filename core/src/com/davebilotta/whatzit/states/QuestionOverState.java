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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class QuestionOverState extends State {

	private PlayState previousState;
    private WhatzIt game;
	private Player winner;
	private int score;
    private float rowSpacing = 30f;
	private Skin skin;
	private Stage stage;

   Label.LabelStyle uiFontStyle;
    Label.LabelStyle messageStyle;

    public class myActor extends Actor {
        public myActor() {

        }
    }

	public QuestionOverState(WhatzIt game, GameStateManager gsm, PlayState st,
			Player winner, int score, Texture image) {
		super(game, gsm);
		this.game = game;
        this.previousState = st;
		this.winner = winner;
		this.score = score;

		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();

		Gdx.input.setInputProcessor(stage);

        // TODO - use generic ImageManager skin
		this.skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas("uiskin.atlas"));

        uiFontStyle = new Label.LabelStyle();
        uiFontStyle.font = this.game.im.uiFont;
        uiFontStyle.fontColor = Color.BLUE;

        messageStyle = new Label.LabelStyle();
        messageStyle.font = this.game.im.messageFont;
        messageStyle.fontColor = Color.WHITE;

        setStage(image);
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	public void setStage(Texture image) {
		//
        Image bkg = new Image(image);
        // TODO: Set the alpha for this
        Color bkgColor = bkg.getColor();
        bkg.setColor(bkgColor.r,bkgColor.g,bkgColor.b,0.4f);

        bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
		table.setFillParent(true);
        //table.setDebug(true);
        table.center();

        String winnerName, winnerScore;

		table.row();

        String topText = "";
        if (this.winner == null) {
            topText = "Question Over";
            winnerName = "Winner: None";
            winnerScore = "Points: 0";
        }
        else {
            topText = "Correct!";
            winnerName = "Winner: " + this.winner.getName();
            winnerScore = "Points: " + this.score;
        }

        // Top text
        table.row();
        table.add(addLabel(topText,uiFontStyle));

        // Winner name
        table.row();
        table.add(addLabel(winnerName));

		// Number of points
		table.row();
        table.add(addLabel(winnerScore));

		// Continue button
		table.row();
		HorizontalGroup row = new HorizontalGroup();

		TextButton continueButton = addButton("Next Question");
		continueButton.center();
        row.addActor(continueButton);
		row.align(Align.center);
        table.add(row).bottom().expand().padBottom(20f);

        stage.addActor(table);
		
	}

    // TODO: Move to ImageManager class
    public TextButton addButton(String name) {
        TextButton button = new TextButton(name, this.game.im.getUIButtonStyle());

        button.addListener(new UIClickListener(name.toLowerCase()));

        return button;
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
		this.gsm.set(previousState);
		previousState.waiting = false;
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

			if (this.buttonName.equals("next question")) {
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
