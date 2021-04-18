package com.angelo.game.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.angelo.game.car.Car;
import com.angelo.game.car.CarRenderer;
import com.angelo.game.car.CarShader;
import com.angelo.game.car.TireRenderer;
import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;
import com.angelo.game.entities.Light;
import com.angelo.game.line.Line;
import com.angelo.game.line.LineRenderer;
import com.angelo.game.line.LineShader;
import com.angelo.game.models.MTLModel;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.normalMappingRenderer.NormalMappingRenderer;
import com.angelo.game.roads.Road;
import com.angelo.game.roads.shaders.RoadRenderer;
import com.angelo.game.roads.shaders.RoadShader;
import com.angelo.game.shaders.StaticShader;
import com.angelo.game.shaders.TerrainShader;
import com.angelo.game.shaders.mtl.MTLEntityRenderer;
import com.angelo.game.shaders.mtl.MTLStaticShader;
import com.angelo.game.shadows.ShadowMapMasterRenderer;
import com.angelo.game.skybox.SkyboxRenderer;
import com.angelo.game.terrains.Terrain;

public class MasterRenderer {

	public static float FOV = 45;
	public static float NEAR_PLANE = 0.1f;
	public static float FAR_PLANE = 1000;
	
	public static final float RED = 0.5444f;
	public static final float GREEN = 0.62f;
	public static final float BLUE = 0.69f;
	
	public static final boolean SHADOW_MAP = true;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private MTLStaticShader mtlShader = new MTLStaticShader();
	private MTLEntityRenderer mtlRenderer;
	
	private NormalMappingRenderer normalMapRenderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private LineRenderer lineRenderer;
	private LineShader lineShader = new LineShader();
	
	private RoadRenderer roadRenderer;
	private RoadShader roadShader = new RoadShader();
	
	private CarRenderer carRenderer;
	private CarShader carShader = new CarShader();
	
	private TireRenderer tireRenderer;
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private Map<MTLModel,List<Entity>> mtlEntities = new HashMap<MTLModel,List<Entity>>();
	private Map<TexturedModel[],List<Car>> cars = new HashMap<TexturedModel[],List<Car>>();
	private Map<TexturedModel,List<Entity>> normalMapEntities = new HashMap<TexturedModel,List<Entity>>();
	private Map<TexturedModel,List<Road>> roads = new HashMap<TexturedModel,List<Road>>();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Line> lines = new ArrayList<Line>();
	
