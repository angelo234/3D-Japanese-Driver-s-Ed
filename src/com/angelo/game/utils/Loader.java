package com.angelo.game.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.angelo.game.line.LineData;
import com.angelo.game.line.LineModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.objConverter.ModelData;
import com.angelo.game.objLoader.MTLModelData;
import com.angelo.game.renderEngine.VAO;
import com.angelo.game.textures.TextureData;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {

	public static Map<Integer, VAO> vaos = new HashMap<Integer, VAO>();
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static RawModel loadToVAO(float[] positions, int[] indices) {
		VAO vao = createVAO();
		vao.setVerticesVBO(storeDataInAttributeList(0, 3, positions));
		vaos.put(vao.getID(), vao);	
		unbindVAO();
		return new RawModel(vao.getID(), indices.length);
	}
	
	public static RawModel loadToVAO(ModelData data) {	
		VAO vao = createVAO();
		vao.setTriangleIndicesVBO(bindIndicesBuffer(data.getIndices()));
		vao.setVerticesVBO(storeDataInAttributeList(0, 3, data.getVertices()));
		vao.setUVSVBO(storeDataInAttributeList(1, 2, data.getTextureCoords()));
		vao.setNormalsVBO(storeDataInAttributeList(2, 3, data.getNormals()));	
		vaos.put(vao.getID(), vao);	
		unbindVAO();	
		return new RawModel(vao.getID(), data.getIndices().length);
	}
	
	public static RawModel loadToVAO(MTLModelData data) {				
		float vertices[] = data.getVertices();
		float textureCoords[] = data.getTextureCoords();
		float normals[] = data.getNormals();
		int indices[] = data.getIndices();
		List<int[]> meshesIndices = data.getMeshesIndices();

		VAO vao1 = createVAO();
		vao1.setTriangleIndicesVBO(bindIndicesBuffer(indices));
		vao1.setVerticesVBO(storeDataInAttributeList(0, 3, vertices));
		if(textureCoords != null){
			vao1.setUVSVBO(storeDataInAttributeList(1, 3, textureCoords));
		}	
		vao1.setNormalsVBO(storeDataInAttributeList(2, 3, normals));
		vaos.put(vao1.getID(), vao1);
		unbindVAO();		
		
		int vaoIDS[] = new int[meshesIndices.size()];
		
		for(int i = 0; i < meshesIndices.size(); i++){
			VAO vao = createVAO();
			vao.setTriangleIndicesVBO(bindIndicesBuffer(meshesIndices.get(i)));
			vao.setVerticesVBO(storeDataInAttributeList(0, 3, vertices));
			if(textureCoords != null){
				vao.setUVSVBO(storeDataInAttributeList(1, 3, textureCoords));
			}	
			vao.setNormalsVBO(storeDataInAttributeList(2, 3, normals));
			vaos.put(vao.getID(), vao);
			vaoIDS[i] = vao.getID();
			unbindVAO();		
		}
		
		return new RawModel(vao1.getID(), vaoIDS, vertices, textureCoords, normals, indices, meshesIndices);
	}
	
	public static LineModel loadToVAO(LineData data) {	
		VAO vao = createVAO();
		vao.setTriangleIndicesVBO(bindIndicesBuffer(data.getIndices()));
		vao.setVerticesVBO(storeDataInAttributeList(0, 3, data.getVertices()));
		vao.setNormalsVBO(storeDataInAttributeList(1, 3, data.getNormals()));	
		vaos.put(vao.getID(), vao);	
		unbindVAO();	
		return new LineModel(vao.getID(), data.getIndices().length);
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		VAO vao = createVAO();
		vao.setTriangleIndicesVBO(bindIndicesBuffer(indices));
		vao.setVerticesVBO(storeDataInAttributeList(0, 3, positions));
		vao.setUVSVBO(storeDataInAttributeList(1, 2, textureCoords));
		vao.setNormalsVBO(storeDataInAttributeList(2, 3, normals));	
		vaos.put(vao.getID(), vao);	
		unbindVAO();
		return new RawModel(vao.getID(), indices.length);
	}
	
	public static RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, List<int[]> indicesArrays) {
		
		return null;
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		VAO vao = createVAO();
		vao.setTriangleIndicesVBO(bindIndicesBuffer(indices));
		vao.setVerticesVBO(storeDataInAttributeList(0, 3, positions));
		vao.setUVSVBO(storeDataInAttributeList(1, 2, textureCoords));
		vao.setNormalsVBO(storeDataInAttributeList(2, 3, normals));	
		vao.setTangentsVBO(storeDataInAttributeList(3, 3, tangents));
		vaos.put(vao.getID(), vao);			
		unbindVAO();
		
		return new RawModel(vao.getID(), indices.length);
	}
	
	public static RawModel loadToVAO(float[] positions, int dimensions){
		VAO vao = createVAO();
		vao.setVerticesVBO(storeDataInAttributeList(0, dimensions, positions));
		vaos.put(vao.getID(), vao);
		unbindVAO();	
		
		return new RawModel(vao.getID(), positions.length/dimensions);
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, int dimensions){
		VAO vao = createVAO();
		vao.setVerticesVBO(storeDataInAttributeList(0, dimensions, positions));
		vao.setUVSVBO(storeDataInAttributeList(1, 2, textureCoords));
		vaos.put(vao.getID(), vao);
		unbindVAO();	
		
		return new RawModel(vao.getID(), positions.length/dimensions);
	}
	
	public static VAO loadToVAO(float[] positions, float[] textureCoords){
		VAO vao = createVAO();
		vao.setVerticesVBO(storeDataInAttributeList(0, 2, positions));
		vao.setUVSVBO(storeDataInAttributeList(1, 2, textureCoords));
		vaos.put(vao.getID(), vao);
		unbindVAO();	
		
		return vao;
	}
	
	public static void deleteVAO(int vaoID){	
		VAO vao = vaos.remove(vaoID); 
		if(vao != null){
			for (int vbo : vao.getVBOS()) {
				GL15.glDeleteBuffers(vbo);
			}		
		}	
		GL30.glDeleteVertexArrays(vaoID);
	}
	
	//Not being used currently
	public static int createEmptyVbo(int floatCount){
		int vbo = GL15.glGenBuffers();
		//vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vbo;
	}
	
	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public static void updateVBO(int vboID, float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);	
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	//Not being used currently
	public static void updateVbo(int vboID, float[] data, FloatBuffer buffer){
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static int loadCubeMap(String [] textureFiles){
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
	
		for(int i=0;i<textureFiles.length;i++){
			TextureData data = decodeTextureFile("res/"+textureFiles[i]+".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		
		return texID;
	}
	
	public static int loadGameTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic){
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
			
			}else{
				System.out.println("Not Supported");
			}
		
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	public static int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(fileName));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic){
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
			
			}else{
				System.out.println("Not Supported");
			}
		
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	public static int loadGameTextureFromObjectFolder(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(ScenarioLoader.OBJECT_FOLDER+fileName));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic){
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
			
			}else{
				System.out.println("Not Supported");
			}
		
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	public static int loadFontTextureAtlas(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	private static VAO createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);	
		return new VAO(vaoID);
	}

	public static void cleanUp(){
		for(Entry<Integer, VAO> entry : vaos.entrySet()){
			GL30.glDeleteVertexArrays(entry.getKey());		
			for(int vbo : entry.getValue().getVBOS()){
				GL15.glDeleteBuffers(vbo);
			}		
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private static int storeDataInAttributeList(int attributeNum, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(attributeNum, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private static int bindIndicesBuffer(int[] triangleIndices){
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(triangleIndices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);	
		return vboID;
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;

	}
	
}
