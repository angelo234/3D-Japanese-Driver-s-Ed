package com.angelo.game.water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Light;
import com.angelo.game.shaders.ShaderProgram;
import com.angelo.game.utils.Maths;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/angelo/game/water/waterVertex.vert";
	private final static String FRAGMENT_FILE = "src/com/angelo/game/water/waterFragment.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationDudvMap;
	private int locationMoveFactor;
	private int locationCameraPosition;
	private int locationNormalMap;
	private int locationLightColor;
	private int locationLightPosition;	
	private int locationDepthMap;
	private int locationSkyColor;
	
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		locationReflectionTexture = getUniformLocation("reflectionTexture");
		locationRefractionTexture = getUniformLocation("refractionTexture");
		locationDudvMap = getUniformLocation("dudvMap");
		locationMoveFactor = getUniformLocation("moveFactor");
		locationCameraPosition = getUniformLocation("cameraPosition");
		locationNormalMap = getUniformLocation("normalMap");
		locationLightColor = getUniformLocation("lightColor");
		locationLightPosition = getUniformLocation("lightPositon");	
		locationDepthMap = getUniformLocation("depthMap");
		locationSkyColor = getUniformLocation("skyColor");
	}
	
	public void connectTextureUnits(){
		super.loadInt(locationReflectionTexture, 0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDudvMap, 2);
		super.loadInt(locationNormalMap, 3);
		super.loadInt(locationDepthMap, 4);
	}
	
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(locationSkyColor, new Vector3f(r,g,b));
	}
	
	public void loadLight(Light sun){
		super.loadVector(locationLightColor, sun.getColor());
		super.loadVector(locationLightPosition, sun.getPosition());
	}
	
	public void loadMoveFactor(float factor){
		super.loadFloat(locationMoveFactor, factor);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(locationCameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
