package com.angelo.game.line;

public class LineData {

	private float[] vertices;
	private float[] normals;
	private int[] indices;

	public LineData(float[] vertices, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.indices = indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}
	
}
