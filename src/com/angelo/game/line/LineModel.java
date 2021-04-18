package com.angelo.game.line;

public class LineModel {

	private int vaoID;
	private int vertexCount;

	public LineModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
}
