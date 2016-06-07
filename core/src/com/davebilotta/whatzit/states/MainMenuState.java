package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class MainMenuState extends State {

	private Skin skin, skinLabel;
	private Stage stage;
	
	private static int difficulty;
	private static int mode;
	private static int numPlayers;

    // These are fields for the descriptor rows below the choices
    // Init text as nil
    private String gameModeRowLabelText = "";
    private TextField gameModeRowLabelTextField;

    private String difficultyRowLabelText = "";
    private TextField difficultyRowLabelTextField;

    private String numPlayersRowLabelText = "";
    private TextField numPlayersRowLabelTextField;

    @Override
    public void notifyResponse(Player player, String response) {

    }

    public MainMenuState(WhatzIt game, GameStateManager gsm) {
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
		  textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		  textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		  textButtonStyle.over = skin.newDrawable("background", Color.CORAL);
		  textButtonStyle.font = skin.getFont("default");

		  skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle =  new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.NAVY;

        skin.add("default", textFieldStyle);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/AlfaSlabOne-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        BitmapFont labelFont = generator.generateFont(parameter);
        generator.dispose();

        skinLabel = new Skin();
        skinLabel.add("default", labelFont);
        TextField.TextFieldStyle labelStyle =  new TextField.TextFieldStyle();
       // labelStyle.font = skin.getFont("default");
       labelStyle.font = labelFont;
        labelStyle.fontColor = Color.TAN;
        skinLabel.add("default",labelStyle);

        }

	@Override
	public void handleInput() {
	//	if(Gdx.input.justTouched()){
      //      gsm.set(new PlayState(this.game, gsm));
        //}
		stage.act();
	}

    public void nextStage() {
        if ( (MainMenuState.mode > 0) && (MainMenuState.difficulty > 0) && (MainMenuState.numPlayers > 0) ) {
            stage.dispose();
            //gsm.set(new PlayState(this.game, gsm, MainMenuState.mode, MainMenuState.difficulty, MainMenuState.numPlayers));
            gsm.set(new MainMenuState2(this.game, gsm, MainMenuState.mode, MainMenuState.difficulty, MainMenuState.numPlayers));
        }
    }


	@Override
	public void update(float dt) {
		handleInput();
				
	}

    public TextField addText(String text) {
        return new TextField(text,skin);

    }

    public TextField addText(String text, Skin skin) {
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

        //
        // Game mode
        //
        table.row().align(Align.left);
        HorizontalGroup row1Label = new HorizontalGroup();
        row1Label.addActor(addText("Game Mode"));
        table.add(row1Label);

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
        // Game Mode explanation
        //
        table.row().align(Align.left);
        gameModeRowLabelTextField = addText(gameModeRowLabelText,skinLabel);
        table.add(gameModeRowLabelTextField).minWidth(500);

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

		row2.sizeBy(0.5f);
        row2.space(getButtonSpacing());
        table.add(row2);

        //
        // Difficulty explanation
        //
        table.row().align(Align.left);
        difficultyRowLabelTextField = addText(difficultyRowLabelText,skinLabel);
        table.add(difficultyRowLabelTextField).minWidth(500);

        //
        // Number of players
        //
        table.row().align(Align.left).padTop(getRowSpacing());
        HorizontalGroup row3Label = new HorizontalGroup();
        row3Label.addActor(addText("Number Of Players"));
        table.add(row3Label);

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

        //
        // Number Of Players Explanation
        //
        table.row().align(Align.left);
        numPlayersRowLabelTextField = addText(numPlayersRowLabelText,skinLabel);
        table.add(numPlayersRowLabelTextField).minWidth(500);

        //
		// Play row
        //
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
				MainMenuState.mode = 1;
                gameModeRowLabelText = "Normal images";
			}
			else if (this.button.equals("macro")) {
				MainMenuState.mode = 2;
                gameModeRowLabelText = "Macro images";
			}
			else if (this.button.equals("mixed")) {
				MainMenuState.mode = 3;
                gameModeRowLabelText = "A mixture of normal and macro images";

			}

			// Difficulty
			else if (this.button.equals("easy")) {
				MainMenuState.difficulty = 1;
                difficultyRowLabelText = "Large tiles, slow speed";
			}
			else if (this.button.equals("medium")) {
				MainMenuState.difficulty = 2;
                difficultyRowLabelText = "Medium tiles, Medium speed";
			}
			else if (this.button.equals("hard")) {
				MainMenuState.difficulty = 3;
                difficultyRowLabelText = "Small tiles, fast speed";
			}
			else if (this.button.equals("insane")) {
				MainMenuState.difficulty = 4;
                difficultyRowLabelText = "Tiny tiles, very fast speed";
			}
			
			// Number of players 
			else if (this.button.equals("one")) { 
				MainMenuState.numPlayers = 1;
                numPlayersRowLabelText = "Solo mode!";
			}
			else if (this.button.equals("two")) {
				MainMenuState.numPlayers = 2;
                numPlayersRowLabelText = "Two-on-two";
            }
			else if (this.button.equals("three")) {
				MainMenuState.numPlayers = 3;
                numPlayersRowLabelText = "Three's a crowd";
            }
			else if (this.button.equals("four")) {
				MainMenuState.numPlayers = 4;
                numPlayersRowLabelText = "The max";
            }
			
			else {
                nextStage();
			}
			
			Utils.log("Received button event = current difficulty is " + MainMenuState.difficulty + ", mode is " + MainMenuState.mode + ", num players is " + MainMenuState.numPlayers);

            gameModeRowLabelTextField.setText(gameModeRowLabelText);
            difficultyRowLabelTextField.setText(difficultyRowLabelText);
            numPlayersRowLabelTextField.setText(numPlayersRowLabelText);

        }
		
	}

}

