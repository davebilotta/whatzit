package com.davebilotta.whatzit;

import com.badlogic.gdx.graphics.Texture;

public class Question {

	private int id;
	private String name; // This is the display name
	private String imagePath; // Path to image
	private Texture image; // texture used to display image in level
	
	private String[] answers; // These are the acceptable answers 
	private String[] closeAnswers; // These answers are close - will prompt user to be more specific
	
	public Question(int id, String name, String imagePath, String[] answer, String[] closeAnswers) {
		this.setId(id);
		this.setName(name);
		this.setImagePath("images/" + imagePath);
		this.setAnswers(answer);
		this.setCloseAnswers(closeAnswers);
		
	/*	Utils.log("Creating new question with id " + id + " and name " + name);
		Utils.log("Image path is " + image);
		Utils.log("Answers are " + Utils.pretty(answers));
		Utils.log("Close answers are " + Utils.pretty(closeAnswers)); */
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImagePath(String image) {
		this.imagePath = image;
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public Texture getImage() {
		return image;
	}
	
	public void setImage(Texture image) {
		this.image = image;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	public String[] getCloseAnswers() {
		return closeAnswers;
	}

	public void setCloseAnswers(String[] closeAnswers) {
		this.closeAnswers = closeAnswers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
