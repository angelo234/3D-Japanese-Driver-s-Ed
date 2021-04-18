package com.angelo.game.physicsEngine;

import org.lwjgl.util.vector.Vector3f;

public class DimensionsAndMass {

	private double length;
	private double width;
	private double height;

	public DimensionsAndMass(){}
	
	/**Specifies the dimensions (meters)*/
	public DimensionsAndMass(double length, double width, double height){
		this.length = length;
		this.width = width;
		this.height = height;
	}

	public Vector3f getDimensions(){
		return new Vector3f((float)getLength(), (float)getWidth(), (float)getHeight());
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
}
