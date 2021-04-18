package com.angelo.game.objLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.angelo.game.textures.MTLData;
import com.momchil_atanasov.data.front.error.WFException;
import com.momchil_atanasov.data.front.parser.IOBJParser;
import com.momchil_atanasov.data.front.parser.MTLLibrary;
import com.momchil_atanasov.data.front.parser.MTLParser;
import com.momchil_atanasov.data.front.parser.OBJDataReference;
import com.momchil_atanasov.data.front.parser.OBJFace;
import com.momchil_atanasov.data.front.parser.OBJMesh;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJNormal;
import com.momchil_atanasov.data.front.parser.OBJObject;
import com.momchil_atanasov.data.front.parser.OBJParser;
import com.momchil_atanasov.data.front.parser.OBJTexCoord;
import com.momchil_atanasov.data.front.parser.OBJVertex;

public class OBJMTLLoader {

	/**Loads OBJ file and associated MTL file */
	public static MTLModelData loadOBJ(String objFile){	
		OBJModel model = null;
		MTLLibrary mtlLibrary = null;
		
		try {
			Object objMtl[] = parseOBJFile(objFile);
			model = (OBJModel) objMtl[0];
			mtlLibrary = (MTLLibrary) objMtl[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		float[] verticesArray = verticesToArray(model.getVertices());
		float[] textureCoordsArray = textureCoordsToArray(model.getTexCoords());
		float[] normalsArray = normalsToArray(model.getNormals());
		int[] indicesArray = indicesToArray(model);
		List<int[]> meshIndicesArray = meshesIndicesToArray(model);
			
		MTLModelData data = new MTLModelData(verticesArray, textureCoordsArray, normalsArray, indicesArray, meshIndicesArray, new MTLData(model, mtlLibrary));
		
		return data;
	}

	private static float[] verticesToArray(List<OBJVertex> vertices){
		float verticesArray[] = new float[vertices.size() * 3];
		
		for(int i = 0; i < vertices.size(); i++) {
			OBJVertex vertex = vertices.get(i);

			verticesArray[i * 3] = vertex.x;
			verticesArray[i * 3 + 1] = vertex.y;
			verticesArray[i * 3 + 2] = vertex.z;
		}
		
		return verticesArray;
	}
	
	private static float[] normalsToArray(List<OBJNormal> normals){
		float normalsArray[] = new float[normals.size() * 3];
		
		for(int i = 0; i < normals.size(); i++){
			OBJNormal normal = normals.get(i);
			
			normalsArray[i * 3] = normal.x;
			normalsArray[i * 3 + 1] = normal.y;
			normalsArray[i * 3 + 2] = normal.z;
		}
		
		return normalsArray;
	}
	
	private static float[] textureCoordsToArray(List<OBJTexCoord> texCoords){
		float textureCoordsArray[] = null;
				
		if(texCoords.size() != 0){
			textureCoordsArray = new float[texCoords.size() * 3];
			
			for(int i = 0; i < texCoords.size(); i++){
				OBJTexCoord texCoord = texCoords.get(i);
				
				textureCoordsArray[i * 3] = texCoord.u;
				textureCoordsArray[i * 3 + 1] = texCoord.v;
				textureCoordsArray[i * 3 + 2] = texCoord.w;
			}
		}
				
		return textureCoordsArray;
	}
	
	private static int[] indicesToArray(OBJModel model){	
		List<Integer> indices = new ArrayList<Integer>();

		for(OBJObject object : model.getObjects()){
			for(OBJMesh mesh : object.getMeshes()){		
				for (OBJFace face : mesh.getFaces()) {	
					// Triangle Face
					if (face.getReferences().size() == 3) {
						for (OBJDataReference ref : face.getReferences()) {
							if (ref.hasVertexIndex()) {
								indices.add(ref.vertexIndex);							
							}
						}
					}
					// Quad Face (needs to be converted into a triangle face)
					else if (face.getReferences().size() == 4) {
						int quadFaceIndices[] = new int[4];
						int i = 0;

						for (OBJDataReference ref : face.getReferences()) {
							if (ref.hasVertexIndex()) {
								quadFaceIndices[i] = ref.vertexIndex;
							}
							i++;
						}

						indices.add(quadFaceIndices[0]);
						indices.add(quadFaceIndices[1]);
						indices.add(quadFaceIndices[2]);
						indices.add(quadFaceIndices[0]);
						indices.add(quadFaceIndices[2]);
						indices.add(quadFaceIndices[3]);
					}
				}			
			}		
		}
		
		int indicesArray[] = new int[indices.size()];
		
		for(int i = 0; i < indices.size(); i++){
			indicesArray[i] = indices.get(i);
		}	
		
		return indicesArray;
	}	
	
	private static List<int[]> meshesIndicesToArray(OBJModel model){	
		List<int[]> meshesIndices = new ArrayList<int[]>();

		for(OBJObject object : model.getObjects()){
			for(OBJMesh mesh : object.getMeshes()){		
				List<Integer> indices = new ArrayList<Integer>();
				for (OBJFace face : mesh.getFaces()) {	
					// Triangle Face
					if (face.getReferences().size() == 3) {
						for (OBJDataReference ref : face.getReferences()) {
							if (ref.hasVertexIndex()) {
								indices.add(ref.vertexIndex);
								//System.out.println(ref.vertexIndex - startIndex);
							}
						}
					}
					// Quad Face (needs to be converted into a triangle face)
					else if (face.getReferences().size() == 4) {
						int quadFaceIndices[] = new int[4];
						int i = 0;
						
						for (OBJDataReference ref : face.getReferences()) {
							
							if (ref.hasVertexIndex()) {
								quadFaceIndices[i] = ref.vertexIndex;
							}
							i++;
						}
						
						indices.add(quadFaceIndices[0]);
						indices.add(quadFaceIndices[1]);
						indices.add(quadFaceIndices[2]);
						indices.add(quadFaceIndices[0]);
						indices.add(quadFaceIndices[2]);
						indices.add(quadFaceIndices[3]);
					}
				}
				
				int indicesArray[] = new int[indices.size()];
				
				for(int i = 0; i < indices.size(); i++){
					indicesArray[i] = indices.get(i);
				}
				
				meshesIndices.add(indicesArray);
			}		
		}
		
		return meshesIndices;
	}	
	
	private static Object[] parseOBJFile(String objFile) throws WFException, IOException {
		Object objMtl[] = new Object[2];
		
		OBJModel model = new OBJModel();
		MTLLibrary library = new MTLLibrary();
		
		InputStream in = null;
		InputStream mtlStream = null;
		try {
			in = new FileInputStream(objFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// Create an OBJParser and parse the resource
		final IOBJParser objParser = new OBJParser();
		model = objParser.parse(in);
		
		MTLParser mtlParser = new MTLParser();
		
		for (String libraryReference : model.getMaterialLibraries()) {
			String dir = getMTLDirectory(objFile);
			mtlStream = new FileInputStream(dir+libraryReference); // You will need to resolve this based on `libraryReference`
		    library = mtlParser.parse(mtlStream);
		    // Do something with the library. Maybe store it for later usage.   
		} 
		
		objMtl[0] = model;
		objMtl[1] = library;
		
		in.close();
		mtlStream.close();
		
		return objMtl;
	}
	
	private static String getMTLDirectory(String objFile){
		int index = objFile.lastIndexOf("/");
		
		final StringBuilder builder = new StringBuilder(objFile);
		
		builder.delete(index + 1, objFile.length());

		return builder.toString();
	}
	
}
