package com.angelo.game.physicsEngine.carComponents;

import org.lwjgl.util.vector.Vector3f;

public class Wheel {

	private Vector3f position;
	private Vector3f positionRelativeToAxle;
	private double wheelAngle;
	
	private double rpm;
	
	private double wheelRadius;
	private double coefficientOfRollingFriction;
	
	private boolean isDriveWheel;
	
	public Wheel(Vector3f positionRelativeToAxle, double wheelRadius, boolean isDriveWheel){
		this.wheelRadius = wheelRadius;
		this.positionRelativeToAxle = positionRelativeToAxle;
		this.isDriveWheel = isDriveWheel;
	}
	
	public double getWheelRadius() {
		return wheelRadius;
	}
	
	public void setWheelRadius(double wheelRadius) {
		this.wheelRadius = wheelRadius;
	}
	
	public double getWheelAngle() {
		return wheelAngle;
	}

	public void setWheelAngle(double wheelAngle) {
		this.wheelAngle = wheelAngle;
	}

	public double getRPM() {
		return rpm;
	}

	public void setRPM(double rpm) {
		this.rpm = rpm;
	}

	public Vector3f getPosition(){
		return position;
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
	}
	
	public Vector3f getPositionRelativeToAxle() {
		return positionRelativeToAxle;
	}

	public boolean isDriveWheel() {
		return isDriveWheel;
	}

	public double getCoefficientOfRollingFriction() {
		return coefficientOfRollingFriction;
	}
	
	public void setCoefficientOfRollingFriction(double coefficientOfRollingFriction) {
		this.coefficientOfRollingFriction = coefficientOfRollingFriction;
	}
	
	
}