	private SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	public MasterRenderer(Camera camera){
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		mtlRenderer = new MTLEntityRenderer(mtlShader,projectionMatrix);
		carRenderer = new CarRenderer(mtlShader,projectionMatrix);
		tireRenderer = new TireRenderer(carShader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		roadRenderer = new RoadRenderer(roadShader,projectionMatrix);
		lineRenderer = new LineRenderer(lineShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}
	
	public void renderScene(List<Entity> entities, List<Car> cars, List<Entity> normalEntities, List<Line> lines, List<Road> roads, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane){
		
		for(Terrain terrain:terrains){
			processTerrain(terrain);
		}
		
		for(Entity entity:entities){
			if(entity.getModel() instanceof PNGModel){
				processEntity(entity);
			}
			else if(entity.getModel() instanceof MTLModel){
				processMTLEntity(entity);
			}
		}
		
		for(Car car:cars){
			processCar(car);
		}
		
		for(Entity entity:normalEntities){
			processNormalMapEntity(entity);
		}
		
		for(Line line:lines){
			processLine(line);
		}
		
		for(Road road:roads){
			processRoad(road);
		}
		render(lights, camera, clipPlane);
	}
	
	public void renderSceneThroughMirrors(List<Entity> entities, List<Car> cars, List<Entity> normalEntities, List<Line> lines, List<Road> roads, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane){
		
		for(Terrain terrain:terrains){
			processTerrain(terrain);
		}
		
		for(Entity entity:entities){
			if(entity.getModel() instanceof PNGModel){
				processEntity(entity);
			}
			else if(entity.getModel() instanceof MTLModel){
				processMTLEntity(entity);
			}
		}
		
		for(Car car:cars){
			processCar(car);
		}
		
		for(Entity entity:normalEntities){
			processNormalMapEntity(entity);
		}
		
		for(Line line:lines){
			processLine(line);
		}
		
		for(Road road:roads){
			processRoad(road);
		}
		renderThroughMirrors(lights, camera, clipPlane);
	}

	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane){	
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		
		mtlShader.start();
		mtlShader.loadClipPlane(clipPlane);
		mtlShader.loadSkyColor(RED, GREEN, BLUE);
		mtlShader.loadLights(lights);
		mtlShader.loadViewMatrix(camera);
		mtlRenderer.render(mtlEntities, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		mtlShader.stop();
		
		mtlShader.start();
		mtlShader.loadClipPlane(clipPlane);
		mtlShader.loadSkyColor(RED, GREEN, BLUE);
		mtlShader.loadLights(lights);
		mtlShader.loadViewMatrix(camera);
		carRenderer.render(cars, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());	
		mtlShader.stop();
		
		carShader.start();
		carShader.loadClipPlane(clipPlane);
		carShader.loadSkyColor(RED, GREEN, BLUE);
		carShader.loadLights(lights);
		carShader.loadViewMatrix(camera);
		tireRenderer.render(cars, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		carShader.stop();
		
		roadShader.start();
		roadShader.loadClipPlane(clipPlane);
		roadShader.loadSkyColor(RED, GREEN, BLUE);
		roadShader.loadLights(lights);
		roadShader.loadViewMatrix(camera);
		roadRenderer.render(roads, camera);
		roadShader.stop();
		
		lineShader.start();
		lineShader.loadClipPlane(clipPlane);
		lineShader.loadSkyColor(RED, GREEN, BLUE);
		lineShader.loadLights(lights);
		lineShader.loadViewMatrix(camera);
		lineRenderer.render(lines, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		lineShader.stop();
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		normalMapRenderer.render(normalMapEntities, lights, camera);
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
		terrains.clear();
		entities.clear();
		cars.clear();
		normalMapEntities.clear();
		roads.clear();
		lines.clear();
	}
	
	public void renderThroughMirrors(List<Light> lights, Camera camera, Vector4f clipPlane){	
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadViewMatrix(camera);
		renderer.render(entities, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		
		mtlShader.start();
		mtlShader.loadClipPlane(clipPlane);
		mtlShader.loadViewMatrix(camera);
		mtlRenderer.render(mtlEntities, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		mtlShader.stop();
		
		mtlShader.start();
		mtlShader.loadClipPlane(clipPlane);
		mtlShader.loadViewMatrix(camera);
		carRenderer.render(cars, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());	
		mtlShader.stop();
		
		carShader.start();
		carShader.loadClipPlane(clipPlane);
		carShader.loadViewMatrix(camera);
		tireRenderer.render(cars, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		carShader.stop();
		
		roadShader.start();
		roadShader.loadClipPlane(clipPlane);
		roadShader.loadViewMatrix(camera);
		roadRenderer.render(roads, camera);
		roadShader.stop();
		
		lineShader.start();
		lineShader.loadClipPlane(clipPlane);
		lineShader.loadViewMatrix(camera);
		lineRenderer.render(lines, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		lineShader.stop();
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		normalMapRenderer.render(normalMapEntities, lights, camera);
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
		terrains.clear();
		entities.clear();
		cars.clear();
		normalMapEntities.clear();
		roads.clear();
		lines.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	private void processLine(Line line) {
		lines.add(line);		
	}
	
	private void processCar(Car car) {
		TexturedModel carModel = car.getCarModel();
		TexturedModel carInteriorModel = car.getCarInteriorModel();
		TexturedModel carTireModel = car.getTireModel();
		
		TexturedModel carModels[] = new TexturedModel[3];
		
		carModels[0] = carModel;
		carModels[1] = carInteriorModel;
		carModels[2] = carTireModel;
		
		List<Car> batch = cars.get(carModels);
		if(batch!=null){
			batch.add(car);
		}
		else{
			List<Car> newBatch = new ArrayList<Car>();
			newBatch.add(car);
			cars.put(carModels, newBatch);
		}
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}
		else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processMTLEntity(Entity entity){
		MTLModel entityModel = (MTLModel) entity.getModel();
		List<Entity> batch = mtlEntities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}
		else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			mtlEntities.put(entityModel, newBatch);
		}
	}
	
	public void processNormalMapEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}
		else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}
	
	public void processRoad(Road road){
		TexturedModel roadModel = road.getModel();
		List<Road> batch = roads.get(roadModel);
		if(batch!=null){
			batch.add(road);
		}
		else{
			List<Road> newBatch = new ArrayList<Road>();
			newBatch.add(road);
			roads.put(roadModel, newBatch);
		}
	}
	
	public void renderShadowMap(List<Entity> entityList, List<Car> carList, List<Line> lineList, Light sun){
		for(Entity entity : entityList){
			processEntity(entity);
		}
		for(Car car : carList){
			processCar(car);
		}		
		for(Line line : lineList){
			processLine(line);
		}
		shadowMapRenderer.render(entities, cars, lines, sun);
		entities.clear();
		mtlEntities.clear();
		cars.clear();
		lines.clear();
	}
	
	public int getShadowMapTexture(){
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		roadShader.cleanUp();
		normalMapRenderer.cleanUp();
		shadowMapRenderer.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
 
	
}
