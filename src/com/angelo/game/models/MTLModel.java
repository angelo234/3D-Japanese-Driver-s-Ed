package com.angelo.game.models;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.objLoader.MTLModelData;
import com.angelo.game.objLoader.OBJMTLLoader;
import com.angelo.game.renderEngine.OBJLoader;
import com.angelo.game.textures.MTLData;
import com.angelo.game.utils.Loader;

public class MTLModel extends TexturedModel{

	private MTLData mtlData;
	
	public MTLModel(String modelName, RawModel rawModel, MTLData mtlData){
		super(modelName, rawModel);
		this.mtlData = mtlData;
	}
	
	public MTLModel(String modelName, RawModel rawModel, MTLData mtlData, Vector3f aabbSize, float mass, float friction, float restitution){
		super(modelName, rawModel, aabbSize, mass, friction, restitution);
		this.mtlData = mtlData;
	}
	
	public static MTLModel createMTLModelFromScenario(String objName, String objFile) {
		MTLModelData modelData = OBJMTLLoader.loadOBJ(OBJLoader.getObjModelFromScenario(objFile));	
		RawModel rawModel = Loader.loadToVAO(modelData);
		
		return new MTLModel(objName, rawModel, modelData.getMTLData());
	}

	public static MTLModel createMTLModelFromScenario(String objName, String objFile, Vector3f aabbSize, float mass, float friction, float restitution) {
		MTLModelData modelData = OBJMTLLoader.loadOBJ(OBJLoader.getObjModelFromScenario(objFile));	
		RawModel rawModel = Loader.loadToVAO(modelData);
		
		return new MTLModel(objName, rawModel, modelData.getMTLData(), aabbSize, mass, friction, restitution);
	}	
	
	public MTLData getMTLData() {
		return mtlData;
	}
}
