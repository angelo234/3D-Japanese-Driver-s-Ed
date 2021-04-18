package com.angelo.game.quiz;

import java.util.List;

public class QA {

	private String question;
	private List<String> answers;
	private int correctAnswer;
	private int image;
	
	public QA(String question, List<String> answers, int correctAnswer, int image){
		this.question = question;
		this.answers = answers;
		this.image = image;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public List<String> getAnswers() {
		return answers;
	}
		
	public int getCorrectAnswer(){
		return correctAnswer;
	}

	public int getImage() {
		return image;
	}
	
}
