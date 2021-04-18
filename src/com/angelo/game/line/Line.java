package com.angelo.game.line;

import org.lwjgl.util.vector.Vector3f;

public class Line {

	private Vector3f startPoint;
	private Vector3f endPoint;
	private Vector3f color;
	private float width;
	
	private LineModel model;
	
	public Line(Vector3f startPoint, Vector3f endPoint, Vector3f color, float width){
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
		this.width = width;
	}

	public LineModel getModel(){
		return model;
	}
	
	public void setModel(LineModel model){
		this.model = model;
	}
	
	public Vector3f getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3f startPoint) {
		this.startPoint = startPoint;
	}

	public Vector3f getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector3f endPoint) {
		this.endPoint = endPoint;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
}
