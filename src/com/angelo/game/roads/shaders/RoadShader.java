package com.angelo.game.roads.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Light;
import com.angelo.game.shaders.ShaderProgram;
import com.angelo.game.utils.Maths;

public class RoadShader extends ShaderProgram{

	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/com/angelo/game/roads/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/roads/shaders/fragmentShader.frag";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition[];
	private int locationLightColor[];
	private int locationAttenuation[];
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationUseFakeLighting;
	private int locationSkyColor;
	private int locationPlane;
	private int locationSpecularMap;
	private int locationUsesSpecularMap;
	private int locationModelTexture;
	
	
	public RoadShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		locationSpecularMap = super.getUniformLocation("specularMap");
		locationUsesSpecularMap = super.getUniformLocation("usesSpecularMap");
		locationModelTexture = super.getUniformLocation("modelTexture");
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationUseFakeLighting = super.getUniformLocation("useFakeLighting");
		locationSkyColor = super.getUniformLocation("skyColor");
		locationPlane = super.getUniformLocation("plane");
		
		
		locationLightPosition = new int[MAX_LIGHTS];
		locationLightColor = new int[MAX_LIGHTS];
		locationAttenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			locationLightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			locationLightColor[i] = super.getUniformLocation("lightColor["+i+"]");
			locationAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
	}
	
	public void connectTextureUnits() {
		super.loadInt(locationModelTexture, 0);
		super.loadInt(locationSpecularMap, 1);
	}
	
	public void loadUseSpecularMap(boolean useMap){
		super.loadBoolean(locationUsesSpecularMap, useMap);
	}
	
	public void loadClipPlane(Vector4f plane){
		super.loadVector(locationPlane, plane);
	}
	
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(locationSkyColor, new Vector3f(r,g,b));
	}
	
	public void loadFakeLightingVariable(boolean useFakeLighting){
		super.loadBoolean(locationUseFakeLighting, useFakeLighting);
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColor[i], lights.get(i).getColor());
				super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
			}
			else{
				super.loadVector(locationLightPosition[i], new Vector3f(0,0,0));
				super.loadVector(locationLightColor[i], new Vector3f(0,0,0));
				super.loadVector(locationAttenuation[i], new Vector3f(1,0,0));
			}
		}
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(locationProjectionMatrix, projection);
	}
	
}
