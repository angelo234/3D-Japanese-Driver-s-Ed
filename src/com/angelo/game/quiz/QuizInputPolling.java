package com.angelo.game.quiz;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.angelo.game.guis.buttons.GUIButton;

public class QuizInputPolling {

	public static void pollInput(Quiz quiz) {
		// Keyboard Inputs
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()){
				keyPressed(quiz);
			}
		}
		keyHeld(quiz);

		// Mouse Inputs
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mousePressed(quiz);
			}else{
				mouseReleased(quiz);
			}
		}
		buttonHeld(quiz);

	}

	private static void keyPressed(Quiz quiz) {
		int key = Keyboard.getEventKey();
		
	}

	private static void keyHeld(Quiz quiz) {
		int key = Keyboard.getEventKey();
		
	}

	private static void mousePressed(Quiz quiz) {
		int button = Mouse.getEventButton();
		
	}

	private static void mouseReleased(Quiz quiz) {
		int button = Mouse.getEventButton();
		
		//Left click
		if(button == 0){
			switch(GUIButton.getGUIButtonClicked()){
			case -1: break;

			case 20: quiz.submitAnswer(0); break;
			case 21: quiz.submitAnswer(1); break;
			case 22: quiz.submitAnswer(2); break;
			case 23: quiz.submitAnswer(3); break;
			case 24: quiz.flagRemoveQuestion(); break;
			case 25: quiz.stopQuiz(); break;
			}
		}
	}
	
	private static void buttonHeld(Quiz quiz) {
		int button = Mouse.getEventButton();
	}

}
