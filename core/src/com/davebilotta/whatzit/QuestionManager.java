// This is basically the level 
//
package com.davebilotta.whatzit;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class QuestionManager {

	private WhatzIt game;
	private ArrayList<Question> questions; // all of the questions - these get built up front
	private boolean hasQuestions;
	private int questionNum = 0;
	
	public QuestionManager(WhatzIt game) { 
		// TODO: Figure out tile size - base this on SPEED variable
		this.game = game;
	}
	
	public boolean hasQuestions() { 
		return this.questionNum < (this.questions.size() -1 );
	}
	
	public void loadQuestions() {
		/* This builds all of the questions */
		this.questionNum = -1;
		this.questions = new ArrayList<Question>();
		int id = 0;
	
		 try {
			FileHandle file = Gdx.files.internal("questions.txt");
	    	BufferedReader reader = file.reader(1024);
	    		
	        String line;
	 
	            while ((line = reader.readLine()) != null) {
	            	line.trim();
	            	
	            	if(line.length() > 0) {
	                if (!line.substring(0,2).equals("//")) {
	                	
	                	String[] s = line.split("\\|");
	                	String name = s[0].trim();
	                	String image = s[1].trim();
	                	
	                	String[] answers = null;
	                	String[] closeAnswers = null;
	                	//
	                	// Determine the answers
	                	answers = s[2].trim().split("\\,");

	                	//
	                	// Determine the "close answers"	                	
	                	if (s.length > 3) {
	                		String close = s[3].trim();
	                		// Determine "close answers"
	                		if (close.equals("")) {
	                			closeAnswers = null;
	                		}
	                		else { 
	                			closeAnswers = close.split("\\,");
	                		}
	                	}
	                	// Need to put s[2] and s[3] into a list of strings
	                	questions.add(new Question(id,name,image,answers,closeAnswers));
	                	
	                	id++;
	                }
	            }
	                
	            }
	            reader.close();
	 
	        } catch (IOException e) {
	         // TODO: If we can't load at start, need to bail
	        	e.printStackTrace();
	        }	
	}
	
	private Question getQuestionById(int id) {
		/* Gets a question with a given id: Note - probably *could* just rely on the id and its position in the 
		 * overall question list, but we want to safe-guard against gaps (e.g. if for some reason we can't find
		 * the image file, don't load it into overall list of questions. Iterating through will prevent against this
		 */
		Question quest = null;
		Iterator<Question> iter = this.questions.iterator();
		boolean done = false;
		while (iter.hasNext() && !done) {
			Question q = iter.next();
			
			if (q.getId() == id) {
				quest = q;
				done = true;
			}
		}
		return quest;
	}
	
	public ArrayList<Question> buildLevel(int size) {
		Utils.log("Building level (" + size + " questions) ... ");
		Utils.startTimer();
		ArrayList<Question> levelQuestions = new ArrayList<Question>();
		int[] idArray = new int[size];
		
		//this.questionNum = -1;
		
		for (int i = 0; i< idArray.length; i++) {
			idArray[i] = -1;
		}
		Question question;
		Random rand = new Random();
				
		for (int i=0; i<size; i++) {
			boolean done = false;
			while (!done) {
				int id = rand.nextInt(questions.size());
				if (!Utils.inList(id, idArray)) { 
					// 	TODO: May be gaps, can't solely rely on questions.size (need to check if question id returned is null)
					question = getQuestionById(id);
					levelQuestions.add(question);
					idArray[i] = id;
					done = true;
				}
				else {
					Utils.log("Found duplicate! Items in list are " + Utils.pretty(idArray,","));
				}
			} // end while
		} // end for
		
		//this.hasQuestions = true;
		
		Utils.stopTimer();
		Utils.log("Level build complete!");
		return levelQuestions;
	}
	
	public void nextQuestion() {
		if (this.hasQuestions()) {
			this.questionNum++;
		}
	}
	
	public int currentQuestion() { 
		return this.questionNum;
	}
	
	public Texture getQuestionImage() { 
		return questions.get(questionNum).getImage();
	}	
	
	public boolean checkGuess(String userGuess) { 
		boolean correct = false;
		String guess = userGuess.trim().toLowerCase();
		String[] answers, closeAnswers;
		
		Utils.log("Checking user guess of " + userGuess);
		//Utils.log("Answers are " + questions.get(questionNum).getAnswers().toString());
		//Utils.log("Close answers are " + questions.get(questionNum).getCloseAnswers().toString());
		
		answers = questions.get(questionNum).getAnswers();
		for (int i = 0; i < answers.length; i++) {
			if (guess.equals(answers[i].trim().toLowerCase())) { 
				correct = true;
			}
		}

		if (!correct && questions.get(questionNum).getCloseAnswers() != null) {
			closeAnswers = questions.get(questionNum).getCloseAnswers();
			for (int i = 0; i < closeAnswers.length; i++) {
				if (guess.equals(closeAnswers[i].trim().toLowerCase())) { 
					correct = true;
				}
			}

		}
		
		if (correct) { 
			Utils.log("User guess of " + userGuess + " MATCHED answers of " + Utils.pretty(questions.get(questionNum).getAnswers(),",") + " or " + Utils.pretty(questions.get(questionNum).getCloseAnswers(),","));
		}
		else { 
			Utils.log("User guess of " + userGuess + " DID NOT MATCH answers of " + Utils.pretty(questions.get(questionNum).getAnswers(),",") + " or " + Utils.pretty(questions.get(questionNum).getCloseAnswers(),","));			
		}
		return correct;
	}
}
