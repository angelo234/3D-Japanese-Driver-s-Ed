package com.angelo.game.physicsEngine.carComponents;

import java.util.Map;

public class EngineTransmission {

	private float engineRPM;
	private float gearRatio1;
	private float gearRatio2;
	private float gearRatio3;
	private float gearRatio4;
	private float gearRatio5;
	private float gearRatioR;
	private float diffRatio;
	
	private Map<Float, Float> nmatrpm;

	public EngineTransmission(float gearRatio1, float gearRatio2, float gearRatio3, float gearRatio4, float gearRatio5, float gearRatioR, float diffRatio, Map<Float, Float> nmatrpm){
		this.gearRatio1 = gearRatio1;
		this.gearRatio2 = gearRatio2;
		this.gearRatio3 = gearRatio3;
		this.gearRatio4 = gearRatio4;
		this.gearRatio5 = gearRatio5;
		this.gearRatioR = gearRatioR;
		this.diffRatio = diffRatio;
		this.nmatrpm = nmatrpm;
	}
	
	public void update(){
		
	}

	public float getEngineTorqueFromRPM(float rpm){
		//-0.000008607x^2+0.07600x+19.60		
		float torque = (float) (-0.000008607 * Math.pow(rpm, 2) + 0.076 * rpm + 19.6);
		
		return torque;
	}
	
	public float getEngineRPM() {
		return engineRPM;
	}

	public void setEngineRPM(float engineRPM) {
		this.engineRPM = engineRPM;
	}

	public float getGearRatio1() {
		return gearRatio1;
	}

	public float getGearRatio2() {
		return gearRatio2;
	}

	public float getGearRatio3() {
		return gearRatio3;
	}

	public float getGearRatio4() {
		return gearRatio4;
	}

	public float getGearRatio5() {
		return gearRatio5;
	}

	public float getGearRatioR() {
		return gearRatioR;
	}

	public float getDiffRatio() {
		return diffRatio;
	}
	
	

}
