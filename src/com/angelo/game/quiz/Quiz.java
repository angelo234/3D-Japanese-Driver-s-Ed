package com.angelo.game.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.angelo.game.Main;
import com.angelo.game.guis.GUITexture;
import com.angelo.game.guis.buttons.GUIButton;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.utils.GlobalVariables;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.fileloaders.QuizLoader;

public class Quiz {
	
	private static final int IMAGEID = 80;
	private static final int ANSWER1 = 81;
	private static final int ANSWER2 = 82;
	private static final int ANSWER3 = 83;
	private static final int ANSWER4 = 84;
	
	private List<QA> qas;
	private List<QA> chosenQAS;
	
	private Random random;
		
	private int answersCorrect = 0;
	
	private int questionNum = 0;	
	private boolean isDone;
	private boolean quizStopped;
	
	private QA currentQuestion = null;
	private List<String> currentAnswers = null;
	private int currentCorrectAnswer = 0;
	
	private boolean flagRemoveQuestion = false;
	
	private int noImage;
	private int correct;
	private int incorrect;
	
	public Quiz(){
		qas = QuizLoader.getQAS();
		chosenQAS = new ArrayList<QA>();
		random = new Random();	

		//Quiz screen
		GUIText.createText(30, "Question", 1f, Main.fontArial, new Vector2f(0.5f, 0.15f), 1f, true);
		GUIText.setColor(30, 0, 0, 0);
		GUIText.createText(31, "Question itself", 1, Main.fontArial, new Vector2f(0.5f, 0.2f), 1f, true);
		GUIText.setColor(31, 0, 0, 0);
		GUIText.createText(32, "Result", 1, Main.fontArial, new Vector2f(0.5f, 0.72f), 1f, true);
		
		//Result screen
		GUIText.createText(33, "PASS", 2f, Main.fontArial, new Vector2f(0.5f, 0.4f), 1f, true);
		GUIText.createText(34, "FAIL", 2f, Main.fontArial, new Vector2f(0.5f, 0.4f), 1f, true);
		GUIText.createText(35, "Score", 1f, Main.fontArial, new Vector2f(0.5f, 0.5f), 1f, true);
		GUIButton.createButton(25, new Vector2f(0.45f,0.65f), new Vector2f(0.55f,0.7f), "Exit", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		
		//Buttons
		GUIButton.createButton(20, new Vector2f(0.3f,0.3f), new Vector2f(0.7f,0.35f), "1", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		GUIButton.createButton(21, new Vector2f(0.3f,0.4f), new Vector2f(0.7f,0.45f), "2", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		GUIButton.createButton(22, new Vector2f(0.3f,0.5f), new Vector2f(0.7f,0.55f), "3", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		GUIButton.createButton(23, new Vector2f(0.3f,0.6f), new Vector2f(0.7f,0.65f), "4", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		GUIButton.createButton(24, new Vector2f(0.45f,0.77f), new Vector2f(0.55f,0.82f), "Next", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		noImage = Loader.loadTexture(QuizLoader.QUIZ_FOLDER+"noImage.png");
		
		GUITexture image = new GUITexture(noImage, new Vector2f(0.75f,0.05f), new Vector2f(0.95f,0.35f));
		
		image.setVisible(false);
		GlobalVariables.guis.set(IMAGEID,image);
		
		correct = Loader.loadTexture(QuizLoader.QUIZ_FOLDER+"correct.png");
		incorrect = Loader.loadTexture(QuizLoader.QUIZ_FOLDER+"incorrect.png");
		
		GUITexture image2 = new GUITexture(noImage, new Vector2f(0.245f,0.3f), new Vector2f(0.28f,0.35f));	
		image2.setVisible(false);
		
		GlobalVariables.guis.set(ANSWER1, image2);
		
		GUITexture image3 = new GUITexture(noImage, new Vector2f(0.245f,0.4f), new Vector2f(0.28f,0.45f));	
		image2.setVisible(false);
		
		GlobalVariables.guis.set(ANSWER2, image3);
		
		GUITexture image4 = new GUITexture(noImage, new Vector2f(0.245f,0.5f), new Vector2f(0.28f,0.55f));	
		image2.setVisible(false);
		
		GlobalVariables.guis.set(ANSWER3, image4);
		
		GUITexture image5 = new GUITexture(noImage, new Vector2f(0.245f,0.6f), new Vector2f(0.28f,0.65f));	
		image2.setVisible(false);
		
		GlobalVariables.guis.set(ANSWER4, image5);
		
	}
	
	public void submitAnswer(int answer){
		
		GUIButton.setClickable(20, false);
		GUIButton.setClickable(21, false);
		GUIButton.setClickable(22, false);
		GUIButton.setClickable(23, false);
		
		GUITexture resultImage = GlobalVariables.guis.get(ANSWER1 + currentCorrectAnswer);
		resultImage.setTexture(correct);
		
		resultImage.setVisible(true);
		
		if(answer == currentCorrectAnswer){
			GUIText.setText(32, "Correct!");			
			
			answersCorrect++;
		}
		else{
			GUIText.setText(32, "Incorrect!");
			
			GUITexture incorrectImage = GlobalVariables.guis.get(ANSWER1 + answer);
			
			incorrectImage.setTexture(incorrect);
			
			incorrectImage.setVisible(true);
		}
			
		GUIButton.setVisible(24, true);
		GUIText.setVisible(32, true);	
	}
	
	public void update(){
		QuizInputPolling.pollInput(this);	
		
		if(!quizStopped){
			if(flagRemoveQuestion){
				GlobalVariables.guis.get(ANSWER1).setVisible(false);
				GlobalVariables.guis.get(ANSWER2).setVisible(false);
				GlobalVariables.guis.get(ANSWER3).setVisible(false);
				GlobalVariables.guis.get(ANSWER4).setVisible(false);
				
				currentQuestion = null;
				currentAnswers = null;
				
				flagRemoveQuestion = false;
				
				GUIButton.setVisible(24, false);
				GUIText.setVisible(32, false);
				
				GUIButton.setClickable(20, true);
				GUIButton.setClickable(21, true);
				GUIButton.setClickable(22, true);
				GUIButton.setClickable(23, true);
			}
			
			//Test is done (10 questions answered) brings up test results
			if(questionNum > 20){
				currentQuestion = null;
				currentAnswers = null;
				
				GUIText.setVisible(30, false);
				GUIText.setVisible(31, false);
				GUIButton.setVisible(20, false);
				GUIButton.setVisible(21, false);
				GUIButton.setVisible(22, false);
				GUIButton.setVisible(23, false);
				
				GlobalVariables.guis.get(IMAGEID).setTexture(noImage);
				
				if(answersCorrect > 14){
					GUIText.setVisible(33, true);
				}
				else{
					GUIText.setVisible(34, true);
				}
				
				GUIText.setText(35, "You got "+answersCorrect+" out of 20 correct.");
				GUIText.setVisible(35, true);
				
				GUIButton.setVisible(25, true);
				//isDone = true;
			}
			else{					
				//If current question is null, randomly pick a new question that has never been chosen
				if(currentQuestion == null){
					questionNum++;
					List<QA> chosenQASTemp = new ArrayList<QA>();
					
					while(true){	
						QA requestedQuestion = qas.get(random.nextInt(qas.size()));
						
						//Look through all chosen questions and see if randomly chose question has already been picked			
						if(chosenQAS.size() == 0){
							currentQuestion = requestedQuestion;
							chosenQASTemp.add(currentQuestion);						
							break;
						}
						else{
							boolean alreadyChosen = false;
							
							for(QA chosenqa : chosenQAS){
								if(chosenqa.equals(requestedQuestion)){
									alreadyChosen = true;
								}
							}
							
							if(alreadyChosen){
								continue;
							}
							else{
								currentQuestion = requestedQuestion;
								chosenQASTemp.add(currentQuestion);
								break;
							}		
						}
												
					}

					chosenQAS.addAll(chosenQASTemp);				
					
					//Randomize current answer orders
					currentAnswers = new ArrayList<String>();
					//System.out.println(currentQuestion.getAnswers());
					
					currentAnswers.addAll(currentQuestion.getAnswers());
					String correctAnswer = currentAnswers.get(currentQuestion.getCorrectAnswer());
					
					Collections.shuffle(currentAnswers);
					
					for(int i = 0; i < currentAnswers.size(); i++){
						if(currentAnswers.get(i) == correctAnswer){
							currentCorrectAnswer = i;
						}
					}
					GUITexture image = GlobalVariables.guis.get(IMAGEID);
					
					if(currentQuestion.getImage() == -1){
						image.setTexture(noImage);	
					}
					else{
						image.setTexture(currentQuestion.getImage());
					}			
					image.setVisible(true);
								
				}			
			}				
		}
	}
	
	public void render(){
		if(!quizStopped){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glClearColor(0.45f, 0.6f, 0.9f, 1);
			if(currentQuestion != null && currentAnswers != null){
				GUIText.setVisible(30, true);
				GUIText.setVisible(31, true);
				GUIButton.setVisible(20, true);
				GUIButton.setVisible(21, true);
				GUIButton.setVisible(22, true);
				GUIButton.setVisible(23, true);
				
				GUIText.setText(30, "Question "+questionNum+":");	
				GUIText.setText(31, currentQuestion.getQuestion());	
				GUIButton.setText(20, currentAnswers.get(0));
				GUIButton.setText(21, currentAnswers.get(1));
				GUIButton.setText(22, currentAnswers.get(2));
				GUIButton.setText(23, currentAnswers.get(3));
			}			
		}	
	}
	
	public void stopQuiz(){
		GUIText.setVisible(30, false);
		GUIText.setVisible(31, false);
		GUIText.setVisible(32, false);
		GUIText.setVisible(33, false);
		GUIText.setVisible(34, false);
		GUIText.setVisible(35, false);
		
		GUIButton.setVisible(20, false);
		GUIButton.setVisible(21, false);
		GUIButton.setVisible(22, false);
		GUIButton.setVisible(23, false);
		GUIButton.setVisible(24, false);
		GUIButton.setVisible(25, false);
		this.quizStopped = true;
		this.isDone = true;
	}
	
	public void flagRemoveQuestion() {
		this.flagRemoveQuestion = true;
	}

	public boolean isQuizDone(){
		return isDone;
	}
	
	
}
