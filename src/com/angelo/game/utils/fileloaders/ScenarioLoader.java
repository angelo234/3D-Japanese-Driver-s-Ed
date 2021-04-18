package com.angelo.game.utils.fileloaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.audio.AudioMaster;
import com.angelo.game.car.Car;
import com.angelo.game.car.PlayerCar;
import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;
import com.angelo.game.entities.Light;
import com.angelo.game.entities.PlayerCamera;
import com.angelo.game.gameobjs.CarObject;
import com.angelo.game.gameobjs.GameObject;
import com.angelo.game.gameobjs.MTLGameObject;
import com.angelo.game.gameobjs.PNGGameObject;
import com.angelo.game.gameobjs.PhysicsMTLGameObject;
import com.angelo.game.gameobjs.PhysicsPNGGameObject;
import com.angelo.game.gameobjs.RoadObject;
import com.angelo.game.line.Line;
import com.angelo.game.line.LineDataLoader;
import com.angelo.game.models.MTLModel;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.objLoader.MTLModelData;
import com.angelo.game.objLoader.OBJMTLLoader;
import com.angelo.game.roads.RoadToObject;
import com.angelo.game.scenario.Bounds;
import com.angelo.game.scenario.BoundsGenerator;
import com.angelo.game.scenario.Scenario;
import com.angelo.game.scenario.WayPoint;
import com.angelo.game.terrains.Terrain;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.Maths;

public class ScenarioLoader {

	public static final String SCENARIO_FOLDER = "scenarios/";
	public static final String OBJECT_FOLDER = SCENARIO_FOLDER + "Objects/";
	public static final String SOUND_FOLDER = SCENARIO_FOLDER + "Sounds/";
	public static final String CAR_FOLDER = SCENARIO_FOLDER + "Cars/";	
	public static final String OBJECTS_FILE = SCENARIO_FOLDER + "Objects.txt";

	public static Scenario currentScenario;
	
	public static void loadScenario(int scenarionum, PlayerCamera camera){	
		currentScenario = new Scenario(camera);
		
		List<String> worldFile = FileLoader.loadFile(ScenarioLoader.SCENARIO_FOLDER + "Scenario_"+scenarionum+".txt", null);
		List<String> objectsFile = FileLoader.loadFile(OBJECTS_FILE, null);
			
		Map<String, GameObject> gameObjs = new HashMap<String, GameObject>();
		Map<String, CarObject> carObjs = new HashMap<String, CarObject>();
		Map<String, RoadObject> roadObjs = new HashMap<String, RoadObject>();
		
		System.out.println("Loading Scenario: "+scenarionum);	
		loadObjects(objectsFile, gameObjs, carObjs, roadObjs);
		loadScenarioFileAndMapObjects(worldFile, gameObjs, carObjs, roadObjs);
		loadOtherScenarioAttributes();

		currentScenario.initScenario();
	}
	
	public static void quitCurrentScenario(){
		currentScenario.quitScenario();
		currentScenario = null;
	}
	
