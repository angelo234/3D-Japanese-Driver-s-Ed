package com.angelo.game.renderEngine;

import java.util.ArrayList;
import java.util.List;

public class VAO {

	private List<Integer> vbos = new ArrayList<Integer>();
	private int id;
	private int verticesVBOID;
	private int uvsVBOID;
	private int normalsVBOID;
	private int tangentsVBOID;
	private int triangleIndicesVBOID;
	private int quadIndicesVBOID;
	
	public VAO(int id){
		this.id = id;
	}

	public List<Integer> getVBOS(){
		return vbos;
	}
	
	public int getVerticesVBOID() {
		return verticesVBOID;
	}

	public int getUVSVBOID() {
		return uvsVBOID;
	}

	public int getNormalsVBOID() {
		return normalsVBOID;
	}

	public int getTangentsVBOID() {
		return tangentsVBOID;
	}

	public int getTriangleIndicesVBOID(){
		return triangleIndicesVBOID;
	}
	
	public int getQuadIndicesVBOID(){
		return quadIndicesVBOID;
	}
	
	public void setVerticesVBO(int vboID){
		this.verticesVBOID = vboID;
		vbos.add(vboID);
	}
	
	public void setUVSVBO(int vboID){
		this.uvsVBOID = vboID;
		vbos.add(vboID);
	}
	
	public void setNormalsVBO(int vboID){
		this.normalsVBOID = vboID;
		vbos.add(vboID);
	}
	
	public void setTangentsVBO(int vboID){
		this.tangentsVBOID = vboID;
		vbos.add(vboID);
	}
	
	public void setTriangleIndicesVBO(int vboID){
		this.triangleIndicesVBOID = vboID;
		vbos.add(vboID);
	}
	
	public void setQuadIndicesVBO(int vboID){
		this.quadIndicesVBOID = vboID;
		vbos.add(vboID);
	}
	
	public int getID() {
		return id;
	}
}
