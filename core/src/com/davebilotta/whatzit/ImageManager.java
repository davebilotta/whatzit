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
	public BitmapFont nameFont, scoreFont, timeFont, uiFont;
    public TextField.TextFieldStyle uiFontStyle;
	private Texture tileEASY, tileMEDIUM, tileHARD, tileINSANE;
	private Texture medal;
	private Texture p1, p2, p3, p4;
	private Texture button;
	private Texture bkg,bkg2;
	private Texture questionOver;
	private Texture gameOver;
	
	private Texture cancelButton,okButton,uiButton,uiButtonLarge,backButton, uiButtonLargeUp, uiButtonLargeDown;

    private TextButton.TextButtonStyle uiButtonStyle;

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
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-";

		FreeTypeFontParameter parameterSmall = new FreeTypeFontParameter();
		parameterSmall.size = 16;
		parameterSmall.characters = parameter.characters;

		FreeTypeFontParameter parameterLarge = new FreeTypeFontParameter();
		parameterLarge.size = 36;
		parameterLarge.characters = parameter.characters;

        FreeTypeFontParameter uiParameter = new FreeTypeFontParameter();
        uiParameter.size = 60;
        uiParameter.characters = parameter.characters;

		this.nameFont = generator.generateFont(parameterSmall);
		this.nameFont.setColor(Color.BLUE);

		this.scoreFont = generator.generateFont(parameterSmall);
		this.scoreFont.setColor(Color.ORANGE);

		this.timeFont = generator.generateFont(parameterLarge);
		this.timeFont.setColor(Color.RED);

        this.uiFont = generator.generateFont(uiParameter);
        this.uiFont.setColor(Color.MAROON);

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
		this.medal = addImage("flatshadow_medal8.png");
		
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

        this.cancelButton = addImage("ui/red_boxCross.png");
		this.okButton = addImage("ui/green_boxCheckmark.png");
		this.backButton = addImage("ui/yellow_sliderLeft.png");
	}


    private void buildSkin() {
        this.uiButtonStyle = new TextButton.TextButtonStyle();
        this.uiButtonStyle.up = new TextureRegionDrawable(new TextureRegion(uiButtonLargeUp));
        this.uiButtonStyle.down = new TextureRegionDrawable(new TextureRegion(uiButtonLargeDown));
        this.uiButtonStyle.font = this.nameFont;
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

	public Texture getMedal() {
		return this.medal;
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

    public Texture getUIButtonLarge() { return this.uiButtonLarge; }

    public TextButton.TextButtonStyle getUIButtonStyle() { return this.uiButtonStyle;  }
	
	public Texture getCancelButton() { 
		return this.cancelButton;
	}
	
	public Texture getOKButton() { 
		return this.okButton;
	}

    public Texture getBackButton() { return this.backButton;
    }
}
