package com.angelo.game.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.angelo.game.objLoader.MTLMeshData;
import com.momchil_atanasov.data.front.parser.MTLColor;

public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	int ambientColorLocation = -1;
	int diffuseColorLocation = -1;
	int specularColorLocation = -1;
	int transmissionColorLocation = -1;
	int specularExponentLocation = -1;
	int dissolveLocation = -1;
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected int getUniformBlockBindingLocation(String uniformBlockname){
		return GL31.glGetUniformBlockIndex(programID, uniformBlockname);
	}
	
	protected String getUniformVariableName(int location){
		return GL20.glGetActiveUniform(programID, location, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
	}
	
	public void start(){
		GL20.glUseProgram(programID);		
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector(int location, Vector2f vector){
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadBoolean(int location, boolean value){
		float bool = 0;
		
		if(value){
			bool = 1;
		}
		GL20.glUniform1f(location, bool);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);	
	}
	
	protected void loadMTLMeshDatas(int locationMTLMeshData, MTLMeshData meshData){
		
		Vector3f ambientColor = convertMTLColorToVector(meshData.getMaterial().getAmbientColor());
		Vector3f diffuseColor = convertMTLColorToVector(meshData.getMaterial().getDiffuseColor());
		Vector3f specularColor = convertMTLColorToVector(meshData.getMaterial().getSpecularColor());
		Vector3f transmissionColor = convertMTLColorToVector(meshData.getMaterial().getTransmissionColor());
		float specularExponent = meshData.getMaterial().getSpecularExponent();
		float dissolve = meshData.getMaterial().getDissolve();

		/*if(ambientColorLocation == -1 || diffuseColorLocation == -1 || specularColorLocation == -1 || transmissionColorLocation == -1 || specularExponentLocation == -1 || dissolveLocation == -1){
			ambientColorLocation = getUniformLocation("mtlMeshData.ambientColor");
			diffuseColorLocation = getUniformLocation("mtlMeshData.diffuseColor");
			specularColorLocation = getUniformLocation("mtlMeshData.specularColor");
			transmissionColorLocation = getUniformLocation("mtlMeshData.transmissionColor");
			specularExponentLocation = getUniformLocation("mtlMeshData.specularExponent");
			dissolveLocation = getUniformLocation("mtlMeshData.dissolve");
		}*/
		
		if(ambientColorLocation == -1 || diffuseColorLocation == -1){
			ambientColorLocation = getUniformLocation("mtlMeshData.ambientColor");
			diffuseColorLocation = getUniformLocation("mtlMeshData.diffuseColor");
		}
		
		this.loadVector(ambientColorLocation, ambientColor);
		this.loadVector(diffuseColorLocation, diffuseColor);
		/*this.loadVector(specularColorLocation, specularColor);
		this.loadVector(transmissionColorLocation, transmissionColor);
		this.loadFloat(specularExponentLocation, specularExponent);
		this.loadFloat(dissolveLocation, dissolve);*/
	}
	
	private Vector3f convertMTLColorToVector(MTLColor color){
		return new Vector3f(color.r, color.g, color.b);
	}
	
	private MTLMeshData[] convertMTLMeshDataListToArray(List<MTLMeshData> meshes){
		MTLMeshData meshesArray[] = new MTLMeshData[meshes.size()];
		
		for(int i = 0; i < meshes.size(); i++){
			meshesArray[i] = meshes.get(i);
		}
		
		return meshesArray;
	}
	
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("\n");
				
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Could not read file");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}
		
}
