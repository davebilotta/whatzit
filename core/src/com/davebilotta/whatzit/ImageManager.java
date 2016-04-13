package com.davebilotta.whatzit;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class ImageManager {

	private WhatzIt game;
	public BitmapFont nameFont, scoreFont, timeFont;
	private Texture tileEASY, tileMEDIUM, tileHARD, tileINSANE;
	private Texture medal;
	private Texture p1, p2, p3, p4;
	private Texture button;
	private Texture bkg,bkg2;
	private Texture questionOver;
	private Texture gameOver;
	
	private Texture cancelButton,okButton,uiButton;
	
	private ArrayList<Texture> images;

	public ImageManager(WhatzIt game) {
		this.game = game;
		this.images = new ArrayList<Texture>();
		
		loadFonts();
		loadTileImages();
		loadMisc();

	}

	private void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				//Gdx.files.internal("ui/future-webfont.ttf"));
				Gdx.files.internal("ui/AlfaSlabOne-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-";

		FreeTypeFontParameter parameterSmall = new FreeTypeFontParameter();
		parameterSmall.size = 16;
		parameterSmall.characters = parameter.characters;

		FreeTypeFontParameter parameterLarge = new FreeTypeFontParameter();
		parameterLarge.size = 36;
		parameterLarge.characters = parameter.characters;

		this.nameFont = generator.generateFont(parameterSmall);
		this.nameFont.setColor(Color.BLUE);

		this.scoreFont = generator.generateFont(parameterSmall);
		this.scoreFont.setColor(Color.ORANGE);

		this.timeFont = generator.generateFont(parameterLarge);
		this.timeFont.setColor(Color.RED);
		generator.dispose();

	}

	private void loadTileImages() {
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

	private void loadMisc() {
		// this.medal = new Texture(Gdx.files.internal("flat_medal8.png"));
		this.medal = addImage("flatshadow_medal8.png");
		
		this.p1 = addImage("pieceRed_border00.png");
		this.p2 = addImage("pieceGreen_border00.png");
		this.p3 = addImage("pieceBlue_border01.png");
		this.p4 = addImage("piecePurple_border00.png");
		this.button = addImage("button.png");
		this.bkg = addImage("bkg/bkg_blue.png");
				
		this.questionOver = addImage("question_over.png");
		this.gameOver = addImage("game_over.png");
		
		this.uiButton = addImage("blue_button09.png");
		this.cancelButton = addImage("red_button09.png");
		this.okButton = addImage("green_button09.png");
				
		
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
	
	public Texture getCancelButton() { 
		return this.cancelButton;
	}
	
	public Texture getOKButton() { 
		return this.okButton;
	}
}
