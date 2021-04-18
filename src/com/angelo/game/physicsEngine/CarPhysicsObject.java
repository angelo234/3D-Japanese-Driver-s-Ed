package com.angelo.game.physicsEngine;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Matrix3f;

import com.angelo.game.Main;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.utils.CarProperties;
import com.angelo.game.utils.Maths;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.vehicle.DefaultVehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.dynamics.vehicle.VehicleTuning;
import com.bulletphysics.dynamics.vehicle.WheelInfo;
import com.bulletphysics.dynamics.vehicle.WheelInfoConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;

public class CarPhysicsObject {

	private CompoundShape carShape;
	private RigidBody carChassis;
	private RaycastVehicle vehicle;
	private CarProperties carProps;
	
	private int gear;
	private int actualgear;
	private float steeringAngle;
	
	private float lastGasPedalPosition;
	private float lastBrakePedalPosition;
	
	private static float engineIdleRPM = 800;	
	private static float wheelFriction = 1000f;//1e30f;
	private static float suspensionStiffness = 50f;
	private static float suspensionDampingRelaxation = (float) ((0.8f) * 2.0f * Math.sqrt(suspensionStiffness));
	private static float suspensionCompression = (float) ((0.6f) * 2.0f * Math.sqrt(suspensionStiffness));	
	private static float rollInfluence = 0.1f;//doesn't work
	
	public CarPhysicsObject(Object position, Object rotation, Object aabbSize, CarProperties props){
		Vector3f newPosition = null;
		Vector3f newRotation = null;
		Vector3f newAABBSize = null;
		
		//If object data types have instance of org.lwjgl.util.vector.Vector3f, then convert them into javax.vecmath.Vector3f 
		if(position instanceof org.lwjgl.util.vector.Vector3f || rotation instanceof org.lwjgl.util.vector.Vector3f || aabbSize instanceof org.lwjgl.util.vector.Vector3f){
			newPosition = Maths.toVecMathVector3f((org.lwjgl.util.vector.Vector3f) position);
			newRotation = Maths.toVecMathVector3f((org.lwjgl.util.vector.Vector3f) rotation);
			newAABBSize = Maths.toVecMathVector3f((org.lwjgl.util.vector.Vector3f) aabbSize);		
		}
		else{
			newPosition = (Vector3f) position;
			newRotation = (Vector3f) rotation;
			newAABBSize = (Vector3f) aabbSize;
		}
	
		this.carProps = props;
		
		//Create car chassis
		carShape = new CompoundShape();
		CollisionShape chassisShape = new BoxShape(newAABBSize);
		
		Transform localTrans = new Transform();
		localTrans.setIdentity();
		localTrans.origin.set(-0.18f,-0.2f,0);

		carShape.addChildShape(localTrans, chassisShape);
		
		/*Vector3f localInertia = new Vector3f(0, 0, 0);
		// rigidbody is dynamic if and only if mass is non zero,
		// otherwise static
		
		if (props.getMass() != 0f) {
			chassisShape.calculateLocalInertia(props.getMass(), localInertia);
		}		
		
		// using motionstate is recommended, it provides
		// interpolation capabilities, and only synchronizes
		// 'active' objects
		DefaultMotionState motionStateOffset = new DefaultMotionState(localTrans);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(props.getMass(), motionStateOffset, chassisShape, localInertia);
		RigidBody carOffsetChassis = new RigidBody(rbInfo);
		
		PhysicsEngine.getDynamicWorld().addRigidBody(carOffsetChassis);
		*/
		Transform transform = new Transform();
		transform.setIdentity();
		// localTrans effectively shifts the center of mass with respect to the chassis
		transform.origin.set(newPosition);
		MatrixUtil.setEulerZYX(transform.basis, (float)Math.toRadians(newRotation.x), (float)Math.toRadians(newRotation.y), (float)Math.toRadians(newRotation.z));

		createCarChassisRigidBody(transform, props.getMass());
		
		float wheelWidth = 0.185f;
		//float wheelX = 0.73f + wheelWidth;
		float wheelX = 0.73f;
		
		
		float connectionHeight = -0.55f;
		
		createVehicle(new Vector3f(wheelX, connectionHeight, 1.13f), new Vector3f(-wheelX, connectionHeight, 1.13f), new Vector3f(-wheelX, connectionHeight, -1.5f), new Vector3f(wheelX, connectionHeight, -1.5f), props.getWheelRadius());
		PhysicsEngine.getDynamicWorld().addRigidBody(carChassis);
	}
	
