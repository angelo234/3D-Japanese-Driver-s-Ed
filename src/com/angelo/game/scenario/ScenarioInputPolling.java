package com.angelo.game.scenario;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.angelo.game.GameState;
import com.angelo.game.Main;
import com.angelo.game.guis.buttons.GUIButton;

public class ScenarioInputPolling {

	public static void pollInput(Scenario scenario) {
		// Keyboard Inputs
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()){
				keyPressed(scenario);
			}
		}
		keyHeld(scenario);

		// Mouse Inputs
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mousePressed(scenario);
			}else{
				mouseReleased(scenario);
			}
		}
		buttonHeld(scenario);

	}

	private static void keyPressed(Scenario scenario) {
		int key = Keyboard.getEventKey();
		
	}

	private static void keyHeld(Scenario scenario) {
		int key = Keyboard.getEventKey();
		
	}

	private static void mousePressed(Scenario scenario) {
		int button = Mouse.getEventButton();
		
	}

	private static void mouseReleased(Scenario scenario) {
		int button = Mouse.getEventButton();
		
		//Left click
		if(button == 0){
			
			
			switch(GUIButton.getGUIButtonClicked()){
			case -1: break;
			
			case 70:{
				Main.setState(GameState.MAINMENU); 
				System.out.println(true);
				break;
			}
			}
			
			
		}
	}
	
	private static void buttonHeld(Scenario scenario) {
		int button = Mouse.getEventButton();
	}

}
