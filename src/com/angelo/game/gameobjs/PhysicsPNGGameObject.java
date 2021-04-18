package com.angelo.game.gameobjs;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.utils.fileloaders.FileLoader;

public class PhysicsPNGGameObject extends PhysicsGameObject{

	private String objTexture;
	private int rows;
	
	protected PhysicsPNGGameObject(String objName, String objFile, String objTexture, Vector3f aabbSize, float mass, float friction, float restitution){
		super(objName, objFile, aabbSize, mass, friction, restitution);
		this.objTexture = objTexture;
	}
	
	public static PhysicsPNGGameObject createGameObject(String objPropsFile){
		PhysicsPNGGameObject physicsGameObject = null;
		
		List<String> file = FileLoader.loadFile(objPropsFile, "[Physics PNG Object Properties File]");
		
		String objName = null;
		String objFile = null;
		String objTexture = null;
		Vector3f aabbsize = null;
		float mass = 0;
		float friction = 0;
		float restitution = 0;
		
		//Reading file from array
		for (String line : file) {

			if (line.startsWith("objname")) {
				String value = line.split("=")[1];
				objName = value;
			}
			if (line.startsWith("objfile")) {
				String value = line.split("=")[1];
				objFile = value;
			}
			if (line.startsWith("pngfile")) {
				String value = line.split("=")[1];
				objTexture = value;
			}
			if (line.startsWith("aabbsize")) {
				String value = line.split("=")[1].replace("(", "").replace(")", "").replaceAll("\\s", "");
				String vector3f[] = value.split(",");

				aabbsize = new Vector3f(Float.parseFloat(vector3f[0]), Float.parseFloat(vector3f[1]), Float.parseFloat(vector3f[2]));
			}
			if (line.startsWith("mass")) {
				String value = line.split("=")[1];
				mass = Float.parseFloat(value);
			}
			if (line.startsWith("friction")) {
				String value = line.split("=")[1];
				friction = Float.parseFloat(value);
			}
			if (line.startsWith("restitution")) {
				String value = line.split("=")[1];
				restitution = Float.parseFloat(value);
			}

		}
		
		physicsGameObject = new PhysicsPNGGameObject(objName, objFile, objTexture, aabbsize, mass, friction, restitution);
		
		return physicsGameObject;
	}
	
	public String getObjTexture() {
		return objTexture;
	}
	
	public int getRows(){
		return rows;
	}
}
