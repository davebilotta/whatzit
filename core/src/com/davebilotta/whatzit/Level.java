package com.davebilotta.whatzit;

import java.util.ArrayList;

public class Level {
	
	private ArrayList<Question> questions;
	private enum DIFFICULTY {
		EASY,MEDIUM,HARD,INSANE
	}
	private DIFFICULTY difficulty;
	private float delay;
	
	public Level() {
		this.questions = new ArrayList<Question>();
		
		this.difficulty = DIFFICULTY.MEDIUM;
		
		this.delay = 5f;
		
		Utils.log("Level created");
	}

}
