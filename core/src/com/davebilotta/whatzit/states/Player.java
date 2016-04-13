package com.davebilotta.whatzit.states;

import com.davebilotta.whatzit.Utils;

public class Player {

	private int id;
	private String name;
	private int score;
	private int correct, incorrect;
	
	private int hints;
	
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
}
