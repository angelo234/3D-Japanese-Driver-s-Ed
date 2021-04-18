package com.angelo.game.objConverter;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	
	private static final int NO_INDEX = -1;

	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	
	public Vertex(Vertex instance){
		this.position = instance.getPosition();
		this.textureIndex = instance.getTextureIndex();
		this.normalIndex = instance.getNormalIndex();
		this.duplicateVertex = instance.getDuplicateVertex();
		this.index = instance.getIndex();
		this.length = instance.getLength();
	}
	
	public Vertex(int index,Vector3f position){
		this.index = index;
		this.position = position;
		this.length = position.length();
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public float getLength(){
		return length;
	}
	
	public boolean isSet(){
		return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
