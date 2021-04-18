package com.angelo.game.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.GameState;
import com.angelo.game.Main;
import com.angelo.game.audio.Source;
import com.angelo.game.car.Car;
import com.angelo.game.car.PlayerCar;
import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;
import com.angelo.game.entities.Light;
import com.angelo.game.entities.PlayerCamera;
import com.angelo.game.guis.buttons.GUIButton;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.line.Line;
import com.angelo.game.roads.Road;
import com.angelo.game.terrains.Terrain;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

public class Scenario {

	public List<Entity> entities = new ArrayList<Entity>();
	public List<Entity> entitiesForRendering = new ArrayList<Entity>();
	public List<Terrain> terrains = new ArrayList<Terrain>();
	public List<Light> lights = new ArrayList<Light>();
	public List<GUIText> scenarioTexts = new ArrayList<GUIText>();
	public List<Entity> normalMapEntities = new ArrayList<Entity>();
	public Map<String, Integer> audioBuffers = new HashMap<String, Integer>();

	public List<Car> cars = new ArrayList<Car>();
	public List<Road> roads = new ArrayList<Road>();
	public List<Line> lines = new ArrayList<Line>();

	public Light sun;
	
	private String scenarioName;
	private List<WayPoint> wayPoints = new ArrayList<WayPoint>();
	private Bounds courseBounds;
	private PlayerCar car;
	private PlayerCamera camera;
	
	private static int guiTextOffsetID = 50;
	private static int scenarioEndButtonID = 70;
	
	public Scenario(PlayerCamera camera){
		this.camera = camera;
	}
	
	/**Sets up the scenario*/
	public void initScenario(){	
		camera.setFixToPlayerCar(true);
		//car.setPosition(this.getStartingPoint().getPosition());
		//car.setRotation(this.getStartingPoint().getRotation());
		car.setOperateable(true);
		
		int i = guiTextOffsetID;
		
		for(WayPoint wayPoint : ScenarioLoader.currentScenario.wayPoints){
			GUIText.createText(i, wayPoint.getText(), 1, Main.fontArial, new Vector2f(0.5f, 0.2f), 1, true);
			GUIText.setVisible(i, false);
			i++;
		}
		
		GUIButton.createButton(scenarioEndButtonID, new Vector2f(0.45f,0.475f), new Vector2f(0.55f,0.525f), "Finish", 1, Main.fontArial, Loader.loadGameTexture("Button"));
		GUIButton.setVisible(scenarioEndButtonID, false);
	}
	
	public void quitScenario(){
		GUIText.setVisible(9, false);
		GUIText.setVisible(12, false);
		GUIText.setVisible(13, false);
		GUIText.setVisible(14, false);
		GUIText.setVisible(15, false);
		GUIText.setVisible(16, false);
		GUIText.setVisible(22, false);
		
		entities.clear();
		entitiesForRendering.clear();
		terrains.clear();
		lights.clear();
		scenarioTexts.clear();
		normalMapEntities.clear();
		cars.clear();
		roads.clear();
		lines.clear();
		audioBuffers.clear();
		sun = null;
		scenarioName = null;
		wayPoints.clear();
		courseBounds = null;
		car = null;
		camera = null;
	}
	
	public void update(){
		PlayerCar pcar = ScenarioLoader.currentScenario.getPlayerCar();
		
		int i = guiTextOffsetID;
		
		ScenarioInputPolling.pollInput(this);
		
		//Display waypoint text if player car is in range of waypoint
		for(WayPoint wayPoint : ScenarioLoader.currentScenario.wayPoints){
			if(ScenarioLoader.currentScenario.isPlayerCarInWayPoint(wayPoint, wayPoint.getDisplayTextRadius(), false)){
				GUIText.setVisible(i, true);
			}
			else{
				GUIText.setVisible(i, false);
			}
			i++;
		}
		
		//If the car stops in the destination area (parking space), quit the scenario
		if(ScenarioLoader.currentScenario.isPlayerCarInWayPoint(ScenarioLoader.currentScenario.getDestinationPoint(), 0.75f, true)){
			if((int)pcar.getKMH() == 0 && pcar.getGearPosition() == 0){
				pcar.setOperateable(false);
				//pcar.setVelocity(0,0,0);
				pcar.setPosition(ScenarioLoader.currentScenario.getDestinationPoint().getPosition());
				//Main.setState(GameState.PAUSED);
				Mouse.setGrabbed(false);
				
				GUIButton.setVisible(scenarioEndButtonID, true);
				
				int k = guiTextOffsetID;
				for(WayPoint wayPoint : ScenarioLoader.currentScenario.wayPoints){
					GUIText.setVisible(k, false);
					k++;
				}
				
				if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
					Main.flagQuitCurrentScenario = true;
				}
			}
		}
	}
	
	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public List<WayPoint> getWayPoints() {
		return wayPoints;
	}
	
	public void setWayPoint(WayPoint wayPoint) {
		this.wayPoints.add(wayPoint);
	}
	
	public void setWayPoints(List<WayPoint> wayPoints) {
		this.wayPoints = wayPoints;
	}
	
	public Bounds getCourseBounds() {
		return courseBounds;
	}

	public void setCourseBounds(Bounds courseBounds) {
		this.courseBounds = courseBounds;
	}
	
	public PlayerCar getPlayerCar() {
		return car;
	}

	public void setCar(PlayerCar car) {
		this.car = car;
	}

	public WayPoint getStartingPoint(){
		for(WayPoint wayPoint : wayPoints){
			if(wayPoint.getType() == WayPoint.BEGIN){
				return wayPoint;
			}
		}
		return null;
	}
	
	public WayPoint getDestinationPoint(){
		for(WayPoint wayPoint : wayPoints){
			if(wayPoint.getType() == WayPoint.END){
				return wayPoint;
			}
		}
		return null;
	}
	
	public boolean isPlayerCarInWayPoint(WayPoint wayPoint, float areaSize, boolean checkRotation){
		float angleError = 10;
		
		Vector3f carPos = this.getPlayerCar().getPosition();
		Vector3f carRot = this.getPlayerCar().getRotation();
		
		Vector3f pointPos = wayPoint.getPosition();
		Vector3f pointRot = wayPoint.getRotation();
		
		/*System.out.println("Positions");
		System.out.println(carPos+"_"+pointPos);
		System.out.println("Rotations");
		System.out.println(carRot+"_"+pointRot);
		*/
		
		if(checkRotation){
			//Check if player car's rotation is about the same as the waypoint area (+-10 error)
			if(carRot.getY() < pointRot.getY() + angleError && carRot.getY() > pointRot.getY() - angleError){
				if(carPos.getX() < pointPos.getX() + areaSize && carPos.getX() > pointPos.getX() - areaSize){
					if(carPos.getY() < pointPos.getY() + areaSize && carPos.getY() > pointPos.getY() - areaSize){
						if(carPos.getZ() < pointPos.getZ() + areaSize && carPos.getZ() > pointPos.getZ() - areaSize){
							return true;
						}
					}
				}
			}
		}
		else {
			if (carPos.getX() < pointPos.getX() + areaSize && carPos.getX() > pointPos.getX() - areaSize) {
				if (carPos.getY() < pointPos.getY() + areaSize && carPos.getY() > pointPos.getY() - areaSize) {
					if (carPos.getZ() < pointPos.getZ() + areaSize && carPos.getZ() > pointPos.getZ() - areaSize) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
