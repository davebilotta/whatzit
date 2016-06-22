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

public class MainMenuState2 extends State {

	private Skin skin, skinLabel;
	private Stage stage;
	private Label.LabelStyle messageStyle, uiFontStyle;

	private static int difficulty;
	private static int mode;
	private static int numPlayers;

    // These are fields for the descriptor rows below the choices
    // Init text as nil
    private String gameModeRowLabelText = "";
    private Label gameModeRowLabel;

    private String difficultyRowLabelText = "";
    private Label difficultyRowLabel;

    private String numPlayersRowLabelText = "";
    private Label numPlayersRowLabel;

    @Override
    public void notifyResponse(Player player, String response) {  }

    public MainMenuState2(WhatzIt game, GameStateManager gsm) {
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AlfaSlabOne-Regular.ttf"));
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

        uiFontStyle = new Label.LabelStyle();
        uiFontStyle.font = this.game.im.messageFont;
        uiFontStyle.fontColor = Color.BLUE;

        messageStyle = new Label.LabelStyle();
        messageStyle.font = this.game.im.nameFont;
        messageStyle.fontColor = Color.WHITE;

        }

	@Override
	public void handleInput() {
	//	if(Gdx.input.justTouched()){
      //      gsm.set(new PlayState(this.game, gsm));
        //}
		stage.act();
	}

    public void previousStage() {
        stage.dispose();
            gsm.set(new MainMenuState(this.game, this.gsm));

    }

    public void nextStage() {
        if ( (MainMenuState2.mode > 0) && (MainMenuState2.difficulty > 0) && (MainMenuState2.numPlayers > 0) ) {
            stage.dispose();
            gsm.set(new MainMenuState3(this.game, this.gsm, MainMenuState2.mode, MainMenuState2.difficulty, MainMenuState2.numPlayers));
        }
    }


	@Override
	public void update(float dt) {
		handleInput();
				
	}

    public Label addLabel(String text) {
        return new Label(text,messageStyle);
    }

    public Label addLabel(String text, Label.LabelStyle style) {
        return new Label(text,style);
    }


    public TextField addText(String text) {
        return new TextField(text,skin);

    }

    public TextField addText(String text, Skin skin) {
        return new TextField(text,skin);

    }

    public TextButton addButton(String name, ButtonGroup buttonGroup, boolean navStyle) {
    	TextButton button = addButton(name,navStyle);
    	buttonGroup.add(button);

    	return button;
    }
    
    public TextButton addButton(String name, boolean navStyle) {
    	TextButton button;

        if (!navStyle) {
            button = new TextButton(name, this.game.im.getUIButtonStyle2());
        }
        else {
            button = new TextButton(name, this.game.im.getUIButtonStyle());
        }
    	button.addListener(new UIClickListener(name.toLowerCase()));
    	
    	return button;
    }

    public float getButtonSpacing() {
        // TODO: Eventually have this be dynamic
        return 30f;
    }

    public float getRowSpacing() {
    return 30f;
    }

