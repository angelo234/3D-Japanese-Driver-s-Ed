package com.angelo.game.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.entities.Camera;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.shaders.ShaderProgram;
import com.angelo.game.utils.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/com/angelo/game/skybox/skyboxVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/skybox/skyboxFragmentShader.frag";
	
	private static final float ROTATE_SPEED = 0.1f;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int locationFogColor;
	private int locationCubeMap;
	private int locationCubeMap2;
	private int locationBlendFactor;
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * DisplayManager.getDeltaTime();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColor(float r, float g, float b){
		super.loadVector(locationFogColor, new Vector3f(r,g,b));
	}
	
	public void loadBlendFactor(float blend){
		super.loadFloat(locationBlendFactor, blend);
	}
	
	public void connectTextureUnits(){
		super.loadInt(locationCubeMap, 0);
		super.loadInt(locationCubeMap2, 1);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		locationFogColor = super.getUniformLocation("fogColor");
		locationCubeMap = super.getUniformLocation("cubeMap");
		locationCubeMap2 = super.getUniformLocation("cubeMap2");
		locationBlendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
