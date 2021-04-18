package com.angelo.game.guis;

import org.lwjgl.util.vector.Vector2f;

import com.angelo.game.models.RawModel;
import com.angelo.game.utils.Loader;

public class GUITexture {

	private RawModel quad;
	private int texture;
	private Vector2f topLeft;
	private Vector2f bottomRight;
	private boolean visible;
	private float scaleFactor;
	
	public GUITexture(int texture, Vector2f topLeft, Vector2f bottomRight) {
		this.texture = texture;
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		
		loadMesh();		
	}

	private void loadMesh(){
		float positions[] = {topLeft.getX(), topLeft.getY(), topLeft.getX(), bottomRight.getY(), bottomRight.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY()};
		float texCoords[] = {0,0,0,1,1,0,1,1};
		
		quad = Loader.loadToVAO(positions,texCoords, 2);
	}
	
	public float getScaleFactor(){
		return scaleFactor;
	}
	
	public void setScaleFactor(float scaleFactor){
		float positions[] = {topLeft.getX() - scaleFactor, topLeft.getY() - scaleFactor, topLeft.getX() - scaleFactor, bottomRight.getY() + scaleFactor, bottomRight.getX() + scaleFactor, topLeft.getY() - scaleFactor, bottomRight.getX() + scaleFactor, bottomRight.getY() + scaleFactor};
		
		quad.setVertexPositions(positions);
		this.scaleFactor = scaleFactor;
	}
	
	public RawModel getQuad(){
		return quad;
	}
	
	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture){
		this.texture = texture;
	}
	
	public Vector2f getPosition(){
		return new Vector2f((this.getTopLeft().getX() + this.getTopRight().getX()) / 2, (this.getTopLeft().getY() + this.getBottomLeft().getY()) / 2);
	}
	
	public Vector2f getTopLeft() {
		return topLeft;
	}
	
	public Vector2f getTopRight(){
		return new Vector2f(bottomRight.getX(), topLeft.getY());
	}

	public Vector2f getBottomLeft(){
		return new Vector2f(topLeft.getX(), bottomRight.getY());
	}
	
	public Vector2f getBottomRight() {
		return bottomRight;
	}
	
	public boolean getVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
}
