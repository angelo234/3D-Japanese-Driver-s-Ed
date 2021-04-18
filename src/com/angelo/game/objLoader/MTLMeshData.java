package com.angelo.game.objLoader;

import java.util.List;

import com.momchil_atanasov.data.front.parser.MTLMaterial;

public class MTLMeshData {

	private MTLMaterial material;
	private List<Integer> vertexIndices;
	
	public MTLMeshData(MTLMaterial material, List<Integer> vertexIndices){
		this.material = material;
		this.vertexIndices = vertexIndices;
	}

	public MTLMaterial getMaterial() {
		return material;
	}

	public List<Integer> getVertexIndices() {
		return vertexIndices;
	}
	
}
