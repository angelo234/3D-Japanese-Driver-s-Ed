package com.angelo.game.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.models.TexturedModel;
import com.angelo.game.terrains.Terrain;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1,0,0);
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public Light(Vector2f position, Vector3f color, Vector3f attenuation, Terrain terrain, TexturedModel lampModel, Loader loader) {
		float y = terrain.getHeightOfTerrain(position.x, position.y);
		this.position = new Vector3f(position.x,y+17,position.y);
		this.color = color;
		this.attenuation = attenuation;	
	
		ScenarioLoader.currentScenario.entities.add(new Entity(lampModel,terrain, new Vector2f(position.x,position.y),new Vector3f(0,0,0),1));

	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation, TexturedModel lampModel, Loader loader) {
		this.position = new Vector3f(position.x,position.y+17,position.z);
		this.color = color;
		this.attenuation = attenuation;	
	
		ScenarioLoader.currentScenario.entities.add(new Entity(lampModel, new Vector3f(position.x, position.y,position.z),new Vector3f(0,0,0),1));

	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f postion) {
		this.position = postion;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}	
	
}
