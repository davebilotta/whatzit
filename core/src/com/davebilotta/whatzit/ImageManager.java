package com.davebilotta.whatzit;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageManager {

	private WhatzIt game;
	public BitmapFont nameFont, scoreFont, timeFont, uiFont, messageFont;
    public TextField.TextFieldStyle uiFontStyle;
	private Texture tileEASY, tileMEDIUM, tileHARD, tileINSANE;
	private Texture medal1, medal2, medal3, medal4;
	private Texture p1, p2, p3, p4;
	private Texture button;
    private Texture pauseButton;
	private Texture bkg,bkg2;
	private Texture questionOver;
	private Texture gameOver;
	
	private Texture cancelButtonUp, cancelButtonDown,okButtonUp, okButtonDown,
            uiButton,uiButtonLarge,backButton, uiButtonLargeUp, uiButtonLargeDown;
    private TextButton.TextButtonStyle uiButtonStyle;

    private Texture uiButtonLargeUp2, uiButtonLargeDown2;
    private TextButton.TextButtonStyle uiButtonStyle2;
    private TextField.TextFieldStyle messageButtonStyle;

    private ArrayList<Texture> images;

	public ImageManager(WhatzIt game) {
		this.game = game;
		this.images = new ArrayList<Texture>();


		buildFonts();
		buildTileImages();
		buildMisc();

        buildSkin();

	}
	private void buildFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                // BowlbyOneSC-Regular is good
                // OverlockSC-Regular is good, but needs to be bigger
                // RubikMonoOne-Reguar isn't bad
                Gdx.files.internal("fonts/BowlbyOneSC-Regular.ttf"));
				//Gdx.files.internal("fonts/AlfaSlabOne-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-,";

		FreeTypeFontParameter parameterSmall = new FreeTypeFontParameter();
		parameterSmall.size = 16;
		parameterSmall.characters = parameter.characters;

		FreeTypeFontParameter parameterLarge = new FreeTypeFontParameter();
		parameterLarge.size = 36;
		parameterLarge.characters = parameter.characters;

        FreeTypeFontParameter uiParameter = new FreeTypeFontParameter();
        uiParameter.size = 70;
        uiParameter.characters = parameter.characters;

        FreeTypeFontParameter messageParameter = new FreeTypeFontParameter();
        messageParameter.size = 24;
        messageParameter.characters = parameter.characters;

		this.nameFont = generator.generateFont(parameterSmall);
		this.nameFont.setColor(Color.BLUE);

		this.scoreFont = generator.generateFont(parameterSmall);
		this.scoreFont.setColor(Color.ORANGE);

		this.timeFont = generator.generateFont(parameterLarge);
		this.timeFont.setColor(Color.RED);

        this.uiFont = generator.generateFont(uiParameter);
        this.uiFont.setColor(Color.MAROON);

        this.messageFont = generator.generateFont(messageParameter);
        this.messageFont.setColor(Color.WHITE);

		generator.dispose();

	}

	private void buildTileImages() {
		this.tileEASY =  addImage("tiles/tile.128.jpg");
		this.tileMEDIUM =  addImage("tiles/tile.64.jpg");;
		this.tileHARD =  addImage("tiles/tile.32.jpg");
		this.tileINSANE =  addImage("tiles/tile.16.jpg");
	}

	/* Loads assets for images in level */
	public ArrayList<Texture> loadQuestionImages(ArrayList<Question> questions) {
		// This gets called after the level is created
		// Loads assets

		Utils.log("Loading images (" + questions.size() + " questions) ... ");
		Utils.startTimer();

		ArrayList<Texture> images = new ArrayList<Texture>();
		Iterator<Question> iter = questions.iterator();

		while (iter.hasNext()) {
			Question quest = iter.next();
			//Texture texture = new Texture(Gdx.files.internal(quest
				//	.getImagePath() + ".1024" + ".jpg"));
			
			Texture texture = addImage(quest.getImagePath() + ".1024" + ".jpg"); 
			quest.setImage(texture);
		}

		Utils.log("Image load complete!");
		Utils.stopTimer();
		return images;
	}
	
	private Texture addImage(String img_name) { 
		Texture temp = new Texture(Gdx.files.internal(img_name));
		this.images.add(temp);
		return temp;
	}

	private void buildMisc() {
		// this.medal = new Texture(Gdx.files.internal("flat_medal8.png"));
		this.medal1 = addImage("medals/shaded_medal8.png");
        this.medal2 = addImage("medals/shaded_medal7.png");
        this.medal3 = addImage("medals/shaded_medal2.png");
        this.medal4 = addImage("medals/shaded_medal9.png");

		this.p1 = addImage("pieceRed_border00.png");
		this.p2 = addImage("pieceGreen_border00.png");
		this.p3 = addImage("pieceBlue_border01.png");
		this.p4 = addImage("piecePurple_border00.png");
		this.button = addImage("button.png");
        this.bkg = addImage("bkg/land_sand12.png");

		this.questionOver = addImage("question_over.png");
		this.gameOver = addImage("game_over.png");
		
		this.uiButton = addImage("ui/yellow_button10.png");
		//this.uiButtonLarge = addImage("ui/yellow_button03.png");

        this.uiButtonLarge = addImage("ui/yellow_button03.png");

        // TODO: Figure out the right down style to use
        this.uiButtonLargeUp = addImage("ui/red_button01.png");
        this.uiButtonLargeDown = addImage("ui/red_button00.png");

        this.uiButtonLargeUp2 = addImage("ui/blue_button01.png");
        this.uiButtonLargeDown2 = addImage("ui/blue_button00.png");

        //this.cancelButton = addImage("ui/red_boxCross.png");
        this.cancelButtonUp = addImage("ui/red_button01.png");
        this.cancelButtonDown = addImage("ui/red_button00.png");

        //this.okButton = addImage("ui/green_boxCheckmark.png");
        this.okButtonUp = addImage("ui/green_button04.png");
        this.okButtonDown = addImage("ui/green_button05.png");

        this.backButton = addImage("ui/yellow_sliderLeft.png");

        this.pauseButton = addImage("ui/pauseButton.png");

	}
    private void buildSkin() {
        this.uiButtonStyle = new TextButton.TextButtonStyle();
        this.uiButtonStyle.up = new TextureRegionDrawable(new TextureRegion(uiButtonLargeUp));
        this.uiButtonStyle.down = new TextureRegionDrawable(new TextureRegion(uiButtonLargeDown));
        this.uiButtonStyle.font = this.nameFont;

        this.uiButtonStyle2 = new TextButton.TextButtonStyle();
        this.uiButtonStyle2.up = new TextureRegionDrawable(new TextureRegion(uiButtonLargeUp2));
        this.uiButtonStyle2.down = new TextureRegionDrawable(new TextureRegion(uiButtonLargeDown2));
        this.uiButtonStyle2.font = this.nameFont;

        this.messageButtonStyle = new TextField.TextFieldStyle();
        this.messageButtonStyle.font = this.messageFont;
    }

	public void dispose() {
		// TODO: need to dispose of all assets
		Iterator<Texture> iter = images.iterator();

		while (iter.hasNext()) {
			iter.next().dispose();
		}
	}

	public Texture getTileImage(int num) {
		// Returns texture to use as tile image

		switch (num) {
		case 16:
			return this.tileINSANE;
		case 32:
			return this.tileHARD;
		case 64:
			return this.tileMEDIUM;
		case 128:
			return this.tileEASY;
		default:
			return this.tileEASY;
		}
	}

	public Texture getMedal(int num) {
        switch (num) {
            case 1:
                return this.medal1;
                // break;
            case 2:
                return this.medal2;
           // break;
            case 3:
                return this.medal3;
           // break;
            case 4:
                return this.medal4;
           // break;
            default:
                return this.medal1;
           // break;
        }
	}
	
	public Texture getButton() {
		return this.button;
	}

	public Texture getPlayerImage(int num) {
		// Returns texture to use as tile image

		switch (num) {
		case 0:
			return this.p1;
		case 1:
			return this.p2;
		case 2:
			return this.p3;
		case 3:
			return this.p4;
		default:
			return this.p1;
		}
	}
	
	public Texture getBkg() { 
		return this.bkg;
	}
	
	public Texture getQuestionOverImg() { 
		return this.questionOver;
	}
	public Texture getGameOverImg() {
		return this.gameOver;
	}

	public Texture getBkg2() {
		return this.bkg2;
	}
	
	public Texture getUIButton() { 
		return this.uiButton;
	}

    public Texture getPauseButton() { return this.pauseButton; }

    public Texture getUIButtonLarge() { return this.uiButtonLarge; }

    public TextButton.TextButtonStyle getUIButtonStyle() { return this.uiButtonStyle;  }

    public TextButton.TextButtonStyle getUIButtonStyle2() { return this.uiButtonStyle2;  }

    public TextField.TextFieldStyle getMessageButtonStyle() {
        Utils.log("style: " + this.messageButtonStyle);

        return this.messageButtonStyle;  }

    public Texture getCancelButton(boolean up) {

        return up ? this.cancelButtonUp : this.cancelButtonDown;
	}
	
	public Texture getOKButton(boolean up ) {
		return up ? this.okButtonUp : this.okButtonDown;
        }

    public Texture getBackButton() { return this.backButton;
    }
}
