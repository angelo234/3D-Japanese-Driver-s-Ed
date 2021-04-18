package com.angelo.game.gameobjs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class RoadObject extends PhysicsPNGGameObject{

	public RoadObject(String objName, String objFile, String objTexture, Vector3f aabbsize) {
		super(objName, objFile, objTexture, aabbsize, 0, 1, 0);
	}

	public static RoadObject createRoadObject(String objPropsFile){
		RoadObject roadObject = null;
		FileReader fr = null;
			
		List<String> file = new ArrayList<String>();
		
		try {
			fr = new FileReader(new File(objPropsFile));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		//Reading Objects.txt and storing file in an array
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				file.add(line); 
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		//End of Reading Objects.txt
		
		boolean isObjectPropFile = false;
		
		String objName = null;
		String objFile = null;
		String objTexture = null;
		Vector3f aabbsize = null;
		
		//Reading file from array
		for(int i = 0;i<file.size();i++){
			String line = file.get(i);
			
			if(line.startsWith("[Road Properties File]")){
				isObjectPropFile = true;
			}
			
			if(isObjectPropFile){
				
				if(line.startsWith("objname")){
					String value = line.split("=")[1];
					objName = value;
				}
				if(line.startsWith("objfile")){
					String value = line.split("=")[1];					
					objFile = value;
				}
				if(line.startsWith("pngfile")){
					String value = line.split("=")[1];				
					objTexture = value;
				}
				if(line.startsWith("aabbsize")){
					String value = line.split("=")[1].replace("(", "").replace(")", "").replaceAll("\\s", "");				
					String vector3f[] = value.split(",");
					
					aabbsize = new Vector3f(Float.parseFloat(vector3f[0]),Float.parseFloat(vector3f[1]),Float.parseFloat(vector3f[2]));
				}
			}
		}
		
		roadObject = new RoadObject(objName, objFile, objTexture, aabbsize);
		
		return roadObject;
	}
}
