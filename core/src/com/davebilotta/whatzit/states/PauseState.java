package com.davebilotta.whatzit.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.davebilotta.whatzit.Utils;
import com.davebilotta.whatzit.WhatzIt;

public class PauseState extends State {

        private PlayState previousState;
        private WhatzIt game;

        private int score;
        private float rowSpacing = 30f;
        private Skin skin;
        private Stage stage;

        Label.LabelStyle uiFontStyle;
        Label.LabelStyle messageStyle;

        public class myActor extends Actor {
            public myActor() {

            }
        }

        public PauseState(WhatzIt game, GameStateManager gsm, PlayState st) {
            super(game, gsm);
            this.game = game;
            this.previousState = st;
            this.score = score;

            this.stage = new Stage(new ScreenViewport());
            this.stage.clear();

            Gdx.input.setInputProcessor(stage);

            // TODO - use generic ImageManager skin
            this.skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas("uiskin.atlas"));

            uiFontStyle = new Label.LabelStyle();
            uiFontStyle.font = this.game.im.uiFont;
            uiFontStyle.fontColor = Color.BLUE;

            messageStyle = new Label.LabelStyle();
            messageStyle.font = this.game.im.messageFont;
            messageStyle.fontColor = Color.NAVY;

            setStage();
        }

        @Override
        public void update(float dt) {
            handleInput();
        }

        public void setStage() {
            //
            Image bkg = new Image(this.game.im.getBkg());
            bkg.setSize(this.game.WIDTH, this.game.HEIGHT);
            stage.addActor(bkg);

            Table table = new Table(skin);
            table.setFillParent(true);

            table.row().align(Align.center).expandY();
            VerticalGroup textGroup = new VerticalGroup();
            textGroup.space(20f);
            textGroup.addActor(addLabel("Game paused"));
            textGroup.addActor(addLabel("Click anywhere to continue"));

            table.add(textGroup);
            stage.addActor(table);

        }


        public Label addLabel(String text) {
            return new Label(text,messageStyle);
        }

        public Label addLabel(String text, Label.LabelStyle style) {
            return new Label(text,style);
        }

        @Override
        public void render(SpriteBatch sb) {
            stage.draw();
        }

        public void returnToPriorState() {
            stage.dispose();
            this.gsm.set(previousState);
            previousState.waiting = false;
        }

        @Override
        public void handleInput() {
            stage.act();

            if (Gdx.input.justTouched()) {
                returnToPriorState();
                }

        }


        @Override
        public void notifyResponse(Player player, String response) {

        }
    }
