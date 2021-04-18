package com.angelo.game.postProcessing;

import com.angelo.game.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/com/angelo/game/postProcessing/contrastVertex.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/postProcessing/contrastFragment.frag";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
