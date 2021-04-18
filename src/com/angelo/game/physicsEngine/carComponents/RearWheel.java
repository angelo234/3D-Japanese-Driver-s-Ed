package com.angelo.game.physicsEngine.carComponents;

import org.lwjgl.util.vector.Vector3f;

public class RearWheel extends Wheel{

	public RearWheel(Vector3f positionRelativeToAxle, double wheelRadius) {
		super(positionRelativeToAxle, wheelRadius, true);
		
	}

}
