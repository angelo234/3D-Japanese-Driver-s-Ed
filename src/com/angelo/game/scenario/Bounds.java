package com.angelo.game.scenario;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.car.PlayerCar;

public class Bounds {

	private Vector3f farXLeft, farXRight, nearXLeft, nearXRight;
	
	public Bounds(Vector3f nearXLeft, Vector3f nearXRight, Vector3f farXLeft, Vector3f farXRight){
		this.nearXLeft = nearXLeft;
		this.nearXRight = nearXRight;
		this.farXLeft = farXLeft;
		this.farXRight = farXRight;	
	}

	public boolean isPlayerCarOutOfBounds(PlayerCar car){
		Vector3f carPos = car.getPosition();
		
		if(carPos.getX() > this.getNearXLeft().getX() && carPos.getX() < this.getFarXRight().getX()){	
			if(carPos.getZ() > this.getFarXLeft().getZ() && carPos.getZ() < this.getFarXRight().getZ()){
				return false;
			}
		}
		
		return true;
	}
	
	public Vector3f getFarXLeft() {
		return farXLeft;
	}

	public Vector3f getFarXRight() {
		return farXRight;
	}

	public Vector3f getNearXLeft() {
		return nearXLeft;
	}

	public Vector3f getNearXRight() {
		return nearXRight;
	}
	
}