	public void setStage() {
		Image bkg = new Image(this.game.im.getBkg());
		bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
		stage.addActor(bkg);
		
		Table table = new Table();
        //table.setDebug(true);

		table.setFillParent(true);
        table.setHeight(this.game.HEIGHT);

		HorizontalGroup row1_H = new HorizontalGroup();
        VerticalGroup row1 = new VerticalGroup();

		HorizontalGroup row2_H = new HorizontalGroup();
        VerticalGroup row2 = new VerticalGroup();

		HorizontalGroup row3_H = new HorizontalGroup();
        VerticalGroup row3 = new VerticalGroup();

        HorizontalGroup playRow = new HorizontalGroup();

        VerticalGroup buttons = new VerticalGroup();

        //
        // Game mode
        //
        table.row().align(Align.left);
        HorizontalGroup row1Label = new HorizontalGroup();
        row1Label.addActor(addLabel("Game Mode",uiFontStyle));
        row1.addActor(row1Label);

        table.row().align(Align.left);
		ButtonGroup row1_buttonGroup = new ButtonGroup();
		row1_H.addActor(addButton("Normal", row1_buttonGroup,false));
		row1_H.addActor(addButton("Macro",row1_buttonGroup,false));
		row1_H.addActor(addButton("Mixed", row1_buttonGroup,false));
		row1_buttonGroup.setMaxCheckCount(1);
		row1_buttonGroup.uncheckAll();
        row1_H.space(getButtonSpacing());
        row1.addActor(row1_H);

        //
        // Game Mode explanation
        //
        table.row().align(Align.left).padBottom(getRowSpacing());
        gameModeRowLabel = addLabel(gameModeRowLabelText);
        row1.addActor(gameModeRowLabel);

        buttons.addActor(row1);

        //
		// Difficulty
        //
        table.row().align(Align.left);
        HorizontalGroup row2Label = new HorizontalGroup();
        row2Label.addActor(addLabel("Difficulty",uiFontStyle));
       // table.add(row2Label);
        row2.addActor(row2Label);

		table.row().align(Align.left);
		ButtonGroup row2_buttonGroup = new ButtonGroup();
		row2_buttonGroup.setMaxCheckCount(1);
        row2_H.addActor(addButton("Easy", row2_buttonGroup,false));
		row2_H.addActor(addButton("Medium",row2_buttonGroup,false));
		row2_H.addActor(addButton("Hard", row2_buttonGroup,false));
		row2_H.addActor(addButton("Insane", row2_buttonGroup,false));
		row2_buttonGroup.uncheckAll();

		row2_H.sizeBy(0.5f);
        row2_H.space(getButtonSpacing());
        row2.addActor(row2_H);

        //
        // Difficulty explanation
        //
        table.row().align(Align.left).padBottom(getRowSpacing());
        difficultyRowLabel = addLabel(difficultyRowLabelText);
        row2.addActor(difficultyRowLabel);

        buttons.addActor(row2);
        //
        // Number of players
        //
        table.row().align(Align.left);
        HorizontalGroup row3Label = new HorizontalGroup();
        row3Label.addActor(addLabel("Number Of Players",uiFontStyle));
        row3.addActor(row3Label);

		table.row();
		ButtonGroup row3_buttonGroup = new ButtonGroup();
		row3_buttonGroup.setMaxCheckCount(1);
		row3_H.addActor(addButton("One",row3_buttonGroup,false));
		row3_H.addActor(addButton("Two",row3_buttonGroup,false));
		row3_H.addActor(addButton("Three", row3_buttonGroup,false));
        row3_H.addActor(addButton("Four", row3_buttonGroup,false));
		row3_buttonGroup.uncheckAll();

        row3_H.align(Align.left);
		row3_H.sizeBy(0.5f);
        row3_H.space(getButtonSpacing());
        row3.addActor(row3_H);

        //
        // Number Of Players Explanation
        //
        table.row().align(Align.left).padBottom(getRowSpacing());
        numPlayersRowLabel = addLabel(numPlayersRowLabelText);
        row3.addActor(numPlayersRowLabel);

        buttons.addActor(row3);

       /* buttons.setOrigin(buttons.getWidth() /2,buttons.getHeight()/2);
        buttons.setPosition(Gdx.graphics.getWidth() /2 - (buttons.getWidth() /2),
                Gdx.graphics.getHeight()/2 - (buttons.getHeight()/2));
       */

        table.add(buttons).fillY().expand().align(Align.bottom).height(this.game.HEIGHT * 0.40f);

        buttons.space(40f);

        //
		// Play row
        //
        table.row().padTop(getRowSpacing());
        playRow.addActor(addButton("Back",true));
		playRow.addActor(addButton("Next",true));
        playRow.align(Align.bottom).pad(20f);

       playRow.space(getButtonSpacing());

		table.add(playRow).height(this.game.HEIGHT * 0.30f);

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
				MainMenuState2.mode = 1;
                gameModeRowLabelText = "Normal images";
			}
			else if (this.button.equals("macro")) {
				MainMenuState2.mode = 2;
                gameModeRowLabelText = "Close-up images";
			}
			else if (this.button.equals("mixed")) {
				MainMenuState2.mode = 3;
                gameModeRowLabelText = "A medley of normal and macro images";

			}

			// Difficulty
			else if (this.button.equals("easy")) {
				MainMenuState2.difficulty = 1;
                difficultyRowLabelText = "Large tiles, slow speed";
			}
			else if (this.button.equals("medium")) {
				MainMenuState2.difficulty = 2;
                difficultyRowLabelText = "Medium tiles, Medium speed";
			}
			else if (this.button.equals("hard")) {
				MainMenuState2.difficulty = 3;
                difficultyRowLabelText = "Small tiles, fast speed";
			}
			else if (this.button.equals("insane")) {
				MainMenuState2.difficulty = 4;
                difficultyRowLabelText = "Tiny tiles, very fast speed";
			}
			
			// Number of players 
			else if (this.button.equals("one")) { 
				MainMenuState2.numPlayers = 1;
                numPlayersRowLabelText = "Solo mode!";
			}
			else if (this.button.equals("two")) {
				MainMenuState2.numPlayers = 2;
                numPlayersRowLabelText = "Two-on-two";
            }
			else if (this.button.equals("three")) {
				MainMenuState2.numPlayers = 3;
                numPlayersRowLabelText = "Three's a crowd";
            }
			else if (this.button.equals("four")) {
				MainMenuState2.numPlayers = 4;
                numPlayersRowLabelText = "Maxed Out";
            }

            else if (this.button.equals("back")) {
                previousStage();
            }
			else {
                nextStage();
			}
			
			Utils.log("Received button event = current difficulty is " + MainMenuState2.difficulty + ", mode is " + MainMenuState2.mode + ", num players is " + MainMenuState2.numPlayers);

            gameModeRowLabel.setText(gameModeRowLabelText);
            difficultyRowLabel.setText(difficultyRowLabelText);
            numPlayersRowLabel.setText(numPlayersRowLabelText);

        }
		
	}

}