	private static void loadOtherScenarioAttributes() {
		
		//Default Terrain
		Terrain terrain = new Terrain(0, 0, 500, new ModelTexture(Loader.loadGameTexture("asphalt")));
		
		currentScenario.terrains.add(terrain);
		
		//Sun
		Light sun = new Light(new Vector3f(150_000_000f, 150_000_000f, 150_000_000f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		currentScenario.lights.add(sun);
		currentScenario.sun = sun;
		
		MTLModelData modelData = OBJMTLLoader.loadOBJ("res/BMW_X5_4.obj");
		RawModel rawModel = Loader.loadToVAO(modelData);
		MTLModel mtlModel = new MTLModel("BMW", rawModel, modelData.getMTLData());
		
		//PNGModel pngModel = new PNGModel("BMW", rawModel, new ModelTexture(Loader.loadGameTexture("SilverPaint")));
		
		//ScenarioLoader.currentScenario.entities.add(new Entity(mtlModel, new Vector3f(250,0,250), new Vector3f(0,0,0), 0.025f));
		//ScenarioLoader.currentScenario.entities.add(new Entity(mtlModel, new Vector3f(240,0,240), new Vector3f(0,0,0), 0.025f));
		//ScenarioLoader.currentScenario.entities.add(new Entity(mtlModel, new Vector3f(245,0,245), new Vector3f(0,0,0), 0.025f));
		
		MTLModelData modelData2 = OBJMTLLoader.loadOBJ("res/Avent.obj");		
		RawModel rawModel2 = Loader.loadToVAO(modelData2);
		MTLModel mtlModel2 = new MTLModel("Avent", rawModel2, modelData2.getMTLData());
		//PNGModel pngModel2 = new PNGModel("Avent", rawModel2, new ModelTexture(Loader.loadGameTexture("SilverPaint")));
		//ScenarioLoader.currentScenario.entities.add(new Entity(mtlModel2, new Vector3f(260,0,250), new Vector3f(0,0,0), 1f));
		
		/*MTLModelData modelData3 = OBJMTLLoader.loadOBJ("res/stopsign.obj");		
		RawModel rawModel3 = Loader.loadToVAO(modelData3);
		MTLModel mtlModel3 = new MTLModel("Stop Sign", rawModel3, modelData3.getMTLData());
		//PNGModel pngModel2 = new PNGModel("Avent", rawModel2, new ModelTexture(Loader.loadGameTexture("SilverPaint")));
		ScenarioLoader.currentScenario.entities.add(new Entity(mtlModel3, new Vector3f(270,0,257.5f), new Vector3f(0,270,0), 1f));*/
		
		currentScenario.audioBuffers.put("engineStart", AudioMaster.loadSound("./scenarios/Sounds/Engine_Start2.wav"));
		currentScenario.audioBuffers.put("engine", AudioMaster.loadSound("./scenarios/Sounds/Engine3.wav"));
	
	}
	
	private static void loadObjects(List<String> objectsFile, Map<String, GameObject> gameObjs, Map<String, CarObject> carObjs, Map<String, RoadObject> roadObjs){	
			
		// Reading file from array
		for (String line : objectsFile) {
			String objProps = null;
			int rows = 0;

			if(line != null){
				if (!line.startsWith("#")) {
					// If regular object
					if (line.startsWith("PNGOBJ")) {
						objProps = line.replace("PNGOBJ", "").replaceAll("\\s", "");

						if (rows != 0) {
							// GlobalVariables.gameObjs.add(new GameObject(objName,
							// objFile, objTexture, rows));
						} else {
							GameObject gameObject = PNGGameObject.createGameObject(ScenarioLoader.OBJECT_FOLDER + objProps);
							gameObjs.put(gameObject.getObjName(), gameObject);
						}
					}
					else if (line.startsWith("PHYSPNGOBJ")){
						objProps = line.replace("PHYSPNGOBJ", "").replaceAll("\\s", "");
						
						if (rows != 0) {
							// GlobalVariables.gameObjs.add(new GameObject(objName,
							// objFile, objTexture, rows));
						} else {
							GameObject gameObject = PhysicsPNGGameObject.createGameObject(ScenarioLoader.OBJECT_FOLDER + objProps);
							gameObjs.put(gameObject.getObjName(), gameObject);
						}
					}
					else if (line.startsWith("MTLOBJ")) {
						objProps = line.replace("MTLOBJ", "").replaceAll("\\s", "");
						
						GameObject mtlGameObject = MTLGameObject.createMTLGameObject(ScenarioLoader.OBJECT_FOLDER + objProps);
						gameObjs.put(mtlGameObject.getObjName(), mtlGameObject);
						
					}
					else if (line.startsWith("PHYSMTLOBJ")) {
						objProps = line.replace("PHYSMTLOBJ", "").replaceAll("\\s", "");
						
						GameObject mtlGameObject = PhysicsMTLGameObject.createPhysicsMTLGameObject(ScenarioLoader.OBJECT_FOLDER + objProps);
						gameObjs.put(mtlGameObject.getObjName(), mtlGameObject);
						
					}
					// If car object
					else if (line.startsWith("CAROBJ")) {
						objProps = line.replace("CAROBJ", "").replaceAll("\\s", "");
						
						CarObject carObject = CarObject.createCarObject(ScenarioLoader.OBJECT_FOLDER + objProps);		
						carObjs.put(carObject.getObjName(), carObject);
					}
					
					// If road object
					else if (line.startsWith("ROADOBJ")) {
						objProps = line.replace("ROADOBJ", "").replaceAll("\\s", "");
						
						RoadObject roadObject = RoadObject.createRoadObject(ScenarioLoader.OBJECT_FOLDER + objProps);						
						roadObjs.put(roadObject.getObjName(), roadObject);
					}
				}
			}
		}
	}

	private static void loadScenarioFileAndMapObjects(List<String> worldFile, Map<String, GameObject> gameObjs, Map<String, CarObject> carObjs, Map<String, RoadObject> roadObjs) {
		Map<String, TexturedModel> models = new HashMap<String, TexturedModel>();
		
		// Load objects into VAOs
		for(String key : gameObjs.keySet()){
			GameObject gameObj = gameObjs.get(key);
			
			//PNGGameObject
			if(gameObj instanceof PNGGameObject){
				PNGGameObject pngGameObj = (PNGGameObject) gameObj;
				
				PNGModel model = PNGModel.createPNGModelFromScenario(pngGameObj.getObjName(), pngGameObj.getObjFile(), pngGameObj.getObjTexture());
				
				if (pngGameObj.getRows() != 0) {
					model.getTexture().setNumberOfRows(pngGameObj.getRows());
				}
				
				models.put(pngGameObj.getObjName(), model);
			}
			//PhysicsPNGGameObject
			else if(gameObj instanceof PhysicsPNGGameObject){
				PhysicsPNGGameObject physGameObj = (PhysicsPNGGameObject) gameObj;
				
				PNGModel model = PNGModel.createPNGModelFromScenario(physGameObj.getObjName(), physGameObj.getObjFile(), physGameObj.getObjTexture(), physGameObj.getAABBSize(), physGameObj.getMass(), physGameObj.getFriction(), physGameObj.getRestitution());
				
				if (physGameObj.getRows() != 0) {
					model.getTexture().setNumberOfRows(physGameObj.getRows());
				}
				
				models.put(physGameObj.getObjName(), model);
			}
			//MTLGameObject
			else if(gameObj instanceof MTLGameObject){
				MTLGameObject mtlGameObj = (MTLGameObject) gameObj;
				
				MTLModel model = MTLModel.createMTLModelFromScenario(mtlGameObj.getObjName(), mtlGameObj.getObjFile());
				models.put(mtlGameObj.getObjName(), model);
				
			}	
			//PhysicsMTLGameObject
			else if(gameObj instanceof PhysicsMTLGameObject){
				PhysicsMTLGameObject physMtlGameObj = (PhysicsMTLGameObject) gameObj;
				
				MTLModel model = MTLModel.createMTLModelFromScenario(physMtlGameObj.getObjName(), physMtlGameObj.getObjFile(), physMtlGameObj.getAABBSize(), physMtlGameObj.getMass(), physMtlGameObj.getFriction(), physMtlGameObj.getRestitution());
				models.put(physMtlGameObj.getObjName(), model);
			}
			else{}
		}
		
		boolean waypoints = false;
		boolean bounds = false;
		boolean objects = false;
		boolean car = false;	
		int boundsIndex = 0;
		
		Vector3f nearXLeft = null;
		Vector3f nearXRight = null;
		Vector3f farXLeft = null;
		Vector3f farXRight = null;
		
		for(String line : worldFile){
			if(line != null){
				if(line.startsWith("SCENARIO_NAME")){
					String name = line.split("=")[1];
					currentScenario.setScenarioName(name);
				}
				
				if(line.startsWith(")")){
					waypoints = false;					
				}			
				else if(waypoints){
					float x, y, z, rotX, rotY, rotZ, displayTextRadius;
					String strWayPointType, text;
					
					String lineData[] = line.split(",");
					
					x = Float.parseFloat(lineData[0]);
					y = Float.parseFloat(lineData[1]);
					z = Float.parseFloat(lineData[2]);
					rotX = Float.parseFloat(lineData[3]);
					rotY = Float.parseFloat(lineData[4]);
					rotZ = Float.parseFloat(lineData[5]);
					strWayPointType = lineData[6];	
					text = lineData[7];
					displayTextRadius = Float.parseFloat(lineData[8]);
					
					int wayPointType = WayPoint.stringToWayPointType(strWayPointType);				
					currentScenario.setWayPoint(new WayPoint(new Vector3f(x,y,z), new Vector3f(rotX, rotY, rotZ), wayPointType, text, displayTextRadius));			
				}
				
				if(line.equals("WAYPOINTS(")){
					waypoints = true;
				}
				
				if(bounds){
					if(line.startsWith(")")){
						bounds = false;
						currentScenario.setCourseBounds(new Bounds(nearXLeft, nearXRight, farXLeft, farXRight));
					}
					else{
						String xyz[] = line.split(",");	
						switch(boundsIndex){
						case 0:{				
							nearXLeft = new Vector3f(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2]));
						}
						case 1:{			
							nearXRight = new Vector3f(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2]));
						}
						case 2:{					
							farXLeft = new Vector3f(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2]));
						}		
						case 3:{	
							farXRight = new Vector3f(Float.parseFloat(xyz[0]), Float.parseFloat(xyz[1]), Float.parseFloat(xyz[2]));
						}
						}
						boundsIndex++;
					}
				}
				
				if(line.equals("BOUNDS(")){
					bounds = true;
				}
				
				if(objects){
					if(line.startsWith(")")){
						objects = false;
					}
					else if(line.startsWith("obj")){
						if(line.contains("(")&&line.contains(")")){
							String objName;
							float posX, posY, posZ, rotX, rotY, rotZ, scale, reflectivity, shineDamper;
							boolean isTransparent, usingFakeLighting;
							
							String lineObjData = line.split("\\(")[1];				
							lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");

							//Index 0 is obj name and index 1~7 are obj data values
							String data[] = lineObjData.split(",");
							
							int parameters = data.length;

							objName = data[0];	
							posX = Float.parseFloat(data[1]);
							posY = Float.parseFloat(data[2]);
							posZ = Float.parseFloat(data[3]);
							rotX = Float.parseFloat(data[4]);
							rotY = Float.parseFloat(data[5]);
							rotZ = Float.parseFloat(data[6]);
							scale = Float.parseFloat(data[7]);
							
							TexturedModel model = models.get(objName);
							
							if(model != null){
								if(model instanceof PNGModel){
									PNGModel pngModel = (PNGModel) model;
									
									if(pngModel.obeysPhysics()){
										if(parameters == 8){					
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale, pngModel.getAABBSize(), pngModel.getMass(), pngModel.getFriction(), pngModel.getRestitution()));
										}
										else if(parameters == 10){
											isTransparent = Boolean.parseBoolean(data[8]);
											usingFakeLighting = Boolean.parseBoolean(data[9]);
											
											pngModel.getTexture().setTransparent(isTransparent);
											pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
											
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale, pngModel.getAABBSize(), pngModel.getMass(), pngModel.getFriction(), pngModel.getRestitution()));
										}
										else if(parameters == 12){
											isTransparent = Boolean.parseBoolean(data[8]);
											usingFakeLighting = Boolean.parseBoolean(data[9]);
											reflectivity = Float.parseFloat(data[10]);
											shineDamper = Float.parseFloat(data[11]);
											
											pngModel.getTexture().setTransparent(isTransparent);
											pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
											pngModel.getTexture().setReflectivity(reflectivity);
											pngModel.getTexture().setShineDamper(shineDamper);
											
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale, pngModel.getAABBSize(), pngModel.getMass(), pngModel.getFriction(), pngModel.getRestitution()));
										}
									}
									//Static objects
									else{
										if(parameters == 8){
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
										}
										else if(parameters == 10){
											isTransparent = Boolean.parseBoolean(data[8]);
											usingFakeLighting = Boolean.parseBoolean(data[9]);
											
											pngModel.getTexture().setTransparent(isTransparent);
											pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
											
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
										}
										else if(parameters == 12){
											isTransparent = Boolean.parseBoolean(data[8]);
											usingFakeLighting = Boolean.parseBoolean(data[9]);
											reflectivity = Float.parseFloat(data[10]);
											shineDamper = Float.parseFloat(data[11]);
											
											pngModel.getTexture().setTransparent(isTransparent);
											pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
											pngModel.getTexture().setReflectivity(reflectivity);
											pngModel.getTexture().setShineDamper(shineDamper);
											
											currentScenario.entities.add(new Entity(pngModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
										}
									}
									
									
								}
								else if(model instanceof MTLModel){
									MTLModel mtlModel = (MTLModel) model;
									
									if(mtlModel.obeysPhysics()){
										if(parameters == 8){					
											currentScenario.entities.add(new Entity(mtlModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale, mtlModel.getAABBSize(), mtlModel.getMass(), mtlModel.getFriction(), mtlModel.getRestitution()));
										}
									}
									else{
										if(parameters == 8){					
											currentScenario.entities.add(new Entity(mtlModel, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
										}
									}
								}
							}			
						}		
					}
					
					else if(line.startsWith("objat")){
						if(line.contains("(")&&line.contains(")")){
							String objName;
							int index;
							float posX, posY, posZ, rotX, rotY, rotZ, scale, reflectivity, shineDamper;
							boolean isTransparent, usingFakeLighting;
							
							String lineObjData = line.split("\\(")[1];				
							lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");

							//Index 0 is obj name and index 1~7 are obj data values
							String data[] = lineObjData.split(",");
							
							int parameters = data.length;

							objName = data[0];	
							index = Integer.parseInt(data[1]);
							posX = Float.parseFloat(data[2]);
							posY = Float.parseFloat(data[3]);
							posZ = Float.parseFloat(data[4]);
							rotX = Float.parseFloat(data[5]);
							rotY = Float.parseFloat(data[6]);
							rotZ = Float.parseFloat(data[7]);
							scale = Float.parseFloat(data[8]);
							
							TexturedModel model = models.get(objName);
							
							if(model != null){
								if(model instanceof PNGModel){
									PNGModel pngModel = (PNGModel) model;
									
									if(parameters == 9){
										currentScenario.entities.add(new Entity(pngModel, index, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
									}
									else if(parameters == 11){
										isTransparent = Boolean.parseBoolean(data[9]);
										usingFakeLighting = Boolean.parseBoolean(data[10]);
										
										pngModel.getTexture().setTransparent(isTransparent);
										pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
										
										currentScenario.entities.add(new Entity(pngModel, index, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
									}
									else if(parameters == 13){
										isTransparent = Boolean.parseBoolean(data[9]);
										usingFakeLighting = Boolean.parseBoolean(data[10]);
										reflectivity = Float.parseFloat(data[11]);
										shineDamper = Float.parseFloat(data[12]);
										
										pngModel.getTexture().setTransparent(isTransparent);
										pngModel.getTexture().setUseFakeLighting(usingFakeLighting);
										pngModel.getTexture().setReflectivity(reflectivity);
										pngModel.getTexture().setShineDamper(shineDamper);
										
										currentScenario.entities.add(new Entity(pngModel, index, new Vector3f(posX, posY, posZ), new Vector3f(rotX, rotY, rotZ), scale));
									}
									else{
										System.err.println("Could not parse method call arguments into an ObjectData");
										System.err.println(lineObjData);
									}			
								}							
							}			
						}						
					}
					
					else if(line.startsWith("roadNode")){
						float x, y, z;
						boolean curved;
						
						String lineObjData = line.split("\\(")[1];				
						lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");

						String data[] = lineObjData.split(",");
						
						x = Float.parseFloat(data[0]);
						y = Float.parseFloat(data[1]);
						z = Float.parseFloat(data[2]);
						
						if(data[3] != null){
							curved = Boolean.parseBoolean(data[3]);
						}
						else{
							curved = false;
						}
								
						RoadToObject.setRoadNode(new Vector3f(x,y,z), curved);
					}
					
					else if(line.startsWith("createRoad")){
						int index;
						
						String lineObjData = line.split("\\(")[1];				
						lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");
						
						index = Integer.parseInt(lineObjData);
						
						RoadToObject.createRoad(index);
					}
					
					else if(line.startsWith("line")){
						float x1, y1, z1, x2, y2, z2, width, r, g, b, length, spacing;
						
						String lineObjData = line.split("\\(")[1];				
						lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");
						
						String data[] = lineObjData.split(",");
						
						int parameters = data.length;
						
						x1 = Float.parseFloat(data[0]);
						y1 = Float.parseFloat(data[1]);
						z1 = Float.parseFloat(data[2]);
						x2 = Float.parseFloat(data[3]);
						y2 = Float.parseFloat(data[4]);
						z2 = Float.parseFloat(data[5]);
						width = Float.parseFloat(data[6]);
						r = Float.parseFloat(data[7]);
						g = Float.parseFloat(data[8]);
						b = Float.parseFloat(data[9]);
						
						//Create dashed line
						if(parameters == 12){
							length = Float.parseFloat(data[10]);
							spacing = Float.parseFloat(data[11]);
							
							float distance = Maths.getXYZDistance(x1, y1, z1, x2, y2, z2);
							
							float xStep = x2 - x1;
							float yStep = y2 - y1;
							float zStep = z2 - z1;
												
							int lines = (int) (distance / (length + spacing)) + 1;
							for(int i = 0; i < lines; i++){
								float x11 = x1 + (xStep * i);
								float y11 = y1 + (yStep * i);
								float z11 = z1 + (zStep * i);
								
								//float  = 1;
								
								//currentScenario.lines.add(new Line(new Vector3f(x,y,z), new Vector3f(x2,y2,z2), new Vector3f(r,g,b), width));
							}
							
						}
						//Create regular line
						else{
							currentScenario.lines.add(new Line(new Vector3f(x1,y1,z1), new Vector3f(x2,y2,z2), new Vector3f(r,g,b), width));
						}
						
						
					}
					else if(line.startsWith("bline")){
						float x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, width, r, g, b;
						int lines;
						
						String lineObjData = line.split("\\(")[1];				
						lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");
						
						String data[] = lineObjData.split(",");
						
						x1 = Float.parseFloat(data[0]);
						y1 = Float.parseFloat(data[1]);
						z1 = Float.parseFloat(data[2]);
						x2 = Float.parseFloat(data[3]);
						y2 = Float.parseFloat(data[4]);
						z2 = Float.parseFloat(data[5]);
						x3 = Float.parseFloat(data[6]);
						y3 = Float.parseFloat(data[7]);
						z3 = Float.parseFloat(data[8]);
						x4 = Float.parseFloat(data[9]);
						y4 = Float.parseFloat(data[10]);
						z4 = Float.parseFloat(data[11]);
						lines = Integer.parseInt(data[12]);
						width = Float.parseFloat(data[13]);
						r = Float.parseFloat(data[14]);
						g = Float.parseFloat(data[15]);
						b = Float.parseFloat(data[16]);
						
						List<Vector3f> bpoints = Maths.generateBezierCurve(lines, new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3), new Vector3f(x4, y4, z4));
						
						for(int i = 0; i < bpoints.size(); i++){			
							Vector3f currentPoint = bpoints.get(i);
							Vector3f nextPoint = null;
							
							try{
								nextPoint = bpoints.get(i + 1);
							}
							catch(IndexOutOfBoundsException e){}
							
							if(nextPoint != null){					
								currentScenario.lines.add(new Line(currentPoint, nextPoint, new Vector3f(r,g,b), width));							
							}
							else{
								break;
							}
						}				
					}
				}
					
				if(line.equals("OBJECTS(")){
					objects = true;
				}
				
				if(car){
					if(line.startsWith(")")){
						car = false;
					}
					if(line.startsWith("car")){
						if(line.contains("(")&&line.contains(")")){
							String objName;
							float posX, posY, posZ, rotX, rotY, rotZ;
							boolean isPlayerCar;
							
							String lineObjData = line.split("\\(")[1];				
							lineObjData = lineObjData.replace(")", "").replaceAll("\\s", "");

							String data[] = lineObjData.split(",");
							
							int parameters = data.length;

							objName = data[0];	
							posX = Float.parseFloat(data[1]);
							posY = Float.parseFloat(data[2]);
							posZ = Float.parseFloat(data[3]);
							rotX = Float.parseFloat(data[4]);
							rotY = Float.parseFloat(data[5]);
							rotZ = Float.parseFloat(data[6]);
							isPlayerCar = Boolean.parseBoolean(data[7]);
							
							//Checks to see if there are 8 parameters 
						
							if(parameters == 8){	
								CarObject carObj = carObjs.get(objName);
								
								Car entityCar = CarFileLoader.loadCar(objName, new Vector3f(posX,posY,posZ), new Vector3f(rotX,rotY,rotZ), carObj.getAABBSize(), carObj.getProps(), isPlayerCar);
								currentScenario.setCar((PlayerCar) entityCar);
							}
						}		
					}
				}
				
				if(line.equals("CAR(")){
					car = true;
				}
			}
		}
		/*
		// Maps objects into world
		for (TexturedModel object : models) {
			for (GameObject gameObj : gameObjs) {
				for (ObjectData objData : objDatas) {
					if (object.getModelName().equals(objData.getObjName()) && object.getModelName().equals(gameObj.getObjName())) {
						if (objData.getReflectivity() > 0 || objData.getReflectivity() < -0) {
							object.getTexture().setReflectivity(objData.getReflectivity());
						}
						if (objData.getShineDamper() > 0 || objData.getReflectivity() < -0) {
							object.getTexture().setShineDamper(objData.getShineDamper());
						}
						if (objData.isTransparent()) {
							object.getTexture().setTransparent(true);
						}
						if (objData.isUsingFakeLighting()) {
							object.getTexture().setUseFakeLighting(true);
						}

						if (objData.getIndex() != -1) {
							currentScenario.entities.add(new Entity(object, objData.getIndex(), new Vector3f(objData.getPosition().x, objData.getPosition().y, objData.getPosition().z), new Vector3f(objData.getRotX(), objData.getRotY(), objData.getRotZ()), objData.getScale()));
						} else {
							currentScenario.entities.add(new Entity(object,new Vector3f(objData.getPosition().x, objData.getPosition().y,objData.getPosition().z),new Vector3f(objData.getRotX(), objData.getRotY(), objData.getRotZ()),objData.getScale(), gameObj.getAABBSize()));
						}
					}
				}
			}
		}

		System.out.println("Loaded " + currentScenario.entities.size() + " objects");

		*/

		/*
		// Creates one big object entity out of every "RoadSegment" object
		for (Road road : currentScenario.roads) {
			for (RoadObject object : roadObjs) {
				TexturedModel model;
				ModelTexture texture;
				RawModel rawModel;

				rawModel = Loader.loadToVAO(OBJFileLoader.loadOBJ(OBJLoader.getObjModelFromScenario(object.getObjFile())));
				texture = new ModelTexture(Loader.loadGameTextureFromObjectFolder(object.getObjTexture()));

				model = new TexturedModel(object.getObjName(), rawModel, texture);

				TexturedModel marker = new TexturedModel(OBJLoader.loadObjModel("marker"),
						new ModelTexture(Loader.loadGameTexture("black")));
				TexturedModel marker2 = new TexturedModel(OBJLoader.loadObjModel("marker"),
						new ModelTexture(Loader.loadGameTexture("white")));

				RoadToObject.convertRoad(road, texture, marker, marker2);
			}
		}
		 */
		// Loads lines into world
		for (Line line : currentScenario.lines) {
			LineDataLoader.loadLineModel(line);
		}

		BoundsGenerator.generateBounds(currentScenario);

		/*
		 * courses.add(new Course("Course 1", new Dimensions(new
		 * Vector3f(200,0,120),new Vector3f(200,0,100), new Vector3f(220,0,120),
		 * new Vector3f(220,0,100)), null));
		 * 
		 * //Loads courses for(Course course : courses){
		 * CourseGenerator.generateCourse(course); }
		 */

		int carsLoaded = 0;

		for (String key : carObjs.keySet()) {
			CarObject object = carObjs.get(key);
			for (Car entitycar : currentScenario.cars) {
				if (object.getObjName().equals(entitycar.getCarObjFileName())) {
					//Set car body model
					if(object.isUsingMTLTexture()){
						MTLModel model = MTLModel.createMTLModelFromScenario(object.getObjName(), object.getObjFile());
						entitycar.setCarModel(model);
					}
					else{			
						PNGModel model = PNGModel.createPNGModelFromScenario(object.getObjName(), object.getObjFile(), object.getObjTexture());
						entitycar.setCarModel(model);
					}
					//Set car interior model
					if(object.isUsingMTLInteriorTexture()){
						MTLModel model = MTLModel.createMTLModelFromScenario(object.getObjName(), object.getObjFile());
						//MTLModel model = MTLModel.createMTLModelFromScenario(object.getObjName(), object.getInteriorObjFile());
						entitycar.setCarInteriorModel(model);
					}
					else{			
						PNGModel model = PNGModel.createPNGModelFromScenario(object.getObjName(), object.getObjFile(), object.getObjTexture());
						//PNGModel model = PNGModel.createPNGModelFromScenario(object.getObjName(), object.getInteriorObjFile(), object.getInteriorObjTexture());
						entitycar.setCarInteriorModel(model);				
					}
					if(object.isUsingMTLTireTexture()){
						MTLModel model = MTLModel.createMTLModelFromScenario(object.getObjName(), object.getTireObjFile());
						
						entitycar.setTireModel(model);
					}
					else{
						PNGModel model = PNGModel.createPNGModelFromScenario(object.getObjName(), object.getTireObjFile(), object.getTireObjTexture());
						
						entitycar.setTireModel(model);
					}
					carsLoaded++;
				}
			}
		}

		System.out.println("Loaded " + carsLoaded + " cars");
	}

}
