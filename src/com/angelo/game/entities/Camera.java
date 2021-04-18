package com.angelo.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.car.PlayerCar;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

public abstract class Camera {

	public static final int FIRST_PERSON = 0;
	public static final int THIRD_PERSON = 1;
	public static final int FREEROAM = 2;
	
	private Vector3f position;
	private Vector3f rotation;
	private float yawOfCameraInCar;
	private float velocity = 0;

	private final float MOVINGSPEED = 30f;
	
	private int cameraMode = FIRST_PERSON;
	
	private boolean fixToPlayerCar;
	
	public Camera() {
		position = new Vector3f(0,0,0);
		rotation = new Vector3f(0,0,0);
	}
	
	public void invertPitch() {
		this.getRotation().setX(-this.getRotation().getX());
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(float x, float y, float z){
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setRotation(float pitch, float yaw, float roll){
		this.rotation.set(pitch, yaw, roll);
	}
	
	public Vector3f getRotation(){
		return rotation;
	}
	
	public void increaseRotation(float pitch, float yaw, float roll){
		this.rotation.set(this.getRotation().x + pitch, this.getRotation().y + yaw, this.getRotation().z + roll);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	
	/*private void calculateFreeCameraPosition() {

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			velocity = -MOVINGSPEED;
		}

		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			velocity = MOVINGSPEED;
		} else {
			velocity = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			velocity *= 3;
		}

		float distance = velocity * DisplayManager.getDeltaTime();

		float dx = (float) (distance * Math.sin(Math.toRadians(360 - this.getYaw())));
		float dz = (float) (distance * Math.cos(Math.toRadians(360 - this.getYaw())));
		float dy = (float) (distance * Math.sin(Math.toRadians(180 - this.getPitch())));

		this.increasePosition(dx, dy, dz);
	}*/

	/*private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotation().getY();

		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticDistance + 1.8f;
		position.z = player.getPosition().z - offsetZ;
		// System.out.println(position.x+"_"+position.y+"_"+position.z);
	}*/
	
	

}
