package com.angelo.game.roads;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.models.TexturedModel;
import com.angelo.game.textures.ModelTexture;

public class Road {

	private TexturedModel model;
	private ModelTexture texture;
	private Vector3f position;
	private Vector3f rotation;
	private List<Node> nodes;
	
	private double laneLength;
	private double laneWidth;
	
	private double lineLength;
	private double lineWidth;
	private double spacingBetweenDashedLines;
	
	private int lanes;
	
	private boolean isOneWayRoad;

	public Road(ModelTexture texture, int lanes, double laneLength, double laneWidth, double lineLength, double lineWidth, double spacingBetweenDashedLines, boolean isOneWayRoad){
		this.texture = texture;
		this.lanes = lanes;
		this.laneLength = laneLength;
		this.laneWidth = laneWidth;
		this.lineLength = lineLength;
		this.lineWidth = lineWidth;
		this.spacingBetweenDashedLines = spacingBetweenDashedLines;
		this.isOneWayRoad = isOneWayRoad;
	}
	
	public Road(ModelTexture texture, List<Node> nodes){
		this.texture = texture;
		this.nodes = new ArrayList<Node>(nodes);
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
	public void setTexture(ModelTexture texture) {
		this.texture = texture;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation){
		this.rotation = rotation;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	public Node getStartingNode() {
		return nodes.get(0);
	}
	
	public Node getEndingNode() {
		return nodes.get(nodes.size() - 1);
	}	

	public double getLaneLength() {
		return laneLength;
	}

	public double getLaneWidth() {
		return laneWidth;
	}

	public double getLineLength() {
		return lineLength;
	}

	public double getLineWidth() {
		return lineWidth;
	}

	public double getSpacingBetweenDashedLines() {
		return spacingBetweenDashedLines;
	}

	public int getLanes() {
		return lanes;
	}

	public boolean isOneWayRoad() {
		return isOneWayRoad;
	}
	
}