	private void createCarChassisRigidBody(Transform transform, float mass){
		Vector3f localInertia = new Vector3f(0, 0, 0);
		// rigidbody is dynamic if and only if mass is non zero,
		// otherwise static
		System.out.println(mass);
		
		if (mass != 0f) {
			carShape.calculateLocalInertia(mass, localInertia);
		}		
		
		// using motionstate is recommended, it provides
		// interpolation capabilities, and only synchronizes
		// 'active' objects
		DefaultMotionState motionState = new DefaultMotionState(transform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, carShape, localInertia);
		carChassis = new RigidBody(rbInfo);	
	}
	
	private void createVehicle(Vector3f flwOffsetPos, Vector3f frwOffsetPos, Vector3f blwOffsetPos, Vector3f brwOffsetPos, float wheelRadius){
		DefaultVehicleRaycaster vehicleRayCaster = new DefaultVehicleRaycaster(PhysicsEngine.getDynamicWorld());
		
		VehicleTuning tuning = new VehicleTuning();
		RaycastVehicle vehicle = new RaycastVehicle(tuning, carChassis, vehicleRayCaster);
		// never deactivate the vehicle
		
		carChassis.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
		PhysicsEngine.getDynamicWorld().addVehicle(vehicle);
		
		//float connectionHeight = 1.2f;

		// choose coordinate system
		vehicle.setCoordinateSystem(0, 1, 2);
		
		//Front wheels		
		WheelInfoConstructionInfo frontLeftWheelInfo = new WheelInfoConstructionInfo();
		frontLeftWheelInfo.chassisConnectionCS.set(flwOffsetPos);
		frontLeftWheelInfo.wheelDirectionCS.set(0, -1, 0);
		frontLeftWheelInfo.wheelAxleCS.set(-1, 0, 0);
		frontLeftWheelInfo.bIsFrontWheel = true;
		
		WheelInfoConstructionInfo frontRightWheelInfo = new WheelInfoConstructionInfo();
		frontRightWheelInfo.chassisConnectionCS.set(frwOffsetPos);
		frontRightWheelInfo.wheelDirectionCS.set(0, -1, 0);
		frontRightWheelInfo.wheelAxleCS.set(-1, 0, 0);
		frontRightWheelInfo.bIsFrontWheel = true;
		
		//Back wheels
		WheelInfoConstructionInfo backLeftWheelInfo = new WheelInfoConstructionInfo();
		backLeftWheelInfo.chassisConnectionCS.set(blwOffsetPos);
		backLeftWheelInfo.wheelDirectionCS.set(0, -1, 0);
		backLeftWheelInfo.wheelAxleCS.set(-1, 0, 0);
		backLeftWheelInfo.bIsFrontWheel = false;
		
		WheelInfoConstructionInfo backRightWheelInfo = new WheelInfoConstructionInfo();
		backRightWheelInfo.chassisConnectionCS.set(brwOffsetPos);
		backRightWheelInfo.wheelDirectionCS.set(0, -1, 0);
		backRightWheelInfo.wheelAxleCS.set(-1, 0, 0);
		backRightWheelInfo.bIsFrontWheel = false;
		
		//Add wheels to vehicle
		WheelInfo frontLeftWheel = new WheelInfo(frontLeftWheelInfo);
		this.addWheelToVehicle(vehicle, frontLeftWheel, tuning);
		
		WheelInfo frontRightWheel = new WheelInfo(frontRightWheelInfo);
		this.addWheelToVehicle(vehicle, frontRightWheel, tuning);
		
		WheelInfo backLeftWheel = new WheelInfo(backLeftWheelInfo);
		this.addWheelToVehicle(vehicle, backLeftWheel, tuning);
		
		WheelInfo backRightWheel = new WheelInfo(backRightWheelInfo);
		this.addWheelToVehicle(vehicle, backRightWheel, tuning);
		
		for (int i = 0; i < vehicle.getNumWheels(); i++) {
			WheelInfo wheel = vehicle.getWheelInfo(i);
			wheel.wheelsRadius = wheelRadius;
			//wheel.suspensionRestLength1 = suspensionRestLength;
			wheel.suspensionStiffness = suspensionStiffness;
			wheel.wheelsDampingRelaxation = suspensionDampingRelaxation;
			wheel.wheelsDampingCompression = suspensionCompression;
			wheel.frictionSlip = wheelFriction;
			wheel.rollInfluence = rollInfluence;
		}
		this.vehicle = vehicle;
	}
	
