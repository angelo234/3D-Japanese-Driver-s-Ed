package com.angelo.game.gameobjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.physicsEngine.DimensionsAndMass;
import com.angelo.game.physicsEngine.carComponents.EngineTransmission;
import com.angelo.game.utils.CarProperties;
import com.angelo.game.utils.fileloaders.FileLoader;

public class CarObject extends PhysicsGameObject {
	
	private String objTexture;
	private String interiorObjFile;
	private String interiorObjTexture;
	private String tireObjFile;
	private String tireObjTexture;
	private boolean usingMTLTexture;
	private boolean usingMTLInteriorTexture;
	private boolean usingMTLTireTexture;
	private CarProperties props;
	
	protected CarObject(String objName, String objFile, String objTexture, String interiorObjFile, String interiorObjTexture, String tireObjFile, String tireObjTexture, Vector3f aabbSize, CarProperties props){
		super(objName, objFile, aabbSize, props.getMass(), 0, 0);
		this.interiorObjFile = interiorObjFile;
		this.tireObjFile = tireObjFile;
		this.tireObjTexture = tireObjTexture;
		this.props = props;
		
		System.out.println(objTexture);
		//System.out.println(interiorObjTexture);
		if(objTexture == null){
			usingMTLTexture = true;
		}
		else{
			this.objTexture = objTexture;
		}
		if(interiorObjTexture == null){
			usingMTLInteriorTexture = true;
		}
		else{
			this.interiorObjTexture = interiorObjTexture;
		}
		if(tireObjTexture == null){
			usingMTLTireTexture = true;
		}
		else{
			this.tireObjTexture = tireObjTexture;
		}
	}
	
	public static CarObject createCarObject(String objPropsFile){
		CarObject carObject = null;
		
		List<String> file = FileLoader.loadFile(objPropsFile, "[Car Object Properties File]");
		
		String objName = null;
		String objFile = null;
		String objTexture = null;
		String interiorObjFile = null;
		String interiorObjTexture = null;
		String tireObjFile = null;
		String tireObjTexture = null;
		Vector3f aabbsize = null;
		
		float gear1 = 0;
		float gear2 = 0;
		float gear3 = 0;
		float gear4 = 0;
		float gear5 = 0;
		float gearR = 0;
		float gearDiff = 0;
		
		Map<Float, Float> nmatrpm = new HashMap<Float, Float>(3);
		
		CarProperties props = new CarProperties();
		DimensionsAndMass dimsMass = new DimensionsAndMass();		
		
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
			if (line.startsWith("interiorobjfile")){
				String value = line.split("=")[1];
				interiorObjFile = value;
			}
			if (line.startsWith("interiorpngfile")){
				String value = line.split("=")[1];
				interiorObjTexture = value;
			}
			if (line.startsWith("tireobjfile")){
				String value = line.split("=")[1];
				tireObjFile = value;
			}
			if (line.startsWith("tirepngfile")){
				String value = line.split("=")[1];
				tireObjTexture = value;
			}
			if (line.startsWith("aabbsize")) {
				String value = line.split("=")[1].replace("(", "").replace(")", "").replaceAll("\\s", "");
				String vector3f[] = value.split(",");

				aabbsize = new Vector3f(Float.parseFloat(vector3f[0]), Float.parseFloat(vector3f[1]), Float.parseFloat(vector3f[2]));
			}

			if (line.startsWith("dimensions")) {
				String line2 = line.split("=")[1].replace("(", "").replace(")", "").replaceAll("\\s", "");

				float length = Float.parseFloat(line2.split(",")[0]);
				float width = Float.parseFloat(line2.split(",")[1]);
				float height = Float.parseFloat(line2.split(",")[2]);

				dimsMass.setLength(length);
				dimsMass.setWidth(width);
				dimsMass.setHeight(height);

			}
			if (line.startsWith("mass")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float mass = Float.parseFloat(line2);
				props.setMass(mass);
			}
			if(line.startsWith("wheelradius")){
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float wheelRadius = Float.parseFloat(line2);
				
				props.setWheelRadius(wheelRadius);
			}
			if (line.startsWith("gearratio1")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gear1 = Float.parseFloat(line2);
			}
			if (line.startsWith("gearratio2")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gear2 = Float.parseFloat(line2);
			}
			if (line.startsWith("gearratio3")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gear3 = Float.parseFloat(line2);
			}
			if (line.startsWith("gearratio4")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gear4 = Float.parseFloat(line2);
			}
			if (line.startsWith("gearratio5")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gear5 = Float.parseFloat(line2);
			}
			if (line.startsWith("gearratior")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gearR = Float.parseFloat(line2);
			}
			if (line.startsWith("diffratio")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				gearDiff = Float.parseFloat(line2);
			}
			if(line.startsWith("nm@")){
				String nm = line.split("=")[1].replaceAll("\\s", "");
				
				String rpm = line.split("@")[1].split("rpm")[0].replaceAll("\\s", "");
				
				nmatrpm.put(Float.parseFloat(rpm), Float.parseFloat(nm));
			}
			if (line.startsWith("coefficientoffriction")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float coefficientoffriction = Float.parseFloat(line2);

				props.setCoefficientOfFriction(coefficientoffriction);
			}
			if (line.startsWith("rollingresistance")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float rollingresistance = Float.parseFloat(line2);

				props.setRollingResistance(rollingresistance);
			}
			if (line.startsWith("frontalarea")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float frontalarea = Float.parseFloat(line2);

				props.setFrontalArea(frontalarea);
			}
			if (line.startsWith("maxsteerangle")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float maxsteerangle = Float.parseFloat(line2);

				props.setMaxSteerAngle(maxsteerangle);
			}
			if (line.startsWith("distbetwheels")) {
				String line2 = line.split("=")[1].replaceAll("\\s", "");

				float distbetwheels = Float.parseFloat(line2);

				props.setDistanceBetweenWheels(distbetwheels);
			}
		}
		EngineTransmission engineTransmission = new EngineTransmission(gear1, gear2, gear3, gear4, gear5, gearR, gearDiff, nmatrpm);
		
		props.setDimensionsAndMass(dimsMass);
		props.setEngineTransmission(engineTransmission);
		
		carObject = new CarObject(objName, objFile, objTexture, interiorObjFile, interiorObjTexture, tireObjFile, tireObjTexture, aabbsize, props);
		
		return carObject;
	}

	public boolean isUsingMTLTexture() {
		return usingMTLTexture;
	}

	public String getObjTexture() {
		return objTexture;
	}

	public String getInteriorObjFile() {
		return interiorObjFile;
	}

	public String getInteriorObjTexture() {
		return interiorObjTexture;
	}

	public boolean isUsingMTLInteriorTexture() {
		return usingMTLInteriorTexture;
	}
	
	public String getTireObjFile() {
		return tireObjFile;
	}

	public String getTireObjTexture() {
		return tireObjTexture;
	}

	public boolean isUsingMTLTireTexture() {
		return usingMTLTireTexture;
	}

	public CarProperties getProps() {
		return props;
	}
	
}
