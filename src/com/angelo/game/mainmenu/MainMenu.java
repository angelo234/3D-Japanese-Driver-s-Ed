package com.angelo.game.mainmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.angelo.game.GameState;
import com.angelo.game.Main;
import com.angelo.game.guis.GUITexture;
import com.angelo.game.guis.buttons.GUIButton;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.utils.GlobalVariables;
import com.angelo.game.utils.Loader;

public class MainMenu {

	private GUITexture gameLogo;
	private GUITexture menuBackground;
	private GUIText logoText;
	private List<GUIButton> mainMenuButtons;
	private List<GUIButton> trainingCoursesButtons;
	private List<GUIButton> optionsButtons;
	
	private boolean isClosed;
	
	private MainMenuScreen screen = MainMenuScreen.MAINMENU;
	
	private Main mainInstance;
	
	public MainMenu(Main mainInstance){
		mainMenuButtons = new ArrayList<GUIButton>();
		trainingCoursesButtons = new ArrayList<GUIButton>();
		optionsButtons = new ArrayList<GUIButton>();
		
		//GUITexture gameLogo = new GUITexture();
		
		menuBackground = new GUITexture(Loader.loadGameTexture("mainmenu"), new Vector2f(0f,0f), new Vector2f(1f,1f));
		menuBackground.setVisible(true);
		GlobalVariables.guis.add(menuBackground);
		//Logo Texture (WIP)
		logoText = GUIText.createText(10, "3D Japanese Driver's Ed", 1.5f, Main.fontArial, new Vector2f(0.5f, 0.1f), 1, true);
		
		//Main Menu Buttons
		mainMenuButtons.add(GUIButton.createButton(0, new Vector2f(0.41f,0.3f), new Vector2f(0.59f,0.35f), "Driving Training", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		mainMenuButtons.add(GUIButton.createButton(1, new Vector2f(0.41f,0.4f), new Vector2f(0.59f,0.45f), "Traffic Law Quiz", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		mainMenuButtons.add(GUIButton.createButton(2, new Vector2f(0.41f,0.5f), new Vector2f(0.59f,0.55f), "Options", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		mainMenuButtons.add(GUIButton.createButton(3, new Vector2f(0.41f,0.6f), new Vector2f(0.59f,0.65f), "Quit", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		
		//Driving Training Courses Buttons	
		//First Column
		trainingCoursesButtons.add(GUIButton.createButton(4, new Vector2f(0.24f,0.3f), new Vector2f(0.4f,0.35f), "The Basics", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		trainingCoursesButtons.add(GUIButton.createButton(5, new Vector2f(0.24f,0.4f), new Vector2f(0.4f,0.45f), "Maneuvering Obstacles", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		trainingCoursesButtons.add(GUIButton.createButton(6, new Vector2f(0.24f,0.5f), new Vector2f(0.4f,0.55f), "Parking", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		trainingCoursesButtons.add(GUIButton.createButton(7, new Vector2f(0.24f,0.6f), new Vector2f(0.4f,0.65f), "Parallel Parking", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		trainingCoursesButtons.add(GUIButton.createButton(8, new Vector2f(0.24f,0.7f), new Vector2f(0.4f,0.75f), "Basic Traffic Laws", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		
		//Second Column
		trainingCoursesButtons.add(GUIButton.createButton(9, new Vector2f(0.60f,0.3f), new Vector2f(0.76f,0.35f), "WIP", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		
		//trainingCoursesButtons.add(GUIButton.createButton(6, new Vector2f(0.62f,0.3f), new Vector2f(0.78f,0.35f), "Manuvering Obstacles", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		
		trainingCoursesButtons.add(GUIButton.createButton(14, new Vector2f(0.44f,0.85f), new Vector2f(0.56f,0.9f), "Back", 1, Main.fontArial, Loader.loadGameTexture("Button")));
		//Exam Buttons
		
		
		//Option Buttons
		
		
		//mainMenuButtons.add(GUIButton.createButton(1, new Vector2f(0.4f,0.3f), new Vector2f(0.6f,0.35f), Loader.loadGameTexture("Button")));
		this.mainInstance = mainInstance;
	}
	
	public void update(){
		InputPolling.pollInput(this);
	}

	public void render(){	
		prepareRendering();	
		//Rendered throughout all screens
		
		//Render depending on screen
		switch(this.getScreen()){
		case MAINMENU:{		
			renderMainMenu();	
			break;
		}
		case DRIVERSTRAININGCOURSES:{
			renderDriversTrainingCoursesMenu();
			break;
		}
		case QUIZ:{
			Main.setState(GameState.QUIZ);
			break;
		}	
		case OPTIONS:{
			renderOptions();
			break;
		}
		default:
			break;
		}
	}
	
	public void resetMainMenu(){
		menuBackground.setVisible(true);
		this.setClosed(false);
	}
	
	public void stopRendering(){
		this.setScreen(MainMenuScreen.BLANK);
		
		logoText.setVisible(false);
		menuBackground.setVisible(false);
		
		for (GUIButton button : this.getMainMenuButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}

		for (GUIButton button : this.getTrainingCoursesButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}

		for (GUIButton button : this.getOptionsButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}	
		
		this.setClosed(true);
	}
	
	private void renderMainMenu(){
		logoText.setVisible(true);
		menuBackground.setVisible(true);
		for(GUIButton button : this.getMainMenuButtons()){
			if(button != null){
				button.setVisible(true);
			}		
		}
	}
	
	private void renderDriversTrainingCoursesMenu(){
		for(GUIButton button : this.getTrainingCoursesButtons()){
			if(button != null){
				button.setVisible(true);
			}		
		}
	}
	
	private void renderOptions(){
		for(GUIButton button : this.getOptionsButtons()){
			if(button != null){
				button.setVisible(true);
			}		
		}
	}
	
	private void prepareRendering(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1);

		logoText.setVisible(false);
		
		for (GUIButton button : this.getMainMenuButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}

		for (GUIButton button : this.getTrainingCoursesButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}


		for (GUIButton button : this.getOptionsButtons()) {
			if (button != null) {
				button.setVisible(false);
			}
		}
	}
	
	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public MainMenuScreen getScreen() {
		return screen;
	}

	public void setScreen(MainMenuScreen screen) {
		this.screen = screen;
	}

	public List<GUIButton> getMainMenuButtons() {
		return mainMenuButtons;
	}	
	
	public List<GUIButton> getTrainingCoursesButtons() {
		return trainingCoursesButtons;
	}

	public List<GUIButton> getOptionsButtons() {
		return optionsButtons;
	}
	
	public Main getMainInstance(){
		return mainInstance;
	}

}
