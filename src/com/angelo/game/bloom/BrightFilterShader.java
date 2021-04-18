package com.angelo.game.bloom;

import com.angelo.game.shaders.ShaderProgram;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/com/angelo/game/bloom/simpleVertex.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/bloom/brightFilterFragment.frag";
	
	public BrightFilterShader() {
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
