package com.angelo.game.scenario;

import org.lwjgl.util.vector.Vector3f;

public class WayPoint {

	public static final int THRU = 0;
	public static final int STOP = 1;
	public static final int YIELD = 2;
	public static final int BEGIN = 3;
	public static final int END = 4;
	
	private Vector3f position;
	private Vector3f rotation;
	private int type;
	private String text;
	private float displayTextRadius;
	
	public WayPoint(Vector3f position, Vector3f rotation, int type, String text, float displayTextRadius){
		this.position = position;
		this.rotation = rotation;
		this.type = type;
		this.text = text;
		this.displayTextRadius = displayTextRadius;
	}

	public static int stringToWayPointType(String wayPointType){
		switch(wayPointType){
		case "THRU": return WayPoint.THRU;
		case "STOP": return WayPoint.STOP;
		case "YIELD": return WayPoint.YIELD;
		case "BEGIN": return WayPoint.BEGIN;
		case "END": return WayPoint.END;
		}
		return -1;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}

	public int getType() {
		return type;
	}
	
	public String getText(){
		return text;
	}
	
	public float getDisplayTextRadius(){
		return displayTextRadius;
	}
	
}
