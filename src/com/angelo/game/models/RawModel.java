package com.angelo.game.models;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.angelo.game.renderEngine.VAO;
import com.angelo.game.utils.Loader;

public class RawModel {

	private int vaoID;
	private int vertexCount;

	private int[] vaoIDs;
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private List<int[]> meshesIndices;
	
	private ByteBuffer indicesBuffer;
	private List<ByteBuffer> meshesIndicesBuffer;
	
	private IntBuffer indicesIntBuffer;
	private List<IntBuffer> meshesIndicesIntBuffer;
	
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public RawModel(int vaoID, int[] vaoIDs, float[] vertices, float[] textureCoords, float[] normals, int[] indices, List<int[]> meshesIndices) {
		this.vaoID = vaoID;
		this.vaoIDs = vaoIDs;
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.meshesIndices = meshesIndices;
		
		loadIndicesIntoBuffers();
	}
	
	private void loadIndicesIntoBuffers(){	
		indicesBuffer = BufferUtils.createByteBuffer(indices.length * 4);			
		for(int index : indices){
			indicesBuffer.putInt(index);
		}				
		indicesBuffer.flip();
		
		meshesIndicesBuffer = new ArrayList<ByteBuffer>();
		
		for(int i = 0; i<meshesIndices.size(); i++){
			int[] meshIndices = meshesIndices.get(i);
			ByteBuffer buffer = BufferUtils.createByteBuffer(meshIndices.length * 4);

			for(int index : meshIndices){
				buffer.putInt(index);
			}				
			buffer.flip();
			
			meshesIndicesBuffer.add(buffer);
		}
		
		indicesIntBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesIntBuffer.put(indices);
		indicesIntBuffer.flip();
		
		meshesIndicesIntBuffer = new ArrayList<IntBuffer>();
		
		for(int i = 0; i<meshesIndices.size(); i++){
			int[] meshIndices = meshesIndices.get(i);
			IntBuffer buffer = BufferUtils.createIntBuffer(meshIndices.length * 4);

			buffer.put(meshIndices);			
			buffer.flip();
			
			meshesIndicesIntBuffer.add(buffer);
		}
	}

	public void setVertexPositions(float data[]){
		VAO vao = Loader.vaos.get(vaoID);		
		int vboVerticesID = vao.getVerticesVBOID();	
	
		Loader.updateVBO(vboVerticesID, data);
	}
	
	public int getVAOID() {
		return vaoID;
	}
	
	public int getVAOIDFromArray(int index){
		return vaoIDs[index];
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public List<int[]> getMeshesIndices(){
		return meshesIndices;
	}

	public ByteBuffer getIndicesBuffer() {
		return indicesBuffer;
	}

	public List<ByteBuffer> getMeshesIndicesBuffer() {
		return meshesIndicesBuffer;
	}
	
	public IntBuffer getIndicesIntBuffer() {
		return indicesIntBuffer;
	}

	public List<IntBuffer> getMeshesIndicesIntBuffer() {
		return meshesIndicesIntBuffer;
	}
	
}
