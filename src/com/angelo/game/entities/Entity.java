package com.angelo.game.entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.models.MTLModel;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.physicsEngine.PhysicsObject;
import com.angelo.game.terrains.Terrain;
import com.angelo.game.utils.Maths;
import com.bulletphysics.collision.shapes.BoxShape;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Matrix4f matRotation;
	private float scale;
	
	private PhysicsObject physicsObject;
	
	private boolean obeysPhysics;

	private static final int MAX_RENDER_DISTANCE = 100;
	
	private int textureIndex = 0;

	//Constructor for placing entity on ground	
	public Entity(TexturedModel model, Terrain terrain, Vector2f position, Vector3f rotation, float scale) {	
		float y = terrain.getHeightOfTerrain(position.x, position.y);
		this.position = new Vector3f(position.x,y,position.y);		
		this.model = model;	
		this.rotation = rotation;
		this.scale = scale;
	}
	
	/**Create an entity that obeys the laws of physics.*/
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale, Vector3f aabbSize, float mass, float friction, float restitution) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.matRotation = new Matrix4f();
		this.scale = scale;

		this.physicsObject = new PhysicsObject(position, rotation, new BoxShape(Maths.toVecMathVector3f(aabbSize)), mass, friction, restitution);
		this.obeysPhysics = true;		
	}
	
	/**Constructor for creating an entity*/
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, float scale) {
		this.textureIndex = index;
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	//Constructor for placing entity on ground	
	public Entity(TexturedModel model, Terrain terrain, int index, Vector2f position, Vector3f rotation, float scale) {
		float y = terrain.getHeightOfTerrain(position.x, position.y);
		this.position = new Vector3f(position.x,y,position.y);		
		this.textureIndex = index;
		this.model = model;
		this.rotation = rotation;
		this.scale = scale;
	}

	public float getTextureXOffset(){ 
		if(model instanceof PNGModel){
			PNGModel pngModel = (PNGModel) model;
			
			int column = textureIndex % pngModel.getTexture().getNumberOfRows();
			return (float)column / (float)pngModel.getTexture().getNumberOfRows(); 
		}
		else if(model instanceof MTLModel){
			
		}
		
		return 0;
	} 
	
	public float getTextureYOffset(){ 
		if(model instanceof PNGModel){
			PNGModel pngModel = (PNGModel) model;
			
			int row = textureIndex % pngModel.getTexture().getNumberOfRows();
			return (float)row / (float)pngModel.getTexture().getNumberOfRows(); 
		}
		else if(model instanceof MTLModel){
			
		}
		return 0;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rotation.x+=dx;
		this.rotation.y+=dy;
		this.rotation.z+=dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getRotation(){
		return rotation;
	}
	
	public void setRotation(Vector3f rotation){
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}
	
	public boolean obeysPhysics() {
		return obeysPhysics;
	}

	public boolean getRenderable(Camera camera){
		float distanceBetweenTwo = Maths.getIntXYZDistance(this.getPosition().x, this.getPosition().y, this.getPosition().z, camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
			if(distanceBetweenTwo < MAX_RENDER_DISTANCE){
				return true;
			}		
		
		return false;
	}
}
