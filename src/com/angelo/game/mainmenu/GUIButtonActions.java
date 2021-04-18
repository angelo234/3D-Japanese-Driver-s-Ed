package com.angelo.game.mainmenu;

import com.angelo.game.GameState;
import com.angelo.game.Main;

public final class GUIButtonActions {

	//Main Menu Buttons
	
	public static void trainingCoursesButton(MainMenu mainMenu){
		mainMenu.setScreen(MainMenuScreen.DRIVERSTRAININGCOURSES);
	}
	
	public static void examButton(MainMenu mainMenu){
		mainMenu.setScreen(MainMenuScreen.QUIZ);
	}
	
	public static void optionButton(MainMenu mainMenu){
		mainMenu.setScreen(MainMenuScreen.OPTIONS);
	}
	
	public static void quitButton(MainMenu mainMenu){
		Main.isRunning = false;
	}
	
	//Driver's Training Courses Buttons
	
	//The Basics
	public static void scenario1(MainMenu mainMenu){	
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(1);		
	}
	
	//Maneuvering Obstacles
	public static void scenario2(MainMenu mainMenu){
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(2);
	}
	
	//Parking
	public static void scenario3(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(3);
	}

	//Parallel Parking
	public static void scenario4(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(4);
	}

	//Basic Traffic Laws
	public static void scenario5(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(5);
	}

	//WIP
	public static void scenario6(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(6);
	}
	
	//Road Driving
	public static void scenario7(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(7);
	}
	
	//Highway Driving
	public static void scenario8(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(8);
	}
	
	public static void scenario9(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(9);
	}
	
	public static void scenario10(MainMenu mainMenu) {
		Main.setState(GameState.RUNNING);
		mainMenu.stopRendering();
		mainMenu.getMainInstance().setupScenario(10);
	}
	
	public static void backButton(MainMenu mainMenu){
		mainMenu.setScreen(MainMenuScreen.MAINMENU);
	}
	
}
