package com.davebilotta.whatzit.states;

import com.badlogic.gdx.math.Rectangle;
import com.davebilotta.whatzit.Utils;

public class Player {

	private int id;
	private String name;
	private int score;
	private int correct, incorrect;
	
	private int hints;
    private float x;
    private float y;
    private float width;
    private float height;
    private Rectangle rect;

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
		this.score = 0;
		this.correct = 0;
		this.incorrect = 0;
		
		this.hints = 1; // every player starts with 1 hint
	}
	
	public String getName() { 
		return this.name;
	}
	
	public int getScore() { 
		return this.score;
	}
	
	public void correct(int score) { 
		this.score += score;
		this.correct++;
	}
	
	public void incorrect(int score) { 
		this.score -= score;
		this.incorrect++;
	}
	
	public void earnHint() { 
		this.hints++;
	}
	
	public boolean hasHints() { 
		return (this.hints > 0);
	}

    public void setPos(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;

        this.rect = new Rectangle(x,y,width,height);
    }
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public Rectangle getRect() {
        return this.rect;
    }
}
