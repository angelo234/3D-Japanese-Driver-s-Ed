package com.angelo.game.gameobjs;

import org.lwjgl.util.vector.Vector3f;

public abstract class PhysicsGameObject extends GameObject{

	private Vector3f aabbSize;
	private float mass;
	private float friction;
	private float restitution;
	
	protected PhysicsGameObject(String objName, String objFile, Vector3f aabbSize, float mass, float friction, float restitution) {
		super(objName, objFile);
		this.aabbSize = aabbSize;
		this.mass = mass;
		this.friction = friction;
		this.restitution = restitution;		
	}

	public Vector3f getAABBSize(){
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
	
}
