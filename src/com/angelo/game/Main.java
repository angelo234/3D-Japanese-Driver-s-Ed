package com.angelo.game;

import java.io.File;
import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.Timer;
import org.lwjgl.util.jinput.LWJGLEnvironmentPlugin;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.angelo.game.audio.AudioMaster;
import com.angelo.game.audio.Source;
import com.angelo.game.car.PlayerCar;
import com.angelo.game.entities.Camera;
import com.angelo.game.entities.CarMirrorCamera;
import com.angelo.game.entities.PlayerCamera;
import com.angelo.game.fontRendering.TextMaster;
import com.angelo.game.guis.GUIRenderer;
import com.angelo.game.guis.GUITexture;
import com.angelo.game.guis.buttons.ButtonMaster;
import com.angelo.game.guis.buttons.GUIButton;
import com.angelo.game.guis.text.FontType;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.line.LineDataLoader;
import com.angelo.game.mainmenu.MainMenu;
import com.angelo.game.mainmenu.MainMenuScreen;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.particles.ParticleSystem;
import com.angelo.game.particles.ParticleTexture;
import com.angelo.game.physicsEngine.PhysicsEngine;
import com.angelo.game.postProcessing.Fbo;
import com.angelo.game.postProcessing.PostProcessing;
import com.angelo.game.quiz.Quiz;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.renderEngine.MasterRenderer;
import com.angelo.game.terrains.Terrain;
import com.angelo.game.utils.GetEntitiesToRender;
import com.angelo.game.utils.GlobalVariables;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.Maths;
import com.angelo.game.utils.MousePicker;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

