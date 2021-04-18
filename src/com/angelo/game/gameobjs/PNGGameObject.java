package com.angelo.game.gameobjs;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.utils.fileloaders.FileLoader;

public class PNGGameObject extends GameObject{

	private String objTexture;
	private int rows;
	
	protected PNGGameObject(String objName, String objFile, String objTexture){
		super(objName, objFile);
		this.objTexture = objTexture;
	}
	
	protected PNGGameObject(String objName, String objFile, String objTexture, int rows){
		super(objName, objFile);
		this.objTexture = objTexture;
		this.rows = rows;
	}
	
	public static PNGGameObject createGameObject(String objPropsFile){
		PNGGameObject pngGameObject = null;
		
		List<String> file = FileLoader.loadFile(objPropsFile, "[PNG Object Properties File]");
		
		String objName = null;
		String objFile = null;
		String objTexture = null;
		
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
			
		}
		
		pngGameObject = new PNGGameObject(objName, objFile, objTexture);
		
		return pngGameObject;
	}
	
	public String getObjTexture() {
		return objTexture;
	}
	
	public int getRows(){
		return rows;
	}
}
