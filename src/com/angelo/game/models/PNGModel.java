package com.angelo.game.models;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.objConverter.OBJFileLoader;
import com.angelo.game.renderEngine.OBJLoader;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.Loader;

public class PNGModel extends TexturedModel{

	private ModelTexture texture;
	
	public PNGModel(String name, RawModel rawModel, ModelTexture texture) {
		super(name, rawModel);
		this.texture = texture;
	}
	
	public PNGModel(String name, RawModel rawModel, ModelTexture texture, Vector3f aabbSize, float mass, float friction, float restitution) {
		super(name, rawModel, aabbSize, mass, friction, restitution);
		this.texture = texture;
	}

	public static PNGModel createPNGModelFromScenario(String objectName, String objFile, String objTextureFile){
		return new PNGModel(objectName, Loader.loadToVAO(OBJFileLoader.loadOBJ(OBJLoader.getObjModelFromScenario(objFile))), new ModelTexture(Loader.loadGameTextureFromObjectFolder(objTextureFile)));
	}
	
	public static PNGModel createPNGModelFromScenario(String objectName, String objFile, String objTextureFile, Vector3f aabbSize, float mass, float friction, float restitution){
		return new PNGModel(objectName, Loader.loadToVAO(OBJFileLoader.loadOBJ(OBJLoader.getObjModelFromScenario(objFile))), new ModelTexture(Loader.loadGameTextureFromObjectFolder(objTextureFile)), aabbSize, mass, friction, restitution);
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
	
}
