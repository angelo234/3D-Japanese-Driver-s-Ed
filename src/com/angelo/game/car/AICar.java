package com.angelo.game.car;

import com.angelo.game.audio.Source;
import com.angelo.game.physicsEngine.CarPhysicsObject;

public class AICar extends Car{

	public AICar(String carObjFileName, CarPhysicsObject carPhysics, Source engineSoundSource) {
		super(carObjFileName, carPhysics, engineSoundSource);
	}
}
