package com.angelo.game.objLoader;

import java.util.List;

import com.angelo.game.textures.MTLData;

public class MTLModelData {

	private float[] verticesArray;
	private float[] textureCoordsArray;
	private float[] normalsArray;
	private int[] indicesArray;
	private List<int[]> indicesArrays;
	
	private MTLData mtlData;

	public MTLModelData(float[] verticesArray, float[] textureCoordsArray, float[] normalsArray, int[] indicesArray, List<int[]> indicesArrays, MTLData mtlData){
		this.verticesArray = verticesArray;
		this.textureCoordsArray = textureCoordsArray;
		this.normalsArray = normalsArray;
		this.indicesArray = indicesArray;
		this.indicesArrays = indicesArrays;
		this.mtlData = mtlData;	
	}
	
	public float[] getVertices() {
		return verticesArray;
	}

	public float[] getTextureCoords() {
		return textureCoordsArray;
	}

	public float[] getNormals() {
		return normalsArray;
	}

	public int[] getIndices(){
		return indicesArray;
	}
	
	public List<int[]> getMeshesIndices() {
		return indicesArrays;
	}
	
	public MTLData getMTLData(){
		return mtlData;
	}
	
}
