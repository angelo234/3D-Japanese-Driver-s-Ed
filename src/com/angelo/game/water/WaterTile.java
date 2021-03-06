package com.angelo.game.water;

public class WaterTile {
	
	public static final float TILE_SIZE = 120;
	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float height, float centerZ){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
