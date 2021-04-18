package com.angelo.game.physicsEngine.carComponents;

import org.lwjgl.util.vector.Vector3f;

public class FrontWheel extends Wheel{

	private double yaw = 0;
	
	public FrontWheel(Vector3f positionRelativeToAxle, double wheelRadius, boolean isDriveWheel) {
		super(positionRelativeToAxle, wheelRadius, isDriveWheel);
		
	}

}
