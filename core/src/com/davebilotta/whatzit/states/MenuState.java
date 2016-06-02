package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class MenuState extends State {

	private Skin skin;
	private Stage stage;
	
	private static int difficulty;
	private static int mode;
	private static int numPlayers;

    @Override
    public void notifyResponse(Player player, String response) {

    }

    public MenuState(WhatzIt game, GameStateManager gsm) {
		super(game,gsm);
		
		this.game = game;
		
		this.stage = new Stage(new ScreenViewport());
		this.stage.clear();
			
		Gdx.input.setInputProcessor(stage);
		
		createBasicSkin();
		setStage();
	}

    // TODO: Move this to ImageManager class
	private void createBasicSkin(){
		  //Create a font
		  BitmapFont font = new BitmapFont();
		  skin = new Skin();
		  skin.add("default", font);

		  //Create a texture
		// Generate a 1x1 white texture and store it in the skin named "white".
		  Pixmap pixmap = new Pixmap(140, 60, Pixmap.Format.RGBA8888);
		  pixmap.setColor(Color.WHITE);
		  pixmap.fill();
		  skin.add("background",new Texture(pixmap));

		  //Create a button style
		  TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		  textButtonStyle.up = skin.newDrawable("background", Color.ORANGE);
		  textButtonStyle.down = skin.newDrawable("background", Color.NAVY);
		  textButtonStyle.checked = skin.newDrawable("background", Color.NAVY);
		  textButtonStyle.over = skin.newDrawable("background", Color.WHITE);
		  textButtonStyle.font = skin.getFont("default");

		  skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle =  new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.NAVY;

        skin.add("default", textFieldStyle);

		}

	@Override
	public void handleInput() {
	//	if(Gdx.input.justTouched()){
      //      gsm.set(new PlayState(this.game, gsm));
        //}
		stage.act();
	}

    public void nextStage() {
        if ( (MenuState.mode > 0) && (MenuState.difficulty > 0) && (MenuState.numPlayers > 0) ) {
            stage.dispose();
            //gsm.set(new PlayState(this.game, gsm, MenuState.mode, MenuState.difficulty, MenuState.numPlayers));
            gsm.set(new MainMenuState2(this.game, gsm, MenuState.mode, MenuState.difficulty, MenuState.numPlayers));
        }
    }


	@Override
	public void update(float dt) {
		handleInput();
				
	}

    public TextField addText(String text) {
        return new TextField(text,skin);

    }

    public TextButton addButton(String name, ButtonGroup buttonGroup) {
    	TextButton button = addButton(name);
    	buttonGroup.add(button);

    	return button;
    }
    
    public TextButton addButton(String name) {
    	TextButton button = new TextButton(name,skin);
    	button.addListener(new UIClickListener(name.toLowerCase()));
    	
    	return button;
    }

    public float getButtonSpacing() {
        // TODO: Eventually have this be dynamic
        return 30f;
    }

    public float getRowSpacing() {
    return 20f;
    }

	public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table();
		table.setFillParent(true);
		HorizontalGroup row1 = new HorizontalGroup();
		HorizontalGroup row2 = new HorizontalGroup();
		HorizontalGroup row3 = new HorizontalGroup();
		HorizontalGroup playRow = new HorizontalGroup();
		
		// Game mode
        // Row 1 label
		table.row().align(Align.left);
        HorizontalGroup row1Label = new HorizontalGroup();
        row1Label.addActor(addText("Game Mode"));
        table.add(row1Label);

        // Row 1
        table.row().align(Align.left);
		ButtonGroup row1_buttonGroup = new ButtonGroup();
		row1.addActor(addButton("Normal", row1_buttonGroup));
		row1.addActor(addButton("Macro",row1_buttonGroup));
		row1.addActor(addButton("Mixed", row1_buttonGroup));
		row1_buttonGroup.setMaxCheckCount(1);
		row1_buttonGroup.uncheckAll();
        row1.space(getButtonSpacing());
        table.add(row1);

        //
		// Difficulty
        //
        table.row().align(Align.left).padTop(getRowSpacing());
        HorizontalGroup row2Label = new HorizontalGroup();
        row2Label.addActor(addText("Difficulty"));
        table.add(row2Label);

		table.row().align(Align.left);
		ButtonGroup row2_buttonGroup = new ButtonGroup();
		row2_buttonGroup.setMaxCheckCount(1);
        row2.addActor(addButton("Easy", row2_buttonGroup));
		row2.addActor(addButton("Medium",row2_buttonGroup));
		row2.addActor(addButton("Hard", row2_buttonGroup));
		row2.addActor(addButton("Insane", row2_buttonGroup));
		row2_buttonGroup.uncheckAll();

		//row2.align(Align.left);
		row2.sizeBy(0.5f);
        row2.space(getButtonSpacing());
        table.add(row2);

        //
        // Number of players
        //
        table.row().align(Align.left).padTop(getRowSpacing());
        HorizontalGroup row3Label = new HorizontalGroup();
        row3Label.addActor(addText("Number Of Players"));
        table.add(row3Label);

        //
		table.row();
		ButtonGroup row3_buttonGroup = new ButtonGroup();
		row3_buttonGroup.setMaxCheckCount(1);
		row3.addActor(addButton("One",row3_buttonGroup));
		row3.addActor(addButton("Two",row3_buttonGroup));
		row3.addActor(addButton("Three", row3_buttonGroup));
        row3.addActor(addButton("Four", row3_buttonGroup));
		row3_buttonGroup.uncheckAll();

        row3.align(Align.left);

		row3.sizeBy(0.5f);
        row3.space(getButtonSpacing());
		table.add(row3);
		
		
		// play row 
        table.row().padTop(getRowSpacing());;
		playRow.addActor(addButton("Next >>>"));
        playRow.space(getButtonSpacing());
		table.add(playRow);

		table.pad(10f);
		stage.addActor(table);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		stage.draw();
	}
	
	public class UIClickListener extends ClickListener {

		private String button;
		public UIClickListener(String button) {
			this.button = button;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			Utils.log("Clicked " + this.button);
			super.clicked(event, x, y);
			
			// Mode
			if (this.button.equals("normal")) {
				MenuState.mode = 1;
				return;
			}
			else if (this.button.equals("macro")) {
				MenuState.mode = 2;
			}
			else if (this.button.equals("mixed")) {
				MenuState.mode = 3;
			}
			
			// Difficulty
			else if (this.button.equals("easy")) {
				MenuState.difficulty = 1;
			}
			else if (this.button.equals("medium")) {
				MenuState.difficulty = 2;
			}
			else if (this.button.equals("hard")) {
				MenuState.difficulty = 3;
			}
			else if (this.button.equals("insane")) {
				MenuState.difficulty = 4;
			}
			
			// Number of players 
			else if (this.button.equals("one")) { 
				MenuState.numPlayers = 1;
			}
			else if (this.button.equals("two")) {
				MenuState.numPlayers = 2;
			}
			else if (this.button.equals("three")) {
				MenuState.numPlayers = 3;
			} 
			else if (this.button.equals("four")) {
				MenuState.numPlayers = 4;
			}
			
			else {
                nextStage();
			}
			
			Utils.log("Received button event = current difficulty is " + MenuState.difficulty + ", mode is " + MenuState.mode + ", num players is " + MenuState.numPlayers);
		} 
		
	}


}