	private void addWheelToVehicle(RaycastVehicle vehicle, WheelInfo wheel, VehicleTuning tuning){
		vehicle.addWheel(wheel.chassisConnectionPointCS, wheel.wheelDirectionCS, wheel.wheelAxleCS, wheel.suspensionRestLength1, wheel.wheelsRadius, tuning, wheel.bIsFrontWheel);
		
	}
	
	public void update(){
		//PhysicsEngine.getDynamicWorld().getBroadphase().getOverlappingPairCache().cleanProxyFromPairs(carChassis.getBroadphaseHandle(), PhysicsEngine.getDynamicWorld().getDispatcher());
		Vector3f v = new Vector3f();
		this.carChassis.getLinearVelocity(v);
		//System.out.println(v.toString());
		
		updateAutomaticTransmission();
		
		if(this.getGear() == 0){
			this.actualgear = 0;
		}
		
		if(this.getGear() == 1){
			if(this.actualgear == 0 || this.actualgear == 6){
				this.actualgear = 1;
			}
			
		}
		
		else if(this.getGear() == 2){
			this.actualgear = 6;
		}
		
		// When in park
		if (this.getGear() == 0) {
			this.vehicle.setBrake(99, 0);
			this.vehicle.setBrake(99, 1);
		}

		//vehicle.getRigidBody().setUserPointer(userObjectPointer);
		
		vehicle.updateVehicle(DisplayManager.getDeltaTime());
	}
	
	private void updateAutomaticTransmission(){	
		
		if (this.getEngineRPM() > 6000 * Math.max(0.5f, lastGasPedalPosition)) {
			if (this.getActualGear() != 5) {
				actualgear += 1;
			}
		} else if (this.getEngineRPM() < 1500 && this.getActualGear() > 1) {
			actualgear -= 1;
		}

	}
	
	public float getEnginePower(){
		return this.vehicle.getWheelInfo(0).engineForce;
	}
	
	public void setEnginePower(float gasPedal){			
		//Set engine power to two front wheels (FWD)
		/*switch (this.getGear()) {
		case 0: {
			//Park 
			break;		
		}
		case 1: {
			//Reverse
			this.vehicle.applyEngineForce(-force, 0);
			this.vehicle.applyEngineForce(-force, 1);
			break;	
		}
		case 2: {
			//Neutral
			break;	
		}
		case 3: {
			//Drive
			this.vehicle.applyEngineForce(force, 0);
			this.vehicle.applyEngineForce(force, 1);
			break;	
		}
		case 4: {
			//2
			break;	
		}
		case 5: {
			//1
			break;	
		}
		}*/
		
		float driveforce = getDriveForce(gasPedal) / 2;

		if(this.getActualGear() > 0 && this.getActualGear() < 6){
			this.vehicle.applyEngineForce(driveforce, 0);
			this.vehicle.applyEngineForce(driveforce, 1);
			this.vehicle.applyEngineForce(driveforce, 2);
			this.vehicle.applyEngineForce(driveforce, 3);
		}
		else if(this.getActualGear() == 0){
			this.vehicle.applyEngineForce(0, 0);
			this.vehicle.applyEngineForce(0, 1);
			this.vehicle.applyEngineForce(0, 2);
			this.vehicle.applyEngineForce(0, 3);
		}
		else{
			this.vehicle.applyEngineForce(-driveforce, 0);
			this.vehicle.applyEngineForce(-driveforce, 1);
			this.vehicle.applyEngineForce(-driveforce, 2);
			this.vehicle.applyEngineForce(-driveforce, 3);
		}
	}
	
