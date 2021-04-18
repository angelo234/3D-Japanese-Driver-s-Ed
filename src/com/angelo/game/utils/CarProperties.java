package com.angelo.game.utils;

import com.angelo.game.physicsEngine.DimensionsAndMass;
import com.angelo.game.physicsEngine.carComponents.EngineTransmission;

public class CarProperties {
	
	private DimensionsAndMass dimensionsAndMass;
	private EngineTransmission engineTransmission;
	private float mass;
	private float wheelRadius;
	private float coefficientOfFriction;
	private float rollingResistance;
	private float frontalArea;
	private float maxSteerAngle;
	private float distanceBetweenWheels;
	
	public DimensionsAndMass getDimensionsAndMass() {
		return dimensionsAndMass;
	}
	public void setDimensionsAndMass(DimensionsAndMass dimensionsAndMass) {
		this.dimensionsAndMass = dimensionsAndMass;
	}
	public float getMass() {
		return mass;
	}
	public void setMass(float mass) {
		this.mass = mass;
	}
	public float getWheelRadius() {
		return wheelRadius;
	}
	public void setWheelRadius(float wheelRadius) {
		this.wheelRadius = wheelRadius;
	}
	public EngineTransmission getEngineTransmission() {
		return engineTransmission;
	}
	public void setEngineTransmission(EngineTransmission engineTransmission) {
		this.engineTransmission = engineTransmission;
	}
	public float getCoefficientOfFriction() {
		return coefficientOfFriction;
	}
	public void setCoefficientOfFriction(float coefficientOfFriction) {
		this.coefficientOfFriction = coefficientOfFriction;
	}
	public float getRollingResistance() {
		return rollingResistance;
	}
	public void setRollingResistance(float rollingResistance) {
		this.rollingResistance = rollingResistance;
	}
	public float getFrontalArea() {
		return frontalArea;
	}
	public void setFrontalArea(float frontalArea) {
		this.frontalArea = frontalArea;
	}
	public float getMaxSteerAngle() {
		return maxSteerAngle;
	}
	public void setMaxSteerAngle(float maxSteerAngle) {
		this.maxSteerAngle = maxSteerAngle;
	}
	public float getDistanceBetweenWheels() {
		return distanceBetweenWheels;
	}
	public void setDistanceBetweenWheels(float distanceBetweenWheels) {
		this.distanceBetweenWheels = distanceBetweenWheels;
	}
	
}
