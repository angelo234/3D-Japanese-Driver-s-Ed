package com.angelo.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.car.PlayerCar;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

public class PlayerCamera extends Camera{

	private float yawOfCameraInCar;
	private float velocity = 0;

	private final float MOVINGSPEED = 30f;
	
	private int cameraMode = FIRST_PERSON;
	
	private boolean fixToPlayerCar;
	
	public PlayerCamera(){
		super();
	}
	
	public void move() {
		if(this.getCameraMode() == Camera.FIRST_PERSON){
			if(this.isFixToPlayerCar()){
				PlayerCar car = ScenarioLoader.currentScenario.getPlayerCar();
				
				calculateCameraPositionInCar(car);
				calculateCameraRotationInCar(car);
			}
			else{}
		}
		else if(this.getCameraMode() == Camera.THIRD_PERSON){
			if(this.isFixToPlayerCar()){
				
			}
			else{}
		}
		else if(this.getCameraMode() == Camera.FREEROAM){
			calculateFreeCameraPosition();
			calculateFreeCameraRotation();
		}
	}
	
	private void calculateFreeCameraRotation(){
		float pitchChange = Mouse.getDY() * 0.15f;
		float angleChange = Mouse.getDX() * 0.15f;
		
		this.getRotation().setX(this.getRotation().getX() - pitchChange);
		this.getRotation().setY(this.getRotation().getY() + angleChange);
		
		if (this.getRotation().getX() > 90) {
			this.getRotation().setX(90);
		} else if (this.getRotation().getX() < -90) {
			this.getRotation().setX(-90);
		}	

		if (this.getRotation().getY() > 360) {
			this.getRotation().setY(this.getRotation().getY() - 360);
		}
		if (this.getRotation().getY() < 0) {
			this.getRotation().setY(this.getRotation().getY() + 360);
		}
	}
	
	private void calculateFreeCameraPosition() {

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			velocity = -MOVINGSPEED;
		}

		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			velocity = MOVINGSPEED;
		} else {
			velocity = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			velocity *= 3;
		}

		float distance = velocity * DisplayManager.getDeltaTime();

		float dx = (float) (distance * Math.sin(Math.toRadians(-this.getRotation().getY())));
		float dy = (float) (distance * Math.sin(Math.toRadians(this.getRotation().getX())));
		float dz = (float) (distance * Math.cos(Math.toRadians(-this.getRotation().getY())));
		
		this.increasePosition(dx, dy, dz);
	}
	
	private void calculateCameraPositionInCar(PlayerCar car){
		float x = (float) (car.getPosition().x+(0.4f*Math.cos(Math.toRadians(-car.getRotation().getY()))));	
		float y = (float) (car.getPosition().y+0.4f);				
		float z = (float) (car.getPosition().z+(0.4f*Math.sin(Math.toRadians(car.getRotation().getY()))));	
		
		if(!Float.isNaN(car.getRotation().getY())){
			this.setPosition(x, y, z);
		}	
	}

	private void calculateCameraRotationInCar(PlayerCar car){
		float pitchChange = Mouse.getDY() * 0.1f;
		float yawChange = Mouse.getDX() * 0.1f;

		if(this.getRotation().getX() > 5){
			this.getRotation().setX(5);
		
		}
		else if(this.getRotation().getX() < -20){
			this.getRotation().setX(-20);
		}
		else{
			this.getRotation().setX(this.getRotation().getX() - pitchChange);
		}
		
		
		this.getRotation().setZ(0);
		
		if(yawOfCameraInCar > 180){
			yawOfCameraInCar = 180;
		}
		else if(yawOfCameraInCar < -180){
			yawOfCameraInCar = -180;
		}
		else{
			yawOfCameraInCar += yawChange;
		}
		
		if(Float.isNaN(car.getRotation().getY())){
			this.getRotation().setY(yawOfCameraInCar - 90);
		}
		else{				
			this.getRotation().setY(yawOfCameraInCar + car.getRotation().getY());
		}
	}
	
	public float getYawOfCameraInCar(){
		return yawOfCameraInCar;
	}
	
	public void setYawOfCameraInCar(float yawOfCameraInCar){
		this.yawOfCameraInCar = yawOfCameraInCar;
	}
	
	public void increaseYawOfCameraInCar(float yawOfCameraInCar){
		this.yawOfCameraInCar += yawOfCameraInCar;
	}

	public int getCameraMode() {
		return cameraMode;
	}

	public void setCameraMode(int cameraMode) {
		this.cameraMode = cameraMode;
	}

	public boolean isFixToPlayerCar() {
		return fixToPlayerCar;
	}

	public void setFixToPlayerCar(boolean fixToPlayerCar) {
		this.fixToPlayerCar = fixToPlayerCar;
	}
	
}
