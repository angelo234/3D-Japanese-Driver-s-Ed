package com.angelo.game.particles;

import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/angelo/game/particles/particleVShader.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/particles/particleFShader.frag";

	private int locationNumberOfRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		locationNumberOfRows = super.getUniformLocation("numberOfRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumberOfRows(float numberOfRows){
		super.loadFloat(locationNumberOfRows, numberOfRows);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
