package com.angelo.game.mainmenu;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.angelo.game.guis.buttons.GUIButton;

public class InputPolling {

	public static void pollInput(MainMenu mainMenu) {
		// Keyboard Inputs
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()){
				keyPressed(mainMenu);
			}
		}
		keyHeld(mainMenu);

		// Mouse Inputs
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mousePressed(mainMenu);
			}else{
				mouseReleased(mainMenu);
			}
		}
		buttonHeld(mainMenu);

	}

	private static void keyPressed(MainMenu mainMenu) {
		int key = Keyboard.getEventKey();
		
	}

	private static void keyHeld(MainMenu mainMenu) {
		int key = Keyboard.getEventKey();
		
	}

	private static void mousePressed(MainMenu mainMenu) {
		int button = Mouse.getEventButton();
		
	}

	private static void mouseReleased(MainMenu mainMenu) {
		int button = Mouse.getEventButton();
		
		//Left click
		if(button == 0){
			switch(GUIButton.getGUIButtonClicked()){
			case -1: break;
			//Main Menu
			case 0: GUIButtonActions.trainingCoursesButton(mainMenu); break;
			case 1: GUIButtonActions.examButton(mainMenu); break;
			case 2: GUIButtonActions.optionButton(mainMenu); break;
			case 3: GUIButtonActions.quitButton(mainMenu); break;
			//Driving Training Menu
			case 4: GUIButtonActions.scenario1(mainMenu); break;
			case 5: GUIButtonActions.scenario2(mainMenu); break;
			case 6: GUIButtonActions.scenario3(mainMenu); break;
			case 7: GUIButtonActions.scenario4(mainMenu); break;
			case 8: GUIButtonActions.scenario5(mainMenu); break;
			case 9: GUIButtonActions.scenario6(mainMenu); break;
			case 10: GUIButtonActions.scenario7(mainMenu); break;
			case 11: GUIButtonActions.scenario8(mainMenu); break;
			case 12: GUIButtonActions.scenario9(mainMenu); break;
			case 13: GUIButtonActions.scenario10(mainMenu); break;
			case 14: GUIButtonActions.backButton(mainMenu); break;
			}
		}
	}
	
	private static void buttonHeld(MainMenu mainMenu) {
		int button = Mouse.getEventButton();
	}

}