	private float getDriveForce(float gaspedalPosition){
		float driveForce = 0;
		
		float currentGearRatio = this.getCurrentGearRatio();
		float diffRatio = carProps.getEngineTransmission().getDiffRatio();
		float transmissionEfficiency = 0.8f;

		float rpm = this.getEngineRPM();		
		
		float startAmount = 0.05f;
		
		if(this.getKMH() > 2 || lastBrakePedalPosition > 0){
			startAmount = 0;
		}
		
		float engineTorque = carProps.getEngineTransmission().getEngineTorqueFromRPM(rpm) * Math.max(gaspedalPosition, startAmount);
		float driveTorque = engineTorque * currentGearRatio * diffRatio * transmissionEfficiency;

		driveForce = driveTorque / carProps.getWheelRadius();

		lastGasPedalPosition = gaspedalPosition;
		return driveForce;
	}
	
	public float getEngineRPM(){
		//rpm = wheel rotation rate * gear ratio * differential ratio * 60 / 2 pi
		float currentGearRatio = this.getCurrentGearRatio();
		float diffRatio = carProps.getEngineTransmission().getDiffRatio();
		float wheelRotationRate = (float) ((vehicle.getCurrentSpeedKmHour() / 3.6) / carProps.getWheelRadius() * (60 / (2 * Math.PI)));
		//System.out.println(wheelRotationRate2);
		
		float rpm = (float) (wheelRotationRate * currentGearRatio * diffRatio);
		
		if(rpm < engineIdleRPM){
			rpm = engineIdleRPM;
		}
		
		return rpm;
	}
	
	private float getCurrentGearRatio(){
		switch (this.getActualGear()) {
		case 0:{
			break;
		}
		case 1:{
			return carProps.getEngineTransmission().getGearRatio1();
		}
		case 2:{
			return carProps.getEngineTransmission().getGearRatio2();
		}
		case 3:{
			return carProps.getEngineTransmission().getGearRatio3();
		}
		case 4:{
			return carProps.getEngineTransmission().getGearRatio4();
		}
		case 5:{
			return carProps.getEngineTransmission().getGearRatio5();
		}
		case 6:{
			return carProps.getEngineTransmission().getGearRatioR();
		}
		}
		
		return 0;
	}
	
	public float getBrakingPower(){
		return this.vehicle.getWheelInfo(0).brake;
	}
	
	public void setBrakes(float force, boolean front, boolean back){	
		if(front){
			this.vehicle.setBrake(force, 0);
			this.vehicle.setBrake(force, 1);
		}
		if(back){
			this.vehicle.setBrake(force, 2);
			this.vehicle.setBrake(force, 3);
		}
		
		lastBrakePedalPosition = force;
		//System.out.println("Left rear wheel rotation: "+this.vehicle.getWheelInfo(2).deltaRotation);
		//System.out.println("Right rear wheel rotation: "+this.vehicle.getWheelInfo(3).deltaRotation);
	}
	
	public float getSteeringAngle(){
		return this.vehicle.getSteeringValue(0);
	}
	
