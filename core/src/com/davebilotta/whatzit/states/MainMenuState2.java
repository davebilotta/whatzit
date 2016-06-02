package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

import javax.print.DocFlavor;

/**
 * Created by Dave on 4/24/2016.
 */
public class MainMenuState2 extends State {
    private Skin skin;
    private Stage stage;
    private State state;
    private int mode;
    private int difficulty;
    private String[] playerNames;
    private int numPlayers;
    private int playerId;

    public MainMenuState2(WhatzIt game, GameStateManager gsm, int mode, int difficulty, int numPlayers) {
        super(game,gsm);

        state = this;

        this.game = game;
        this.mode = mode;
        this.numPlayers = numPlayers;

        populatePlayerNames(numPlayers);

        createBasicSkin();
        setStage();

    }

    // TODO: Move this to ImageManager class?
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

    public void setStage() {
        this.stage = new Stage(new ScreenViewport());
        this.stage.clear();

        Gdx.input.setInputProcessor(stage);
        Image bkg = new Image(this.game.im.getBkg());
        bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
        stage.addActor(bkg);

        Table table = new Table();
        table.setFillParent(true);
        for (int i = 0; i < this.numPlayers; i++) {
            // Value for name in Row is "Player (i+1)" until they have a name

            ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
            buttonStyle.up = new  TextureRegionDrawable(new TextureRegion(this.game.im.getPlayerImage(i)));
            buttonStyle.down = new  TextureRegionDrawable(new TextureRegion(this.game.im.getPlayerImage(i)));

            // Show the name and icon
            HorizontalGroup row = new HorizontalGroup();
            table.row().align(Align.left);
            ImageButton icon = new ImageButton(buttonStyle);
            icon.addListener(new UIClickListener(i));
            row.addActor(icon);
            row.addActor(addText(playerNames[i]));
            table.add(row);

        }

       HorizontalGroup playRow = new HorizontalGroup();

        // play row
        table.row().padTop(getRowSpacing());;
        playRow.addActor(addButton("Play!"));
        playRow.space(getButtonSpacing());
        table.add(playRow);

        table.pad(10f);
        stage.addActor(table);
    }

    public void populatePlayerNames(int numPlayers) {
        playerNames = new String[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerNames[i] = "Player " + (i + 1);
        }
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

    @Override
    public void update(float dt) {
        handleInput();
    }

    public void handleInput() {
        //	if(Gdx.input.justTouched()){
        //      gsm.set(new PlayState(this.game, gsm));
        //}
        stage.act();
    }

    @Override
    public void render(SpriteBatch sb) {
        stage.draw();
    }

    public class UIClickListener extends ClickListener {

        private int id;
        private String buttonName;
        private boolean playerButton; // indicates that this is a player's button


        public UIClickListener(int id) {
            this.id = id;
            this.playerButton = true;
        }

        public UIClickListener(String buttonName) {
            this.buttonName = buttonName;
            this.playerButton = false;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);

            if (this.playerButton) {
               playerId = this.id;
                gsm.push(new InputState(game, gsm, state, null, "Enter name for player " + (this.id + 1)));
            }
            else play();
        }
    } // end UIClickListener class

    private void play() {
        if (!missingRequiredFields()) {
            gsm.push(new PlayState(game, gsm, mode, difficulty, playerNames));
        }
    }

    public boolean missingRequiredFields() {
        return false;
    }

    @Override
    public void notifyResponse(Player player, String response) {
        if (!response.equals("")) {
            Utils.log("Was just notified of a response: " + response + ", player " + player);
            playerNames[playerId] = response;
            setStage();
        }

}
}
