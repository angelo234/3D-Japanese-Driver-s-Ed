package com.angelo.game.guis;

import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.shaders.ShaderProgram;

public class GUIShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/com/angelo/game/guis/GuiVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/guis/GuiFragmentShader.frag";

	private int locationTransformationMatrix;
	
	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}

}
