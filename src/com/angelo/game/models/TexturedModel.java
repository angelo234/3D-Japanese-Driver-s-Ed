package com.angelo.game.models;

import org.lwjgl.util.vector.Vector3f;

public abstract class TexturedModel {

	private String modelName;
	private RawModel rawModel;
	private Vector3f aabbSize;
	private float mass;
	private float friction;
	private float restitution;
	
	private boolean obeysPhysics;
	
	public TexturedModel(String modelName, RawModel rawModel){
		this.rawModel = rawModel;
		this.modelName = modelName;
	}
	
	public TexturedModel(String modelName, RawModel rawModel, Vector3f aabbSize, float mass, float friction, float restitution){
		this.rawModel = rawModel;
		this.modelName = modelName;
		this.aabbSize = aabbSize;
		this.mass = mass;
		this.friction = friction;
		this.restitution = restitution;
		obeysPhysics = true;
	}
	
	public String getModelName(){
		return modelName;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public Vector3f getAABBSize() {
		return aabbSize;
	}
	
	public float getMass(){
		return mass;
	}

	public float getFriction() {
		return friction;
	}

	public float getRestitution() {
		return restitution;
	}

	public boolean obeysPhysics() {
		return obeysPhysics;
	}
	
}

