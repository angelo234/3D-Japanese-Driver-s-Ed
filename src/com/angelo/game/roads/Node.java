package com.angelo.game.roads;

import org.lwjgl.util.vector.Vector3f;

public class Node {

	private Node prevNode;
	private Node nextNode;
	
	private Vector3f position;
	private boolean curved;
	
	public Node(Vector3f position, boolean curved){
		this.position = position;
		this.setCurved(curved);
	}

	public Node getPrevNode() {
		return prevNode;
	}

	public void setPrevNode(Node prevNode) {
		this.prevNode = prevNode;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public boolean isCurved() {
		return curved;
	}

	public void setCurved(boolean curved) {
		this.curved = curved;
	}		
}
