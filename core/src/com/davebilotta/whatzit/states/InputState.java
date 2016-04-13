package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class InputState extends State {

	private PlayState previousState;

	private Skin skin;
	private Stage stage;
	private String answer;
	private Label answerLabel;
	private boolean exit;

	private Player player;

	public InputState(WhatzIt game, GameStateManager gsm, PlayState st, Player player) {
		super(game, gsm);
		
		exit = false;
		
		this.previousState = st;
		this.answer = "";
		this.player = player;
		
		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();
		Gdx.input.setInputProcessor(stage);

		this.skin = new Skin(Gdx.files.internal("uiskin.json"),
				new TextureAtlas("uiskin.atlas"));
		setStage();
	}

	public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table(skin);
		table.setFillParent(true);

		table.row();
		table.add(this.player.getName());
		
		table.row();
		table.add("ENTER YOUR GUESS");

		table.row();
		answerLabel = new Label("", skin);
		//answerLabel.setText("");
		table.add(answerLabel);

		table.row();
		table.add(" ");
		
		String[] row1 = { "q","w","e","r","t","y","u","i","o","p"};
		createRow(table, row1);

		String[] row2 = { "a","s","d","f","g","h","j","k","l"};
		createRow(table, row2);

		String[] row3 = { "z","x","c","v","b","n","m","Back"};
		createRow(table, row3);

		String[] row6 = {"Cancel","Space","Continue"};
		createRow(table,row6);
		
		stage.addActor(table);
		
		stage.addListener(new InputListener() {
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				Utils.log(keycode);
				char c = (char)(keycode + 65);
				//Utils.log("Pressed key " + event.getCharacter());
				addLetter(String.valueOf(c));
				return false;
			}
		});
	
	}

	private void createRow(Table table, String[] layout) {
		table.row();
		
		TextureRegion region = new TextureRegion(this.game.im.getUIButton());
		TextureRegionDrawable draw = new TextureRegionDrawable(region);
		Image img = new Image(draw);
		Button bt;
		
		HorizontalGroup row = new HorizontalGroup();
		for (int i = 0; i < layout.length; i++) {
		
			bt = new Button(new Image(draw),skin);
			
			bt.addListener(new UIClickListener(layout[i]));
			bt.setName(layout[i].toLowerCase());
			
			//bt.center();
			//row.addActor(bt);
			
			ImageTextButton img_b = new ImageTextButton(layout[i].toUpperCase().trim(),skin);
			img_b.setBackground(draw);
			img_b.addListener(new UIClickListener(layout[i]));
	
			row.addActor(img_b);
			
		}

		row.align(Align.center);

		table.add(row);
	}

	public void removeLetter() {
		// Handles clicking back
		if (!this.answer.equals("")) {
			this.answer = this.answer.substring(0, this.answer.length() - 1);
		} 
	}

	public void addLetter(String key) {
		// Handles adding letter
		if (key.equals("space")) {
			this.answer += " ";
		} else {
			this.answer += key;
		}

		if (answer.equals("")) {
			answerLabel.setText(" ");
			
		}else { 
			answerLabel.setText(answer);
		}
	}

	@Override
	public void handleInput() {
		stage.act();
		this.answerLabel.setText(answer);
		answerLabel.setText(answer);
	}

	@Override
	public void update(float dt) {
		if (exit) { 
			stage.dispose();
			this.gsm.set(previousState);
			if (!this.answer.equals("")) {
				previousState.notifyGuess(this.player, answer);
			}
				
			
		} else { 
			handleInput();
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}

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