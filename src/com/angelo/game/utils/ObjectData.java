package com.angelo.game.utils;

import org.lwjgl.util.vector.Vector3f;

public class ObjectData {

	private String objName;
	private int index = -1;

	private float rotX;
	private float rotY;
	private float rotZ;
	private float scale;
	private Vector3f position;
	
	private float reflectivity;
	private float shineDamper;
	private boolean isTransparent;
	private boolean usingFakeLighting;
	
	public ObjectData(String objName, Vector3f position, float rotX, float rotY, float rotZ, float scale){
		this.objName = objName;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public ObjectData(String objName, Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean isTransparent, boolean usingFakeLighting){
		this.objName = objName;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.isTransparent = isTransparent;
		this.usingFakeLighting = usingFakeLighting;
	}
	
	public ObjectData(String objName, Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean isTransparent, boolean usingFakeLighting, float reflectivity, float shineDamper){
		this.objName = objName;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.isTransparent = isTransparent;
		this.usingFakeLighting = usingFakeLighting;
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
	}
	
	public ObjectData(String objName, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale){
		this.objName = objName;
		this.index = index;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public ObjectData(String objName, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean isTransparent, boolean usingFakeLighting){
		this.objName = objName;
		this.index = index;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.isTransparent = isTransparent;
		this.usingFakeLighting = usingFakeLighting;
	}
	
	public ObjectData(String objName, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean isTransparent, boolean usingFakeLighting, float reflectivity, float shineDamper){
		this.objName = objName;
		this.index = index;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.isTransparent = isTransparent;
		this.usingFakeLighting = usingFakeLighting;
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
	}
	
	/*public static ObjectData getObject(Vector3f position, float rotX, float rotY, float rotZ, float scale){
		
		for(ObjectData objData : GlobalVariables.objDatas){
			if(objData.getPosition().x == position.x && objData.getPosition().y == position.y && objData.getPosition().z == position.z && objData.rotX == rotX && objData.rotY == rotY && objData.rotZ == rotZ && objData.scale == scale){
				return objData;
			}
		}
		
		return null;			
	}

	public static ObjectData getObject(String name){
		for(ObjectData objData : GlobalVariables.objDatas){
			String objName = objData.getObjName();
			if(name.matches(objName)){
				return objData;
			}
		}
		
		return null;
	}*/
	

	public String getObjName() {
		return objName;
	}

	public int getIndex(){
		return index;
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	public float getRotX() {
		return rotX;
	}


	public float getRotY() {
		return rotY;
	}


	public float getRotZ() {
		return rotZ;
	}
	
	public float getScale(){
		return scale;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isUsingFakeLighting() {
		return usingFakeLighting;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public float getShineDamper() {
		return shineDamper;
	}
	
}
