package com.davebilotta.whatzit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.davebilotta.whatzit.states.GameStateManager;
import com.davebilotta.whatzit.states.MainMenuState;

public class WhatzIt extends ApplicationAdapter {
	private SpriteBatch batch;
	private GameStateManager gsm;
	public QuestionManager qm;
	public ImageManager im;
	public TileManager tm;
	
	public int WIDTH = 1024;
    public int HEIGHT = 768;
    private float ASPECT_RATIO;
   
    public static final String TITLE = "WhatzIt?";
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		
		this.qm = new QuestionManager(this);
		//this.qm.loadQuestions();
		this.im = new ImageManager(this);
		this.tm = new TileManager(this);
		
		this.WIDTH = 1024;
		this.HEIGHT = 768;
		this.ASPECT_RATIO = (float)WIDTH/(float)HEIGHT;
		
		Gdx.gl.glClearColor(0, 0, 0, 0);	
		
		gsm.push(new MainMenuState(this,gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.im.dispose();
	}
	
	
}
