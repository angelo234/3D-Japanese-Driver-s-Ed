package com.angelo.game.physicsEngine.carComponents;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.car.Car;
import com.angelo.game.utils.CarProperties;
import com.angelo.game.utils.Maths;

public class Body {
	
	private Car car;
	private CarProperties props;
	
	private FrontWheel lFWheel;
	private FrontWheel rFWheel;
	private RearWheel lRWheel;
	private RearWheel rRWheel;

	public Body(Car car, CarProperties props, FrontWheel leftFrontWheel, FrontWheel rightFrontWheel, RearWheel leftRearWheel, RearWheel rightRearWheel){
		this.car = car;
		this.props = props;
		this.lFWheel = leftFrontWheel;
		this.rFWheel = rightFrontWheel;
		this.lRWheel = leftRearWheel;
		this.rRWheel = rightRearWheel;
	}
	
	public void updateBody(){
		/*Vector3f lFWheelPosRel = this.lFWheel.getPositionRelativeToAxle();
		Vector3f rFWheelPosRel = this.rFWheel.getPositionRelativeToAxle();
		Vector3f lRWheelPosRel = this.lRWheel.getPositionRelativeToAxle();
		Vector3f rRWheelPosRel = this.rRWheel.getPositionRelativeToAxle();
		
		lFWheel.setPosition(new Vector3f(lFWheelPosRel.getX() + this.getPosition().getX(), lFWheelPosRel.getY() + this.getPosition().getY(), lFWheelPosRel.getZ() + this.getPosition().getZ()));
		rFWheel.setPosition(new Vector3f(rFWheelPosRel.getX() + this.getPosition().getX(), rFWheelPosRel.getY() + this.getPosition().getY(), rFWheelPosRel.getZ() + this.getPosition().getZ()));
		lRWheel.setPosition(new Vector3f(lRWheelPosRel.getX() + this.getPosition().getX(), lRWheelPosRel.getY() + this.getPosition().getY(), lRWheelPosRel.getZ() + this.getPosition().getZ()));
		rRWheel.setPosition(new Vector3f(rRWheelPosRel.getX() + this.getPosition().getX(), rRWheelPosRel.getY() + this.getPosition().getY(), rRWheelPosRel.getZ() + this.getPosition().getZ()));*/
		
		
	}
	
	public CarProperties getProps() {
		return props;
	}

	public Car getCar() {
		return car;
	}
	
	/*public double getCalculatedSpeed(){
		double engineRPM = props.getEngineTransmission().getEngineRPM();
		double transmissionGearRatio = props.getEngineTransmission().getTransmissionGearRatio();
		double axleRatio = props.getEngineTransmission().getAxleRatio();		
		double wheelRadius = Maths.getInchesFromCentimeters(lRWheel.getWheelRadius());
		
		return (0.00595) * (engineRPM * wheelRadius) / (transmissionGearRatio * axleRatio);
	}*/

	public Vector3f getPosition() {
		return car.getPosition();
	}

	public FrontWheel getLeftFrontWheel() {
		return lFWheel;
	}

	public FrontWheel getRightFrontWheel() {
		return rFWheel;
	}

	public RearWheel getLeftRearWheel() {
		return lRWheel;
	}

	public RearWheel getRightRearWheel() {
		return rRWheel;
	}

}
