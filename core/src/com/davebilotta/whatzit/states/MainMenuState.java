package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

/**
 * Created by Dave on 6/7/2016.
 */
public class MainMenuState extends State {

    private Stage stage;
    private Skin skin,skinLabel;
    private WhatzIt gameID;
    private GameStateManager gameStateManagerID;

    public MainMenuState(WhatzIt game, GameStateManager gsm) {
        super(game,gsm);

        this.game = game;

        gameID = game;
        gameStateManagerID = gsm;

        this.stage = new Stage(new ScreenViewport());
        this.stage.clear();

        Gdx.input.setInputProcessor(stage);

        createBasicSkin();
        setStage();
    }

    private void setStage() {
            Image bkg = new Image(this.game.im.getBkg());
            bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
            stage.addActor(bkg);


            Table table = new Table();
            table.setFillParent(true);

            table.setDebug(false);

            HorizontalGroup row1 = new HorizontalGroup();
            HorizontalGroup row2 = new HorizontalGroup();
            HorizontalGroup row3 = new HorizontalGroup();
            HorizontalGroup playRow = new HorizontalGroup();

            table.align(Align.bottom);
            table.padBottom(20f);

            // Logo
        //    table.row();
          //  Image logo = new Image (this.game.im.getQuestionOverImg());
        //    logo.addAction(Actions.fadeIn(4f));
         //   table.addActor(logo);

            // Buttons
            table.row().align(Align.left);
            ButtonGroup row1_buttonGroup = new ButtonGroup();
            row1.addActor(addButton("Play", row1_buttonGroup));
            row1.addActor(addButton("Settings",row1_buttonGroup));
            row1.addActor(addButton("About", row1_buttonGroup));

            row1_buttonGroup.setMaxCheckCount(1);
            row1_buttonGroup.uncheckAll();
            row1.space(getButtonSpacing());

            table.add(row1);

        stage.addActor(table);
    }

    public float getButtonSpacing() {
        // TODO: Eventually have this be dynamic
        return 30f;
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
        boolean oldStyle = false;
        TextButton button;

        if (oldStyle) {
            button = new TextButton(name,skin);
        }
        else {
            button = new TextButton(name,this.game.im.getUIButtonStyle());
        }

        button.addListener(new UIClickListener(name.toLowerCase()));

        return button;
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

            if (this.button.equals("play")) {
                gsm.push(new MainMenuState2(gameID, gameStateManagerID));

            }
            else if (this.button.equals("settings")) {
                gsm.push(new QuestionBuilderState(gameID, gameStateManagerID));
            }
            else if (this.button.equals("about")) {
                Utils.log("about");
            }
        }
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        stage.act();
    }

    @Override
    public void render(SpriteBatch sb) {
        stage.draw();

    }

    @Override
    public void notifyResponse(Player player, String response) {

    }
}
