package com.angelo.game.textures;

import java.util.ArrayList;
import java.util.List;

import com.angelo.game.objLoader.MTLMeshData;
import com.momchil_atanasov.data.front.parser.MTLLibrary;
import com.momchil_atanasov.data.front.parser.MTLMaterial;
import com.momchil_atanasov.data.front.parser.OBJDataReference;
import com.momchil_atanasov.data.front.parser.OBJFace;
import com.momchil_atanasov.data.front.parser.OBJMesh;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJObject;

public class MTLData {

	private OBJModel model;
	private MTLLibrary mtlLib;
	
	private List<MTLMeshData> mtlMeshDatas;
	
	public MTLData(OBJModel model, MTLLibrary mtlLib){
		this.model = model;
		this.mtlLib = mtlLib;
		createMeshDatas();	
	}
	
	private void createMeshDatas(){
		mtlMeshDatas = new ArrayList<MTLMeshData>();
		
		for(OBJObject object : model.getObjects()){
			for(OBJMesh mesh : object.getMeshes()){
				MTLMaterial material = mtlLib.getMaterial(mesh.getMaterialName());
				List<Integer> vertexIndices = new ArrayList<Integer>();
				
				for(OBJFace face : mesh.getFaces()){
					for(OBJDataReference vertex : face.getReferences()){
						vertexIndices.add(vertex.vertexIndex);
					}
				}
				
				mtlMeshDatas.add(new MTLMeshData(material, vertexIndices));		
			}
		}
	}

	public MTLMeshData getMeshFromVertexIndex(int vertexIndex){
		for(MTLMeshData data : this.getMTLMeshDatas()){
			for(int index : data.getVertexIndices()){
				if(vertexIndex == index){
					return data;
				}
			}
		}
		return null;
	}
	
	public OBJModel getModel() {
		return model;
	}

	public MTLLibrary getMTLLibrary() {
		return mtlLib;
	}
	
	public List<MTLMeshData> getMTLMeshDatas(){
		return mtlMeshDatas;
	}
	
}
