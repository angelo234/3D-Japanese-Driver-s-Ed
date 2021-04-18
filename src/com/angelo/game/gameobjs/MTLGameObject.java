package com.angelo.game.gameobjs;

import java.util.List;

import com.angelo.game.utils.fileloaders.FileLoader;

public class MTLGameObject extends GameObject{

	protected MTLGameObject(String objName, String objFile){
		super(objName, objFile);
	}
	
	public static MTLGameObject createMTLGameObject(String objPropsFile){
		MTLGameObject mtlGameObject = null;
		
		List<String> file = FileLoader.loadFile(objPropsFile, "[MTL Object Properties File]");
		
		String objName = null;
		String objFile = null;
		
		// Reading file from array
		for (String line : file) {
			if (line.startsWith("objname")) {
				String value = line.split("=")[1];
				objName = value;
			}
			if (line.startsWith("objfile")) {
				String value = line.split("=")[1];
				objFile = value;
			}

		}
		
		mtlGameObject = new MTLGameObject(objName, objFile);
		
		return mtlGameObject;
	}
}
