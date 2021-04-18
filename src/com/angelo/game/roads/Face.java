package com.angelo.game.roads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.angelo.game.objConverter.Vertex;
import com.angelo.game.utils.Maths;

public class Face {

	private Vertex vertex1;
	private Vertex vertex2;
	private Vertex vertex3;
	
	public Face(){}

	public Vertex getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vertex vertex1) {
		this.vertex1 = vertex1;
	}

	public Vertex getVertex2() {
		return vertex2;
	}

	public void setVertex2(Vertex vertex2) {
		this.vertex2 = vertex2;
	}

	public Vertex getVertex3() {
		return vertex3;
	}

	public void setVertex3(Vertex vertex3) {
		this.vertex3 = vertex3;
	}
	
	public void setNextVertex(Vertex vertex){
		if(this.getVerticesSet() == 0){
			this.setVertex1(vertex);
		}
		else if(this.getVerticesSet() == 1){
			this.setVertex2(vertex);
		}
		else if(this.getVerticesSet() == 2){
			this.setVertex3(vertex);
		}
	}
	
	public void setFrontFace(){		
		List<Vertex> vertices = new ArrayList<Vertex>();
	
		vertices.add(new Vertex(this.getVertex1()));
		vertices.add(new Vertex(this.getVertex2()));
		vertices.add(new Vertex(this.getVertex3()));

		if(!Maths.isFrontFacing(this)){
			Collections.reverse(vertices);
		}
		
		this.setVertex1(vertices.get(0));
		this.setVertex2(vertices.get(1));
		this.setVertex3(vertices.get(2));
		
		this.getVertex1().setNormalIndex(1);
		this.getVertex2().setNormalIndex(1);
		this.getVertex3().setNormalIndex(1);		
	}
	
	public int getVerticesSet(){
		int count = 0;

		if(this.getVertex1() != null){
			if (this.getVertex1().getPosition() != null) {
				count++;
			}
		}
		if(this.getVertex2() != null){
			if (this.getVertex2().getPosition() != null) {
				count++;
			}
		}		
		if(this.getVertex3() != null){
			if (this.getVertex3().getPosition() != null) {
				count++;
			}
		}	
		return count;
	}
}
