package com.angelo.game.physicsEngine;

public final class PhysicsMaths {
	
	public static final double PASSENGER_TRAIN_ROLLING_RESISTANCE_COEFFICIENT = 0.0024D;
	public static final double PASSENGER_TRAIN_STATIC_FRICTION_COEFFICIENT = 0.35D;
	
	public static double getVolts(double watts, double amps){
		return watts / amps;
	}
	
	public static double getAmps(double watts, double volts){
		return watts / volts;
	}
	
	public static double getWatts(double volts, double amps){
		return volts * amps;	
	}
	
	public static double getKilovolts(double watts, double amps){
		return (watts / amps)/1000;
	}
	
	public static double getKiloamps(double watts, double volts){
		return (watts / volts)/1000;
	}
	
	public static double getKilowatts(double volts, double amps){
		return (volts * amps)/1000;	
	}
	
	public static double getAirResistance(double density, double speed, double dragCoefficient, double frontalArea){
		return 0.5D * density * Math.pow(speed, 2) * dragCoefficient * frontalArea;
	}

	/**Returns the tractive force(kN) from engine torque in Newton Meters (Nm), efficiency of motor train, transmission ratio, driving axle ratio, and wheel radius*/
	public static double getTractiveForce(double engineTorque, double efficiency, double transmissionRatio, double drivingAxleRatio, double wheelRadius){
		return (engineTorque * efficiency * transmissionRatio * drivingAxleRatio) / wheelRadius;
	}
	
	public static double getMaxTractiveForce(double weightOnDrivingWheel, double adhesionCoefficient){
		return weightOnDrivingWheel * adhesionCoefficient;
	}
	
	/**Returns the net force (Newtons) from mass (kg) and acceleration (m/s)*/
	public static double getNetForceFromMassAndAcceleration(double mass, double acceleration){
		return mass * acceleration;
	}
	
	public static double getNetForceFromFrictionAndThrust(double friction, double thrust){
		return (thrust / 9.807) - friction;
	}
	
	public static double getAcceleration(double force, double mass){
		return force / mass;
	}
	
	public static double getPowerFromForceAndSpeed(double netforce, double velocity){
		return netforce * velocity;
	}
	
	public static double getRollingForce(double coefficientOfRollingFriction, double mass, double wheelRadius){
		return coefficientOfRollingFriction * mass / wheelRadius;
	}
	
	public static double getRollingForce(double coefficientOfRollingFriction, double mass){
		return coefficientOfRollingFriction * mass * 9.81;
	}
	
	public static double getTorqueFromPowerAndRPM(double kilowatts, double rpm){
		return 9.5488 * kilowatts / rpm;
	}
	
	public static double getKMHFromRPM(double rpm, double wheelRadius){
		double circumference = 2 * Math.PI * wheelRadius;
		
		return (circumference * rpm) / 1000 * 60;
	}
	
	public static double getDensity(double mass, double volume){
		return mass / volume;
	}
	
	public static double getVolume(double length, double width, double height){
		return length * width * height;
	}
	
	public static double inKMH(double mps) {
		return mps * 3.6f;
	}

	public static double inMPS(double accelerationRate) {
		return accelerationRate / 3.6f;
	}
	
}
