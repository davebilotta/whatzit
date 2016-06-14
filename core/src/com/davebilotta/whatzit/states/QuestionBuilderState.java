package com.davebilotta.whatzit.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.davebilotta.whatzit.WhatzIt;

/**
 * Created by Dave on 6/12/2016.
 */
public class QuestionBuilderState extends State {

    protected QuestionBuilderState(WhatzIt game, GameStateManager gsm) {
        super(game, gsm);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        renderImage(sb);
        renderQuestionItems(sb);
    }

    public void renderImage(SpriteBatch sb) {

    }

    public void renderQuestionItems(SpriteBatch sb) {

    }

    @Override
    public void notifyResponse(Player player, String response) {

    }
}
