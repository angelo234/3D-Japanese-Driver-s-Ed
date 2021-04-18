package com.angelo.game.car;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.audio.Source;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.physicsEngine.CarPhysicsObject;
import com.angelo.game.utils.Maths;

public class Car{

	private CarPhysicsObject carPhysics;	
	
	private String carObjFileName;
	
	private TexturedModel model;
	private TexturedModel interiorModel;
	private TexturedModel tireModel;
	
	private Source engineSoundSource;
	
	
	public Car(String carObjFileName, CarPhysicsObject carPhysics, Source engineSoundSource) {
		this.carObjFileName = carObjFileName;
		this.carPhysics = carPhysics;
		this.engineSoundSource = engineSoundSource;
	}
	
	public void accelerate(float gasPedalPosition){
		carPhysics.setEnginePower(gasPedalPosition);
	}
	
	public void brake(float force){
		carPhysics.setBrakes(force, true, true);
	}
	
	public void handBrake(){
		carPhysics.setBrakes(100, false, true);
	}

	public float getSteering(){
		return carPhysics.getSteeringAngle();
	}
	
	public void steer(float steeringAngle){
		carPhysics.steer(steeringAngle);
	}

	public void setSteering(float steeringAngle){
		carPhysics.setSteering(steeringAngle);
	}
	
	public Matrix4f getTransformationMatrix(){
		return Maths.toLWJGLMatrix4f(carPhysics.getTransformationMatrix());
	}
	
	public Matrix4f getTireTransformationMatrix(int tire){
		return Maths.toLWJGLMatrix4f(carPhysics.getTireTransformationMatrix(tire));
	}
	
	public Vector3f getPosition() {
		return Maths.toLWJGLVector3f(carPhysics.getPosition());
	}

	public void setPosition(Vector3f position) {
		carPhysics.setPosition(position.x, position.y, position.z);
	}
	
	public void setPosition(float x, float y, float z) {
		carPhysics.setPosition(x, y, z);	
	}
	
	public Vector3f getRotation() {
		return Maths.toLWJGLVector3f(carPhysics.getRotation());
	}

	public void setRotation(Vector3f rotation) {
		carPhysics.setRotation(rotation.x, rotation.y, rotation.z);
	}	
	
	public Vector3f getVelocity(){
		return Maths.toLWJGLVector3f(carPhysics.getVelocity());
	}
	
	public void setVelocity(Vector3f vector){
		carPhysics.setVelocity(vector.x, vector.y, vector.z);
	}
	
	public void setVelocity(float x, float y, float z){
		carPhysics.setVelocity(x, y, z);
	}

	public float getKMH() {
		return carPhysics.getKMH();		
	}
	
	public boolean collidedWithObject(){
		return true;
	}
	
	public String getCarObjFileName() {
		return carObjFileName;
	}

	public CarPhysicsObject getCarPhysics(){
		return carPhysics;
	}

	public TexturedModel getCarModel() {
		return model;
	}
	
	public void setCarModel(TexturedModel model) {
		this.model = model;
	}
	
	public TexturedModel getCarInteriorModel(){
		return interiorModel;
	}
	
	public void setCarInteriorModel(TexturedModel interiorModel){
		this.interiorModel = interiorModel;
	}

	public TexturedModel getTireModel() {
		return tireModel;
	}

	public void setTireModel(TexturedModel tireModel) {
		this.tireModel = tireModel;
	}

	public Source getEngineSoundSource() {
		return engineSoundSource;
	}

}