import joystick.JInputJoystick;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Main implements Runnable {
	
	public static int framesCountAvg = 0;
	
	public static boolean isRunning = false;
	public static boolean flagQuitCurrentScenario = false;
	
	private static int WIDTH, HEIGHT;

	private Thread mainThread;
	
	private long now;
	private int framesCount = 0;	
	private long framesTimer = 0;
	private long startTime;

	private static int timePast = 0;
	
	private TexturedModel lampModel;
	private TexturedModel barrelModel;
	private TexturedModel cherryModel;

	public PlayerCamera camera;
	public CarMirrorCamera carLeftMirrorCamera;
	public CarMirrorCamera carRightMirrorCamera;

	public GUIRenderer guiRenderer;
	
	public MasterRenderer renderer;
	
	public PhysicsEngine physicsEngine;

	private Fbo multisampleFbo;
	private Fbo carLeftMirrorFbo;
	private Fbo carRightMirrorFbo;
	
	private Fbo outputFbo;
	private Fbo outputFbo2;
	
	private MousePicker picker;

	private Vector3f firstPickedPoint;
	private Vector3f secondPickedPoint;
	
	private Source source;

	public static FontType fontArial;

	private ParticleSystem system;
	
	private ParticleTexture particleTexture;
	
	private static GameState gameState = GameState.MAINMENU;
	
	private static int menuScreen = 0;
	private static int textSelected = 0;
	
	private static final boolean isDeveloperMode = false;
	
	private boolean mainMenuInited = false;

	private static MainMenu mainMenu;

	private static Quiz currentQuiz;
	
	private static JInputJoystick joystick;
	
	private GUITexture leftMirrorTexture;
	private GUITexture rightMirrorTexture;
	
	int buffer = -1;
	
	public static GameState getState(){
	    return gameState;
	}

	public static void setState(GameState state){	
		if(state == GameState.MAINMENU){
			if(Main.gameState == GameState.RUNNING){
				if(Main.mainMenu != null){
		    		toMainMenu(MainMenuScreen.DRIVERSTRAININGCOURSES);
		    	}
			}
			else if(Main.gameState == GameState.QUIZ){
				if(Main.mainMenu != null){
		    		toMainMenu(MainMenuScreen.MAINMENU);
		    	}
			}
		}	
		
	    Main.gameState = state;
	}
	
	@Override
	public void run() {
		
		initGame();
		
		startTime = System.currentTimeMillis();

		while (!Display.isCloseRequested() && isRunning) {		
			
			now = System.currentTimeMillis();

			framesCount++;
			timePast = (int) (now - startTime);
			if (now - framesTimer > 1000) {
				framesTimer = now;
				framesCountAvg = framesCount;
				framesCount = 0;

			}
			
			Timer.tick();
			gameState();			
				
			DisplayManager.updateDisplay();
		}
		
		cleanUp();
	}
	
	private void initGame() {
		DisplayManager.createDisplay(WIDTH, HEIGHT, "3D Japanese Driver's Ed");
		
		initGameControllers();
		
		guiRenderer = new GUIRenderer();
		TextMaster.init();
		ButtonMaster.init(guiRenderer);
		fontArial = new FontType(Loader.loadFontTextureAtlas("Arial"), new File("res/Arial.fnt"));
		
		for(int i = 0 ; i < 99999 ; i++){
			GlobalVariables.guiTexts.add(null);
		}	
		for(int i = 0 ; i < 99999 ; i++){
			GlobalVariables.guiButtons.add(null);
		}
		for(int i = 0 ; i < 99999 ; i++){
			GlobalVariables.guis.add(null);
		}
			
		AudioMaster.init();
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
		source = new Source();
		
		isRunning = true;
	}
	
	private void initGameControllers(){
		
		String os = System.getProperty("os.name");
	
		if(os.startsWith("Windows")){
			joystick = new JInputJoystick(Controller.Type.WHEEL, Controller.Type.GAMEPAD);
		}
		else{
			joystick = new JInputJoystick(Controller.Type.STICK);
		}
		
		// Check if the controller was found.
		if(!joystick.isControllerConnected() ){
		   System.out.println("No controller found!");
		}
		
	}
	
	public void setupScenario(int scenario){
		setupGameMechanics();
		
		ScenarioLoader.loadScenario(scenario, camera);
		picker = new MousePicker(camera, renderer.getProjectionMatrix(), ScenarioLoader.currentScenario.terrains.get(0));
		
		
		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
	}
	
	private void setupGameMechanics(){
		//Initialize game engines
		camera = new PlayerCamera();
		carLeftMirrorCamera = new CarMirrorCamera();
		carRightMirrorCamera = new CarMirrorCamera();
		
		renderer = new MasterRenderer(camera);
		
		physicsEngine = new PhysicsEngine();
		
		multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		
		carLeftMirrorFbo = new Fbo(Display.getWidth() / 8, Display.getHeight() / 8,Fbo.DEPTH_TEXTURE);
		carRightMirrorFbo = new Fbo(Display.getWidth() / 8, Display.getHeight() / 8,Fbo.DEPTH_TEXTURE);
		
		leftMirrorTexture = new GUITexture(carLeftMirrorFbo.getColorTexture(), new Vector2f(0.17f,0.575f), new Vector2f(0.02f,0.425f));
		leftMirrorTexture.setVisible(true);
		GlobalVariables.guis.add(leftMirrorTexture);
		
		rightMirrorTexture = new GUITexture(carRightMirrorFbo.getColorTexture(), new Vector2f(0.98f,0.575f), new Vector2f(0.83f,0.425f));
		rightMirrorTexture.setVisible(true);
		GlobalVariables.guis.add(rightMirrorTexture);
		//outputFbo = new Fbo(Display.getWidth(),Display.getHeight(),Fbo.NONE);
		//outputFbo2 = new Fbo(Display.getWidth(),Display.getHeight(),Fbo.NONE);
		PostProcessing.init();
		
		
		for(int i = 0 ; i < 99999 ; i++){
			GlobalVariables.guiTexts.add(null);
		}
		
		
		GUIText.createText(9, "FPS: " + Main.getFPS(), 1, fontArial, new Vector2f(0, 0.015f), 1f, false);
		//GUIText.createText(10, "Player XYZ ("+player.getPosition().x+"m, "+player.getPosition().y+"m, "+player.getPosition().z+"m)", 1, fontArial, new Vector2f(0f,0.1f), 1f, false);
		//GUIText.createText(11, "Player Rotation XYZ ("+player.getRotation().getX()+", "+player.getRotation().getY()+", "+player.getRotation().getZ()+")", 1, fontArial, new Vector2f(0f,0.15f), 1f, false);
		GUIText.createText(12, "Camera XYZ ("+camera.getPosition().x+"m, "+camera.getPosition().y+"m, "+camera.getPosition().z+"m)", 1, fontArial, new Vector2f(0f,0.15f), 1f, false);
		GUIText.createText(13, "Camera Rotation XYZ ("+camera.getRotation().x+", "+camera.getRotation().y+", "+camera.getRotation().z+")", 1, fontArial, new Vector2f(0f,0.2f), 1f, false);
		GUIText.createText(14, "RAM Allocated: "+0+"GB", 1, fontArial, new Vector2f(0f, 0.25f), 1f, false);
		GUIText.createText(15, 0+" km/h", 1, Main.fontArial,new Vector2f(0.8f,0.95f),1f,false);
		GUIText.createText(16, "Gear: P", 1, Main.fontArial,new Vector2f(0.9f,0.95f),1f,false);
		GUIText.createText(17, "Picker", 1, Main.fontArial,new Vector2f(0f,0.3f),1f,false);
		GUIText.createText(18, "First point", 1, Main.fontArial,new Vector2f(0f,0.4f),1f,false);
		GUIText.createText(19, "Second point", 1, Main.fontArial,new Vector2f(0f,0.45f),1f,false);
		GUIText.createText(20, "Distance", 1, Main.fontArial,new Vector2f(0f,0.5f),1f,false);
		GUIText.createText(21, "Rotation", 1, Main.fontArial,new Vector2f(0f,0.55f),1f,false);
		GUIText.createText(22, "Engine RPM", 1, Main.fontArial,new Vector2f(0f,0.95f),1f,false);
		
		GUIText.setVisible(9, true);
		GUIText.setVisible(12, false);
		GUIText.setVisible(13, false);
		GUIText.setVisible(14, false);
		GUIText.setVisible(15, false);
		GUIText.setVisible(16, false);
		GUIText.setVisible(17, false);
		GUIText.setVisible(18, false);
		GUIText.setVisible(19, false);
		GUIText.setVisible(20, false);
		GUIText.setVisible(21, false);
		GUIText.setVisible(22, false);
	}
	
	private void gameState(){
		
		if(gameState != GameState.RUNNING){
			if(leftMirrorTexture != null && rightMirrorTexture != null){
				leftMirrorTexture.setVisible(false);
				rightMirrorTexture.setVisible(false);
			}
			
		}
		
		switch (gameState) {

		// Main Menu State
		case MAINMENU: {
			if (mainMenu == null) {
				mainMenu = new MainMenu(this);
			}

			if (mainMenu.isClosed()) {
				mainMenu.resetMainMenu();
			}
			mainMenu.update();
			mainMenu.render();
			guiRenderer.render(GlobalVariables.guis);

			break;
		}

		// Tutorial State
		case TUTORIAL: {

			break;
		}

		// In Game State
		case RUNNING: {
			gameRunning();

			break;
		}

		// Game Paused State
		case PAUSED: {
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && getState() == GameState.PAUSED) {
						Main.setState(GameState.RUNNING);

						Mouse.setGrabbed(true);
						Mouse.setCursorPosition(WIDTH / 2, HEIGHT / 2);
					}
				}

			}

			break;
		}
		// Quiz Mode
		case QUIZ: {
			mainMenu.stopRendering();
			
			if(currentQuiz == null){
				currentQuiz = new Quiz();
			}
			
			if(!currentQuiz.isQuizDone()){
				currentQuiz.update();
				currentQuiz.render();
				guiRenderer.render(GlobalVariables.guis);

			}
			else{
				currentQuiz.stopQuiz();
				currentQuiz = null;
				Main.setState(GameState.MAINMENU);
			}
						
		}

			break;
		default:
			break;

		}

		if (gameState != GameState.PAUSED) {
			guiRenderer.render(GlobalVariables.guis);
			GUIText.updateTexts();
			TextMaster.render();

			GUIButton.updateButtons();
			ButtonMaster.render();
		}
	}

	private void gameRunning(){	
		if(flagQuitCurrentScenario == true){
			ScenarioLoader.quitCurrentScenario();
			Main.setState(GameState.MAINMENU);
			flagQuitCurrentScenario = false;
		}
		if(ScenarioLoader.currentScenario != null){
			update();
			render();
		}
	}
	
	private static void toMainMenu(MainMenuScreen screen){
		Mouse.setGrabbed(false);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		mainMenu.setScreen(screen);
	}
	
	private void initGame(int route){
		//LuaScriptLoader luaLoader = new LuaScriptLoader();
		//luaLoader.loadScript();
		
		//ParticleMaster.init(Loader, renderer.getProjectionMatrix());
		//source = new Source();
		
		/*lampModel = new TexturedModel(OBJLoader.loadObjModel("lamp"), new ModelTexture(Loader.loadGameTexture("lamp")));
		barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel"), new ModelTexture(Loader.loadGameTexture("barrel")));
		
		lampModel.getTexture().setUseFakeLighting(true);
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		barrelModel.getTexture().setNormalMap(Loader.loadGameTexture("barrelNormal"));
		
		cherryModel = new TexturedModel(OBJLoader.loadObjModel("cherry"), new ModelTexture(Loader.loadGameTexture("cherry")));
		cherryModel.getTexture().setTransparent(true);
		cherryModel.getTexture().setShineDamper(10);
		cherryModel.getTexture().setReflectivity(0.5f);
		cherryModel.getTexture().setSpecularMap(Loader.loadGameTexture("cherryS"));
		*/
		//GlobalVariables.entities.add(new Entity(cherryModel,new Vector3f(10,0,40),new Vector3f(0,0,0),1));
		
		//lights.add(new Light(new Vector2f(400, -100), new Vector3f(1, 1, 1), new Vector3f(1f, 0.01f, 0.002f), terrain, lampModel, Loader));
		//lights.add(new Light(new Vector3f(50,0, 40), new Vector3f(1, 1, 1), new Vector3f(1f, 0.01f, 0.002f), lampModel, Loader));
		
		
		
//		//float[] sizes2 = {2.5f, 2.5f, 5f, 5f, 2.5f, 2.5f};
		//normalMapEntities.add(new Entity(barrelModel, new Vector3f(400, 20, -75), 0, 0, 0, sizes2,1f));

		//waterTile = new WaterTile(380, -4, 80);
		
		//GlobalVariables.waters.add(waterTile);

		//particleTexture = new ParticleTexture(Loader.loadGameTexture("particleAtlas"), 4, true);
		
		//system = new ParticleSystem(particleTexture, 200, 10, 0.5f, 2, 3.6f);
		
		//system.setDirection(new Vector3f(0,1,0), 0.1f);
		//system.setLifeError(0.1f);
		//system.setSpeedError(0.25f);
		//system.setScaleError(0.5f);
		//system.randomizeRotation();
		// picker = new MousePicker(camera, renderer.getProjectionMatrix(),
		// terrain);

		
		/*int buffer = AudioMaster.loadSound("com/angelo/game/audio/bounce.wav");
		
		source.setLooping(true);
		source.play(buffer);
		source.setVelocity(0,1,0);
		source.setPosition(8,0,1);*/

	}
	
	private void update() {
		pollInput();
	
		picker.update();
		
		ScenarioLoader.currentScenario.update();
		
		//System.out.println(ScenarioLoader.currentScenario.getCourseBounds().isPlayerCarOutOfBounds((PlayerCar) ScenarioLoader.currentScenario.cars.get(0)));
		
		//ParticleMaster.update(camera);

		physicsEngine.update();

		camera.move();
		
		updateCarMirrors();
		
		updateCarAudio();	
		
		for (Terrain terrain : ScenarioLoader.currentScenario.terrains) {
			//if (player.getPosition().x > terrain.getGridX() * Terrain.SIZE && player.getPosition().x < Terrain.SIZE * terrain.getGridX() + 800 && player.getPosition().z > terrain.getGridZ() * Terrain.SIZE && player.getPosition().z < Terrain.SIZE * terrain.getGridZ() + 800) {
			
		}	
	}
	
	private void updateCarMirrors(){
		PlayerCar pcar = ScenarioLoader.currentScenario.getPlayerCar();
		
		//Left Mirror
		float rotationOffset = (float) Math.toRadians(pcar.getRotation().getY() - 180);
		
		Vector3f offset = new Vector3f((float) (0.9f * Math.cos(rotationOffset)), 0.2f, (float) (0.9f*Math.sin(rotationOffset)));
		
		Vector3f leftMirrorPos = Vector3f.add(pcar.getPosition(), offset, null);

		carLeftMirrorCamera.setPosition(leftMirrorPos);
		carLeftMirrorCamera.setRotation(0, pcar.getRotation().getY() - 180, 0);	
		
		//Right Mirror
		float x2 = (float) (pcar.getPosition().x+(0.9f*Math.cos(Math.toRadians(-pcar.getRotation().getY()))));	
		float y2 = (float) (pcar.getPosition().y+0.2f);				
		float z2 = (float) (pcar.getPosition().z+(0.9f*Math.sin(Math.toRadians(pcar.getRotation().getY()))));	
		
		carRightMirrorCamera.setPosition(x2, y2, z2);
		Vector3f rotation2 = ScenarioLoader.currentScenario.getPlayerCar().getRotation();
		
		carRightMirrorCamera.setRotation(0, rotation2.y - 180, 0);
	}
	
	boolean hasInited = false;
	
	private void updateCarAudio(){
		PlayerCar playerCar = ScenarioLoader.currentScenario.getPlayerCar();
		
		Vector3f cameraRotation = new Vector3f(camera.getRotation());
		
		cameraRotation.x = 0;
		cameraRotation.z = 0;
		
		AudioMaster.setListenerData(camera.getPosition(), cameraRotation);
		playerCar.getEngineSoundSource().setPosition(playerCar.getPosition());
		
		
		if(playerCar.isEngineRunning()){
			playerCar.getEngineSoundSource().setPitch(1);
			if(!playerCar.getEngineSoundSource().isPlaying()){
				if(!hasInited){
					//calls these methods once, after engine start sound stops playing				
					playerCar.getEngineSoundSource().setLooping(true);
					playerCar.getEngineSoundSource().setBuffer(ScenarioLoader.currentScenario.audioBuffers.get("engine"));				
					playerCar.getEngineSoundSource().play();
					hasInited = true;
				}
			}	
			//Engine RPM at 1.0 pitch is 6500rpm
			//Engine RPM at 0.85 pitch is 5500rpm
			//Engine RPM at 0.175 is 1000rpm
			float engineRPM = playerCar.getCarPhysics().getEngineRPM();
			
			float enginePitch = (float) (Maths.normalizeToRange(1000, 6500, engineRPM) + 0.175);
			
			if(hasInited){
				playerCar.getEngineSoundSource().setPitch(enginePitch);
			}
			
			//System.out.println("Current Pitch: "+enginePitch);
					
			//pitch += 0.1f * DisplayManager.getDeltaTime();
		}
		else{
			playerCar.getEngineSoundSource().setLooping(false);
			playerCar.getEngineSoundSource().stop();
			hasInited = false;
		}
	}


	
	private void render() {
		//Gets entities to render on screen
		ScenarioLoader.currentScenario.entitiesForRendering = GetEntitiesToRender.getEntities(camera, ScenarioLoader.currentScenario.entities);
		
		renderScene();	
		renderText();
		
		ScenarioLoader.currentScenario.lines.clear();
		
		for(int vaoID : LineDataLoader.vaoIDs) {
			Loader.vaos.remove(vaoID);
		}
		
		LineDataLoader.vaoIDs.clear();
	}
	
	private void renderScene(){
		//Render the scene through player camera
		
		if(MasterRenderer.SHADOW_MAP){
			renderer.renderShadowMap(ScenarioLoader.currentScenario.entities, ScenarioLoader.currentScenario.cars, ScenarioLoader.currentScenario.lines, ScenarioLoader.currentScenario.sun);
		}
		
		multisampleFbo.bindFrameBuffer();
		
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		renderer.renderScene(ScenarioLoader.currentScenario.entities, ScenarioLoader.currentScenario.cars, ScenarioLoader.currentScenario.normalMapEntities, ScenarioLoader.currentScenario.lines, ScenarioLoader.currentScenario.roads, ScenarioLoader.currentScenario.terrains, ScenarioLoader.currentScenario.lights, camera, new Vector4f(0, -1, 0, 100000));
				
		multisampleFbo.resolveToScreen();	
		
		// Render the scene through car mirrors camera
		carLeftMirrorFbo.bindFrameBuffer();
		renderer.renderSceneThroughMirrors(ScenarioLoader.currentScenario.entities, ScenarioLoader.currentScenario.cars, ScenarioLoader.currentScenario.normalMapEntities, ScenarioLoader.currentScenario.lines, ScenarioLoader.currentScenario.roads, ScenarioLoader.currentScenario.terrains, ScenarioLoader.currentScenario.lights, carLeftMirrorCamera, new Vector4f(0, -1, 0, 100));
		carLeftMirrorFbo.unbindFrameBuffer();
		carLeftMirrorFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, carLeftMirrorFbo);

		carRightMirrorFbo.bindFrameBuffer();
		renderer.renderSceneThroughMirrors(ScenarioLoader.currentScenario.entities, ScenarioLoader.currentScenario.cars, ScenarioLoader.currentScenario.normalMapEntities, ScenarioLoader.currentScenario.lines, ScenarioLoader.currentScenario.roads, ScenarioLoader.currentScenario.terrains, ScenarioLoader.currentScenario.lights, carRightMirrorCamera, new Vector4f(0, -1, 0, 100));
		carRightMirrorFbo.unbindFrameBuffer();
		carRightMirrorFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, carRightMirrorFbo);
		
		
		

		// ParticleMaster.renderParticles(camera);
	}
	
	/**Renders text to screen*/
	private void renderText(){	
		GUIText.setVisible(9, true);			
		
		
		GUIText.setText(9, "FPS: " + framesCountAvg);
		if(isDeveloperMode){
			GUIText.setVisible(12, true);
			GUIText.setVisible(13, true);
			GUIText.setVisible(14, true);
			GUIText.setVisible(17, true);
			GUIText.setVisible(18, true);
			GUIText.setVisible(19, true);
			GUIText.setVisible(20, true);
			GUIText.setVisible(21, true);
			
			DecimalFormat df2 = new DecimalFormat();
			
			df2.setMinimumFractionDigits(2);
			df2.setMaximumFractionDigits(2);
			
			float x = ScenarioLoader.currentScenario.getPlayerCar().getRotation().getX();
			float y = ScenarioLoader.currentScenario.getPlayerCar().getRotation().getY();
			float z = ScenarioLoader.currentScenario.getPlayerCar().getRotation().getZ();
			
			float newY = 0;
			
			if(z < 90 && z > -90){
				newY = (180 - y) % 360;
			}
			else{
				newY = (360 + y) % 360;
			}
			
			GUIText.setText(21, "Car Rotation XYZ ("+df2.format(ScenarioLoader.currentScenario.getPlayerCar().getRotation().getX())+", "+df2.format(newY)+", "+df2.format(ScenarioLoader.currentScenario.getPlayerCar().getRotation().getZ())+")");
			
			
			
			//GUIText.setText(10, "Player XYZ ("+player.getPosition().x+"m, "+player.getPosition().y+"m, "+player.getPosition().z+"m)");
			//GUIText.setText(11, "Player Rotation XYZ ("+Math.abs(player.getRotation().getX())+", "+Math.abs(player.getRotation().getY())+", "+Math.abs(player.getRotation().getZ())+")");
			GUIText.setText(12, "Camera XYZ ("+camera.getPosition().x+"m, "+camera.getPosition().y+"m, "+camera.getPosition().z+"m)");
			GUIText.setText(13, "Camera Rotation XYZ ("+camera.getRotation().x+", "+camera.getRotation().y+", "+camera.getRotation().z+")");	
			
			if(picker.getCurrentTerrainPoint() != null){
				GUIText.setText(17, "Cursor World Position ("+picker.getCurrentTerrainPoint().getX()+", "+picker.getCurrentTerrainPoint().getY()+", "+picker.getCurrentTerrainPoint().getZ()+")");
			}
			
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(1);
			df.setMaximumFractionDigits(1);
			
			float memoryUsedValue = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_073_741_824f;
			
			GUIText.setText(14, "RAM Allocated: "+df.format(memoryUsedValue)+"GB");	
		}
		
		PlayerCar pcar = ScenarioLoader.currentScenario.getPlayerCar();
		
		if(pcar != null){
			float kmh = pcar.getKMH();
			//float acceleration = pcar.getVehiclePhysics().getAccelerationKMH();
			
			DecimalFormat numFormat = new DecimalFormat();
			numFormat.setMaximumFractionDigits(2);
			numFormat.setMinimumFractionDigits(1);
			
			String strkmh = numFormat.format(kmh);
			//String strAcceleration = numFormat.format(acceleration);
			GUIText.setText(15, strkmh+" km/h");
			
			if(pcar.getGearPosition() == 0){
				GUIText.setText(16, "Gear: P");
			}
			
			if(pcar.getGearPosition() == 1){
				GUIText.setText(16, "Gear: D");
			}
			
			if(pcar.getGearPosition() == 2){
				GUIText.setText(16, "Gear: R");
			}
		
			GUIText.setText(22, "Engine RPM: "+pcar.getCarPhysics().getEngineRPM());
			
			GUIText.setVisible(15, true);
			GUIText.setVisible(16, true);
			GUIText.setVisible(22, true);
		}
		else{
			GUIText.setVisible(15, false);
			GUIText.setVisible(16, false);
			GUIText.setVisible(22, false);
		}		
	}

	/**Frees up memory from ram and closes game*/
	private void cleanUp() {
		//source.delete();
		AudioMaster.cleanUp();
		PostProcessing.cleanUp();
		//outputFbo.cleanUp();	
		//outputFbo2.cleanUp();
		multisampleFbo.cleanUp();
		carLeftMirrorFbo.cleanUp();
		//ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		Loader.cleanUp();		
		DisplayManager.destroyDisplay();
	}

	public static int getMilliSecondsPast() {
		return timePast;
	}

	/**Captures all key inputs and responds to them*/
	private void pollInput() {
		if(joystick.isControllerConnected()){
			joystick.pollController();
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				keyPressed();			
			}
		}
		keyHeldDown();	
		
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				mousePressed();
			}
		}
		
	}
	
	private void mousePressed() {
		int key = Mouse.getEventButton();
		
		if(key == 0){
			if(isDeveloperMode){
				if(picker.getCurrentTerrainPoint() != null){
					if(firstPickedPoint == null && secondPickedPoint == null){
						firstPickedPoint = picker.getCurrentTerrainPoint();
						
						GUIText.setText(18, "First Point ("+firstPickedPoint.getX()+", "+firstPickedPoint.getY()+", "+firstPickedPoint.getZ()+")");
					}
					
					
					else if(firstPickedPoint != null){	
						if(secondPickedPoint != null){
							firstPickedPoint = null;
							secondPickedPoint = null;
							
							GUIText.setText(18, "First Point");
							GUIText.setText(19, "Second Point");
							GUIText.setText(20, "Distance");
							
						}
						else{
							secondPickedPoint = picker.getCurrentTerrainPoint();
							GUIText.setText(19, "Second Point ("+secondPickedPoint.getX()+", "+secondPickedPoint.getY()+", "+secondPickedPoint.getZ()+")");
							
							GUIText.setText(20, "Distance: "+Maths.getXYZDistance(firstPickedPoint, secondPickedPoint));
						}								
					}	
				}
			}		
		}
		
	}
	
	private void keyPressed(){
		int key = Keyboard.getEventKey();
		
		getPaused();			

		PlayerCar carWithPlayer = ScenarioLoader.currentScenario.getPlayerCar();
		
		if(carWithPlayer != null){
			carWithPlayer.pollPressedKeyInput();
		}	
		
		if(key == Keyboard.KEY_V){
			if(camera.getCameraMode() == Camera.FIRST_PERSON){
				camera.setCameraMode(Camera.FREEROAM);
			}
			else if(camera.getCameraMode() == Camera.FREEROAM){
				camera.setCameraMode(Camera.FIRST_PERSON);
			}
		}
		if(key == Keyboard.KEY_N){
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.setGrabbed(false);
		}
		if(key == Keyboard.KEY_M){
			Mouse.setGrabbed(true);
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}
		if(key == Keyboard.KEY_Q){
			flagQuitCurrentScenario = true;
		}
			
	}
	
	boolean hasReachedZero = false;
	
	private void keyHeldDown(){	
		PlayerCar carWithPlayer = ScenarioLoader.currentScenario.getPlayerCar();
		
		if(carWithPlayer != null){
			carWithPlayer.recieveInputing(joystick);
		}
		
		if(joystick.isControllerConnected()){
			boolean leftPaddlePressed = joystick.getButtonValue(5);
			boolean rightPaddlePressed = joystick.getButtonValue(4);

			float maxViewAngle = 45;
			
			if(leftPaddlePressed){
				if(camera.getYawOfCameraInCar() > -maxViewAngle){
					camera.increaseYawOfCameraInCar(-120 * DisplayManager.getDeltaTime());
				}
				hasReachedZero = false;
			}		
			else if(rightPaddlePressed){
				if(camera.getYawOfCameraInCar() < maxViewAngle){
					camera.increaseYawOfCameraInCar(120 * DisplayManager.getDeltaTime());
				}
				
				hasReachedZero = false;
			}
			else{
				if(!hasReachedZero){
					if(camera.getYawOfCameraInCar() != 0){
						if(camera.getYawOfCameraInCar() > 0){
							camera.increaseYawOfCameraInCar(-120 * DisplayManager.getDeltaTime());
						}
						else{
							camera.increaseYawOfCameraInCar(120 * DisplayManager.getDeltaTime());
						}
						
						if((int)camera.getYawOfCameraInCar() == 0){
							camera.setYawOfCameraInCar(0);
							hasReachedZero = true;
						}
						//camera.setYawOfCameraInCar(0);
					}	
				}			
			}
		}
		
	}
	
	public static int getFPS(){
		return framesCountAvg;
	}
	
	private void getPaused(){

		if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {

			Main.setState(GameState.PAUSED);
			Mouse.setGrabbed(false);
			//pauseText = new GUIText("Game Paused...", 1.5f, fontArial, new Vector2f(0, 0), 1f, true);
			//TextMaster.render();
		}
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		
		String os = System.getProperty("os.name");
		
		if(os.startsWith("Windows")){
			//System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
			WIDTH = 1920;
			HEIGHT = 1080;
		}
		else if(os.equals("Mac OS X")){
			//System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
			WIDTH = 1280;
			HEIGHT = 720;
		}
		
		main.mainThread = new Thread(main,"Main Thread");
		main.mainThread.setPriority(Thread.MAX_PRIORITY);
		main.mainThread.start();
	}
}
