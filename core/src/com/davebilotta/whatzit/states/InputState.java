package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class InputState extends State {

	private State previousState;

	private Skin skin;
	private Stage stage;
	private String text;
	private Label answerLabel;
	private boolean exit;
    private String topText;
	private Player player;
    private TextButton.TextButtonStyle styleLarge, styleBack, style;
    private Label.LabelStyle messageStyle;

//    private ImageButton.ImageButtonStyle styleCancel, styleOK;
private TextButton.TextButtonStyle styleCancel, styleOK;

	public InputState(WhatzIt game, GameStateManager gsm, State st, Player player, String text, String initialText) {
		super(game, gsm);

        this.topText = text;
		exit = false;
		
		this.previousState = st;
		this.text = initialText;
		this.player = player;
		
		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();
		Gdx.input.setInputProcessor(stage);

		this.skin = new Skin(Gdx.files.internal("uiskin.json"),
				new TextureAtlas("uiskin.atlas"));

        Label.LabelStyle labelStyle =  new Label.LabelStyle();
        labelStyle.font = this.game.im.messageFont;
        labelStyle.fontColor = Color.WHITE;

        skin.add("default", labelStyle);

        buildStyles();
		setStage();
	}

    public void buildStyles() {
        // TODO: Move all of these to ImageManager?
        // Normal size
        TextureRegion regionUp = new TextureRegion(this.game.im.getUIButton());
        TextureRegionDrawable drawUp = new TextureRegionDrawable(regionUp);
        TextureRegion regionDown = new TextureRegion(this.game.im.getUIButton());
        TextureRegionDrawable drawDown = new TextureRegionDrawable(regionDown);

        // Large size
        TextureRegion regionUpLarge = new TextureRegion(this.game.im.getUIButtonLarge());
        TextureRegionDrawable drawUpLarge = new TextureRegionDrawable(regionUpLarge);
        TextureRegion regionDownLarge = new TextureRegion(this.game.im.getUIButtonLarge());
        TextureRegionDrawable drawDownLarge = new TextureRegionDrawable(regionDownLarge);

        // Back button
        TextureRegion backUp = new TextureRegion(this.game.im.getBackButton());
        TextureRegion backDown = new TextureRegion(this.game.im.getBackButton());
        TextureRegionDrawable drawBackUp = new TextureRegionDrawable(backUp);
        TextureRegionDrawable drawBackDown = new TextureRegionDrawable(backDown);

        style = new TextButton.TextButtonStyle();
        styleLarge = new TextButton.TextButtonStyle();
        styleBack = new TextButton.TextButtonStyle();
        style.up = drawUp;
        style.down = drawDown;
        style.font = this.game.im.nameFont;

        styleLarge.up = drawUpLarge;
        styleLarge.down = drawDownLarge;
        styleLarge.font = this.game.im.nameFont;

        styleBack.up = drawBackUp;
        styleBack.down = drawBackDown;
        styleBack.font = this.game.im.nameFont;

        styleCancel = new TextButton.TextButtonStyle();
        styleCancel.up = new TextureRegionDrawable(new TextureRegion(this.game.im.getCancelButton(true)));
        styleCancel.down = new TextureRegionDrawable(new TextureRegion(this.game.im.getCancelButton(false)));
        styleCancel.font = this.game.im.nameFont;

        styleOK = new TextButton.TextButtonStyle();
        styleOK.up = new TextureRegionDrawable(new TextureRegion(this.game.im.getOKButton(true)));
        styleOK.down = new TextureRegionDrawable(new TextureRegion(this.game.im.getOKButton(false)));
        styleOK.font = this.game.im.nameFont;

        messageStyle = new Label.LabelStyle();
        messageStyle.font = this.game.im.messageFont;
        messageStyle.fontColor = Color.NAVY;
    }

	public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
		table.setFillParent(true);

		table.row();
		if (this.player != null) {
            table.add(this.player.getName());
        }

		table.row();
		table.add(topText);

		table.row();
		answerLabel = new Label("",messageStyle);

		table.add(answerLabel);

		table.row();
		table.add(" ");
		
		String[] row1 = { "q","w","e","r","t","y","u","i","o","p"};
		createKeyboardRow(table, row1);

		String[] row2 = { "a","s","d","f","g","h","j","k","l","back"};
		createKeyboardRow(table, row2);

		String[] row3 = { "z","x","c","v","space","b","n","m"};
		createKeyboardRow(table, row3);

        table.row();
        table.add("");

		String[] row4 = {"cancel","continue"};
		createKeyboardRow(table,row4);
		
		stage.addActor(table);
		
		stage.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Utils.log("KEY DOWN IS " + keycode);
                // letters
                if (keycode > 28 && keycode < 55) {
                    keycode += 36; // not sure why keycodes aren't corresponding to ASCII values.
                    addLetter((char) keycode);
                }
                // Space (62)
                else if (keycode == 62) {
                    addLetter("space");
                }
                // Back (67)
                else if (keycode == 67) {
                    removeLetter();
                }
                // Return (66)
                else if (keycode == 66) {
                    exit = true;
                }
                return false;

            }
        });
	}

	private void createKeyboardRow(Table table, String[] layout) {
		table.row();

        TextButton button;

		HorizontalGroup row = new HorizontalGroup();
		for (int i = 0; i < layout.length; i++) {

            boolean right = false;
            Actor actor;
            if (layout[i] == "space") {
                actor = new TextButton(layout[i].toUpperCase().trim(), styleLarge);

            }
            else if (layout[i] == "back"){
                actor = new TextButton("",styleBack);
            }
            else if (layout[i] == "cancel"){
              //  actor = new ImageButton(styleCancel);
                actor = new TextButton("Cancel",styleCancel);
                right = true;

            }
            else if (layout[i] == "continue") {
               // actor = new ImageButton(styleOK);
                actor = new TextButton("Continue",styleOK);
                right = true;
            }

            else {
                actor = new TextButton(layout[i].toUpperCase().trim(), style);
            }

            row.addActor(actor);
            actor.addListener(new UIClickListener(layout[i]));

            if (right) {
                row.align(Align.right);
            }
            row.pad(5f);
            row.space(5f);
        }

		table.add(row);
	}

	public void removeLetter() {
		// Handles clicking back
		if (!this.text.equals("")) {
			this.text = this.text.substring(0, this.text.length() - 1);
		} 
	}

    public void addLetter(char c) {
        addLetter(c + "");
    }

	public void addLetter(String key) {
		// Handles adding letter
		if (key.equals("space")) {
			this.text += " ";
		} else {
			this.text += key;
		}

		if (text.equals("")) {
			answerLabel.setText(" ");
			
		}else { 
			answerLabel.setText(text);
		}
	}

	@Override
	public void handleInput() {
		stage.act();
		this.answerLabel.setText(text);
		//answerLabel.setText(text);
	}

	@Override
	public void update(float dt) {
		if (exit) { 
			stage.dispose();
			this.gsm.set(previousState);
			if (!this.text.equals("")) {
				previousState.notifyResponse(this.player, text);
			}

		} else { 
			handleInput();
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}

    @Override
    public void notifyResponse(Player player, String response) {}

    public class UIClickListener extends ClickListener {

		private String buttonName;

		public UIClickListener(String buttonName) {
			this.buttonName = buttonName.toLowerCase();
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			Utils.log(this.buttonName);

			if (this.buttonName.equals("cancel")) { 
				exit = true;																																																																																																												
			}
			else if (this.buttonName.equals("continue")) {
				exit = true;
			}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									
			else if (this.buttonName.equals("back")) {
				removeLetter();
			} else {
				addLetter(this.buttonName);
			}
		}
	}



}