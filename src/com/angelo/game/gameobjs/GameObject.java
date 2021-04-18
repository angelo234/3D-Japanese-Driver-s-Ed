package com.angelo.game.gameobjs;

public abstract class GameObject {

	private String objName;
	private String objFile;
	
	protected GameObject(String objName, String objFile){
		this.objName = objName;
		this.objFile = objFile;
	}

	public String getObjName() {
		return objName;
	}

	public String getObjFile() {
		return objFile;
	}	
}
