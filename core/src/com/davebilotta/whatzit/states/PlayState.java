package com.davebilotta.whatzit.states;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.davebilotta.whatzit.Question;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class PlayState extends State {

	public enum MODE {
		NORMAL, MACRO, MIXED
	}

	private enum DIFF {
		EASY, MEDIUM, HARD, INSANE
	}

	private boolean gameover, questionover, paused;
	public boolean waiting;
	private DIFF difficulty;
	private MODE mode;
	private int numPlayers;
	private float delay, tick;

	private Player[] players;
    private boolean init = false;
    private Rectangle[] playerRect;

	public int numQuestions;
	public ArrayList<Question> questions;
	public ArrayList<Texture> textures;

	private Stage stage;
	private Skin skin;

    private int touchPlayer;

    // TODO: Figure offset out for real
    // These are used for rendering UI
    int yOffset = 10;
    int s = 10;
    int scoreSpacer = 50;
    int left = 5;
    int right = 5;

	private float questionOverDelay = 5f;
    GlyphLayout layout = new GlyphLayout();

    private void createBasicSkin() {
		// Create a font
		BitmapFont font = new BitmapFont();
		skin = new Skin();
		skin.add("default", font);

		// Create a texture
		Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 4,
				(int) Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background", new Texture(pixmap));

		// Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("background",
				Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

	}

	public PlayState(WhatzIt game, GameStateManager gsm, int mode,
			int difficulty, int numPlayers) {
		super(game, gsm);

		createBasicSkin();
		
		// this.stage = new Stage(new FitViewport(this.game.WIDTH,200,new
		// OrthographicCamera(this.game.WIDTH,100)));
		// stage.clear();

		this.game.qm.loadQuestions();
		
		this.numPlayers = numPlayers;
		switch (mode) {
		case 1:
			this.mode = MODE.NORMAL;
			break;
		case 2:
			this.mode = MODE.MACRO;
			break;
		case 3:
			this.mode = MODE.MIXED;
			break;
		}

		int ts;
        float del;
		switch (difficulty) {
		case 1:
			this.difficulty = DIFF.EASY;
			ts = 128; del = 7;
			break;

		case 2:
			this.difficulty = DIFF.MEDIUM;
			ts = 64; del = 5;
			break;

		case 3:
			this.difficulty = DIFF.HARD;
			ts = 32; del = 3;
			break;

		case 4:
			this.difficulty = DIFF.INSANE;
			ts = 16; del = 2;
			break;
		default:
			this.difficulty = DIFF.HARD;
			ts = 32; del = 5;
			break;
		}

		this.game.tm.setTileSize(ts);

		// TODO: *** Change this back later:
		this.delay = del;
		// this.delay = 0.01f;

		players = new Player[numPlayers];
        playerRect = new Rectangle[numPlayers];

		// TODO: Eventually pass all these in
		// names = new String[numPlayers];
		// playerNames = new String[numPlayers];
		//
		// TODO: Eventually pass in names of players
		// { "Dave", "Leana", "Lukas", "Jonah" };

       // renderPlayerInfo(sb,true);

		for (int i = 0; i < numPlayers; i++) {
			// playerNames[i] = "Player " + (i+1);
			players[i] = new Player(i, "Player " + (i + 1));

		}
		// TODO: Eventually replace this when there are more than 4 questions available
		numQuestions = 4;

		// TODO: Show "loading" indicator here
		// TODO: This is redundant for now
		this.questions = this.game.qm.buildLevel(numQuestions);
		this.textures = this.game.im.loadQuestionImages(questions);

		gameover = false;
		paused = false;

       	Utils.log("New game created: number of players = " + this.numPlayers
				+ ", speed = " + this.difficulty + ", mode = " + this.mode);
		// Utils.log("Player names are " + Utils.pretty(playerNames, ","));

		// now get the first question
		this.game.qm.nextQuestion();
	}

	@Override
	public void handleInput() {
		/* Here we need to check if the user buzzed in */

		if (Gdx.input.justTouched()) {
           // Utils.log("Touched X " + Gdx.input.getX());
            // Create touch rectangle

            Rectangle touchRect = new Rectangle(Gdx.input.getX(),(this.game.HEIGHT - Gdx.input.getY()),10,10);

            // Check overlap with player
            if (touchedPlayer(touchRect)) {
                this.gsm.push(new InputState(this.game, this.gsm, this, players[touchPlayer], "Enter Your Guess"));
            }
            else {
                // if touchedPause(touchRect) {
                // this.gsm.push(new PauseState(this.game, this.gsm, this));
                // }
            }
		}
	}

    public boolean touchedPlayer(Rectangle touchRect) {
        boolean touched = false;

        for (int i = 0; i < players.length; i++) {
            //Utils.log("Click is at " + touchRect + ", Checking player " + i + players[i].getRect());

            if (players[i].getRect().overlaps(touchRect)) {
                touchPlayer = i;
                touched = true;

            }
        }

        return touched;
    }

	// This gets called when returning from the InputState - needs to check
	// guess
	public void notifyGuess(Player player, String userGuess) {
		if (!userGuess.equals("")) {
			//
			Utils.log("Was just notified of a guess: " + userGuess);

			// If a correct guess, change to QuestionOverState, passing in
			// player and score
			if (this.game.qm.checkGuess(userGuess)) {
				int score = this.game.tm.getWinScore();

				player.correct(score);

				questionover = true;
				this.gsm.push(new QuestionOverState(this.game, this.gsm, this,
						player, score));

			} else {
				player.incorrect(this.game.tm.getTileValue());
			}
		}
	}

	@Override
	public void update(float dt) {
		handleInput();

		if (paused || gameover || waiting) {
			Utils.log("Paused/Game Over/Waiting, nothing to do here");
		} else if (questionover) {
			if (!this.game.qm.hasQuestions()) {
				gameover = true;
				this.gsm.push(new GameOverState(this.game, this.gsm, this,
						this.players));
			} else {
				Utils.log("Question over, getting next question");
				this.game.qm.nextQuestion();
				this.game.tm.nextQuestion();
				questionover = false;
			}
		} else {
			this.tick += dt;
			if (this.tick >= this.delay) {

				if (!this.game.tm.hasTiles()) {
					questionover = true;
					waiting = true;
					// this.gsm.push(new QuestionOverState(this.game, this.gsm,
					// this, null));

					Random rand = new Random();
					int score = rand.nextInt(500);
					int p = rand.nextInt(players.length);

					players[p].correct(score);
					this.gsm.push(new QuestionOverState(this.game, this.gsm,
							this, players[p], score));

					if (!this.game.qm.hasQuestions()) {
						gameover = true;
						this.gsm.push(new GameOverState(this.game, this.gsm,
								this, this.players));
					} else {
						//
						// OK to grab next question here - won't be rendered
						// until done with QuestionOverState

						Utils.log("Getting next question");
						this.game.qm.nextQuestion();
						this.game.tm.nextQuestion();
						questionover = false;

					}
				}

				else {
					this.game.tm.removeNextTile();

					this.tick = 0;
				}
			}
		}
		cam.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(cam.combined);

		sb.begin();

		if (paused) {

		}
		if (questionover) {
			// TODO: Present summary:
			// Show answer and winner/points
			// pause for X seconds

		}
		if (gameover) {
			// TODO: Present summary of game - winner
			// Leave this here so questionover logic runs first
			renderBackground(sb);
			renderUI(sb);
		} else {

			renderBackground(sb);

			this.game.tm.renderTiles(sb);

			renderUI(sb);

		}

		sb.end();
	}

	public void renderBackground(SpriteBatch sb) {
		// Texture bkg = this.questions.get(questionNum).getImage();
		Texture bkg = this.game.qm.getQuestionImage();
		sb.draw(bkg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderUI(SpriteBatch sb) {

        // render players
		renderPlayerInfo(sb);

		// render time

		float time = this.delay - this.tick;
		int t = (int) Math.ceil((double) time);
		//// TODO: Need to fix these
		// float timeWidth = this.game.im.timeFont.getBounds(t + "").width;
        String timeString = Integer.toString(t);
        layout.setText(this.game.im.timeFont, timeString);
        float timeWidth = layout.width + right;
		this.game.im.timeFont.draw(sb, timeString, this.game.WIDTH - timeWidth,
				this.game.HEIGHT - yOffset);
	}

    public void renderPlayerInfo(SpriteBatch sb) {
        // TODO:
        // Need to
        left = right = 5;
        for (int i = 0; i < this.numPlayers; i++) {
            //
            // Draw image
            Texture img = this.game.im.getPlayerImage(i);
            int p_w = img.getWidth();
            int p_h = img.getHeight();

            // Draw image
            int yPos = this.game.HEIGHT - yOffset - p_h;

            // First time through - need to set positions of all players
            if (!init) {
                players[i].setPos(left,yPos,p_w,p_h);
                if (i == this.numPlayers -1) {
                    Utils.log("Flipping");
                    init = true;
                }
            }
            sb.draw(img, left, yPos);

            //
            // Draw name
                this.game.im.nameFont.draw(sb, players[i].getName(), left + p_w,
                        this.game.HEIGHT - yOffset);
                layout.setText(this.game.im.nameFont, players[i].getName());
            float nameWidth = layout.width;

            // Draw score

                this.game.im.scoreFont.draw(sb, pad(players[i].getScore()), (left
                        + p_w + +nameWidth + s), this.game.HEIGHT - yOffset);
                layout.setText(this.game.im.nameFont, Integer.toString(players[i].getScore()));

            float scoreWidth = layout.width;

            left += (nameWidth + scoreWidth + scoreSpacer + p_w);
        }
    }
    public void renderUINew(SpriteBatch sb) {

		Table table = new Table();
		table.setFillParent(true);

		HorizontalGroup group = new HorizontalGroup();
		group.align(Align.center);
		group.addActor(new TextButton("Dave", skin));

		table.add(group);
		table.row();

		stage.addActor(table);

		stage.act();
		stage.draw();

	}

	public String pad(int num) {
		return String.format("%03d", num);
	}

}
