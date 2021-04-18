package com.angelo.game.utils.fileloaders;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.audio.Source;
import com.angelo.game.car.AICar;
import com.angelo.game.car.Car;
import com.angelo.game.car.PlayerCar;
import com.angelo.game.physicsEngine.CarPhysicsObject;
import com.angelo.game.physicsEngine.carComponents.Body;
import com.angelo.game.utils.CarProperties;

public class CarFileLoader {

	public static Car loadCar(String objName, Vector3f position, Vector3f rotation, Vector3f aabbSize, CarProperties props, boolean isPlayerCar){	
		Car car = null;
		
		if(isPlayerCar){
			car = new PlayerCar(objName, new CarPhysicsObject(position, rotation, aabbSize, props), new Source());		

			ScenarioLoader.currentScenario.cars.add(car);
		}
		else{
			car = new AICar(objName, new CarPhysicsObject(position, rotation, aabbSize, props), new Source());
			
			ScenarioLoader.currentScenario.cars.add(car);
		}
		
		return car;
	}
	
}