	public void steer(float steeringValue){
		if(this.getSteeringAngle() > carProps.getMaxSteerAngle()){
			this.steeringAngle = carProps.getMaxSteerAngle();
		}
		else if(this.getSteeringAngle() < -carProps.getMaxSteerAngle()){
			this.steeringAngle = -carProps.getMaxSteerAngle();
		}
		else{
			this.steeringAngle += steeringValue * DisplayManager.getDeltaTime();
		}
		
		float roundedSteeringAngle = 0;

		if(this.steeringAngle > 0){
			roundedSteeringAngle = Maths.roundDownNumber(this.steeringAngle, 2);
		}
		else if(this.steeringAngle < 0){
			roundedSteeringAngle = Maths.roundUpNumber(this.steeringAngle, 2);
		}		

		//Set steering to two front wheels
		this.vehicle.setSteeringValue(roundedSteeringAngle, 0);
		this.vehicle.setSteeringValue(roundedSteeringAngle, 1);
	}
	
	public void setSteering(float steeringAngle) {
		//Set steering to two front wheels
		this.vehicle.setSteeringValue(steeringAngle, 0);
		this.vehicle.setSteeringValue(steeringAngle, 1);	
	}
		
	public int getActualGear() {
		return actualgear;
	}
	
	public int getGear(){
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public float[] getTransformationMatrix(){
		Transform transform = new Transform();
		carChassis.getMotionState().getWorldTransform(transform);
		
		float matrix[] = new float[16];
		
		transform.getOpenGLMatrix(matrix);
		
		return matrix;
	}
	
	public float[] getTireTransformationMatrix(int tire){
		Transform transform = new Transform();
		vehicle.getWheelTransformWS(tire, transform);
		
		float matrix[] = new float[16];
		
		transform.getOpenGLMatrix(matrix);
		
		return matrix;
	}
	
	public Vector3f getPosition(){
		Transform transform = new Transform();	
		carChassis.getMotionState().getWorldTransform(transform);
		
		return transform.origin;
	}
	
	public void setPosition(Vector3f vector){
		Transform transform = new Transform();		
		carChassis.getMotionState().getWorldTransform(transform);
		
		transform.origin.set(vector);
	}
	
	public void setPosition(float x, float y, float z){
		Transform transform = new Transform();		
		carChassis.getWorldTransform(transform);
		
		transform.origin.set(new Vector3f(x,y,z));
	}
	
	public Vector3f getRotation(){
		Transform transform = new Transform();
		carChassis.getMotionState().getWorldTransform(transform);

		Quat4f quat = new Quat4f();
		MatrixUtil.getRotation(transform.basis, quat);

		return Maths.toEulerForCar(quat);
	}
	
	public void setRotation(Vector3f vector){
		Transform transform = new Transform();
		carChassis.getMotionState().getWorldTransform(transform);
		
		MatrixUtil.setEulerZYX(transform.basis, (float)Math.toRadians(vector.x), (float)Math.toRadians(vector.y), (float)Math.toRadians(vector.z));
	}
	
	public void setRotation(float x, float y, float z){
		Transform transform = new Transform();
		carChassis.getMotionState().getWorldTransform(transform);
		
		MatrixUtil.setEulerZYX(transform.basis, (float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
	}
	
	public Vector3f getVelocity(){
		Vector3f vector = new Vector3f();
		carChassis.getLinearVelocity(vector);
		
		return vector;
	}
	
	public void setVelocity(Vector3f vector){
		carChassis.setLinearVelocity(vector);
	}
	
	public void setVelocity(float x, float y, float z){
		carChassis.setLinearVelocity(new Vector3f(x,y,z));
	}
	
	public Vector3f getAcceleration(){
		Vector3f vector = new Vector3f();
		
		carChassis.getGravity(vector);
		
		return vector;
	}
	
	public void setAcceleration(Vector3f vector){
		carChassis.setGravity(vector);
	}
	
	public void setAcceleration(float x, float y, float z){
		carChassis.setGravity(new Vector3f(x,y,z));
	}
	
	public float getKMH(){
		return vehicle.getCurrentSpeedKmHour();
	}

	public CarProperties getCarProps() {
		return carProps;
	}

}
