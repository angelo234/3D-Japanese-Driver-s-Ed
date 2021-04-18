package com.angelo.game.utils.fileloaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.angelo.game.quiz.QA;
import com.angelo.game.utils.Loader;

public class QuizLoader {

	public static final String QUIZ_FOLDER = "quiz/";
	public static final String QUIZ_FILE = QUIZ_FOLDER+"Quiz.txt";
	
	public static List<QA> getQAS(){
		List<QA> qas = new ArrayList<QA>();
		
		boolean question = false;
		
		List<String> file = FileLoader.loadFile(QUIZ_FILE, "[Quiz File]");
		
		String strQuestion = null;
		List<String> answers = null;
		int correctAnswer = 0;
		int image = -1;
		
		for(String line : file){	
			//End of question marking
			if(line.startsWith("end")){
				qas.add(new QA(strQuestion, answers, correctAnswer, image));
				
				//Reset values
				strQuestion = null;
				answers = null;
				correctAnswer = 0;
				image = -1;
				
				question = false;
			}
			
			if(question){
				//Correct Answer marking
				if(line.startsWith("ac")){
					String parsedLine = line.split(":")[1];
					
					correctAnswer = answers.size();
					answers.add(parsedLine);
				}
				//Not correct answer marking
				else if(line.startsWith("a")){
					String parsedLine = line.split(":")[1];
					answers.add(parsedLine);
				}
				else if(line.startsWith("i")){
					String parsedLine = line.split(":")[1];
					image = Loader.loadTexture(QUIZ_FOLDER+parsedLine);
				}
			}
			
			//Question marking
			if(line.startsWith("q")){
				String parsedLine = line.split(":")[1];
				strQuestion = parsedLine;
				answers = new ArrayList<String>();
				question = true;
			}
		}

		return qas;
	}
	
}
