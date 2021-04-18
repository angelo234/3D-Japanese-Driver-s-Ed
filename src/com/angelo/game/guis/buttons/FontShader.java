package com.angelo.game.guis.buttons;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/com/angelo/game/guis/buttons/fontVertex.vert";
	private static final String FRAGMENT_FILE = "src/com/angelo/game/guis/buttons/fontFragment.frag";
	
	private int locationColor;
	private int locationTranslation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		locationColor = super.getUniformLocation("color");
		locationTranslation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColor(Vector3f color){
		super.loadVector(locationColor, color);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector(locationTranslation, translation);
	}
}
