package com.angelo.game.line;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.objConverter.Vertex;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.Maths;

public class LineDataLoader {

	public static List<Integer> vaoIDs = new ArrayList<Integer>();
	
	static List<Vertex> vertices = new ArrayList<Vertex>();
	static List<Integer> indices = new ArrayList<Integer>();
	
	static {
		//Face 1
		vertices.add(new Vertex(0, new Vector3f(0,0,0)));
		vertices.add(new Vertex(1, new Vector3f(0,0,0)));
		vertices.add(new Vertex(2, new Vector3f(0,0,0)));

		// Face 2
		vertices.add(new Vertex(3, new Vector3f(0,0,0)));
		vertices.add(new Vertex(4, new Vector3f(0,0,0)));
		vertices.add(new Vertex(5, new Vector3f(0,0,0)));
		
		for(int i = 0;i<vertices.size();i++){
			vertices.get(i).setNormalIndex(0);
			indices.add(i);
			
			//System.out.println(vertices.get(i).getPosition());
		}
	}
	
	public static void loadLineModel(Line line){
		//System.out.println(line.getStartPoint()+"_"+line.getEndPoint()+"_"+Vector3f.sub(line.getEndPoint(), line.getStartPoint(), null));
		
		Vector3f rotation = Maths.getAngles(line.getStartPoint(), line.getEndPoint());		
		
		//Face 1
		vertices.get(0).setPosition(getLeftVertex(line.getStartPoint(), rotation, line.getWidth()));
		vertices.get(1).setPosition(getRightVertex(line.getEndPoint(), rotation, line.getWidth()));
		vertices.get(2).setPosition(getLeftVertex(line.getEndPoint(), rotation, line.getWidth()));
		
		//Face 2
		vertices.get(3).setPosition(getRightVertex(line.getEndPoint(), rotation, line.getWidth()));
		vertices.get(4).setPosition(getLeftVertex(line.getStartPoint(), rotation, line.getWidth()));
		vertices.get(5).setPosition(getRightVertex(line.getStartPoint(), rotation, line.getWidth()));
		
		/*
		//Face 1
		vertices.add(new Vertex(0, getLeftVertex(line.getStartPoint(), rotation, line.getWidth())));
		vertices.add(new Vertex(1, getRightVertex(line.getEndPoint(), rotation, line.getWidth())));
		vertices.add(new Vertex(2, getLeftVertex(line.getEndPoint(), rotation, line.getWidth())));
				
		//Face 2
		vertices.add(new Vertex(3, getRightVertex(line.getEndPoint(), rotation, line.getWidth())));
		vertices.add(new Vertex(4, getLeftVertex(line.getStartPoint(), rotation, line.getWidth())));
		vertices.add(new Vertex(5, getRightVertex(line.getStartPoint(), rotation, line.getWidth())));
		
		for(int i = 0;i<vertices.size();i++){
			vertices.get(i).setNormalIndex(0);
			indices.add(i);
			
			//System.out.println(vertices.get(i).getPosition());
		}		
		 */
		
		float verticesArray[] = convertVerticesToArrays(vertices);
		float normalsArrayList[] = {0,1,0,0,-1,0};
		float normalsArray[] = convertNormalsToArrays(vertices, normalsArrayList);
		int indicesArray[] = convertIndicesListToArray(indices);
		
		line.setModel(Loader.loadToVAO(new LineData(verticesArray, normalsArray, indicesArray)));
		
		vaoIDs.add(line.getModel().getVaoID());
	}
	
	private static Vector3f getLeftVertex(Vector3f pos, Vector3f rotation, float width){
		float yaw = rotation.getY()+90;
		float pitch = rotation.getZ();
		
		Vector2f point = Maths.getPointOnCircleCircumference(Maths.toVector2f(pos), width / 2, yaw);
		Vector3f vec3pos = new Vector3f(point.getX(), pos.getY(), point.getY());
		
		return vec3pos;
	}
	
	private static Vector3f getRightVertex(Vector3f pos, Vector3f rotation, float width){
		float yaw = rotation.getY()-90;
		float pitch = rotation.getZ();
		
		Vector2f point = Maths.getPointOnCircleCircumference(Maths.toVector2f(pos), width / 2, yaw);
		Vector3f vec3pos = new Vector3f(point.getX(), pos.getY(), point.getY());
		
		return vec3pos;
	}
	
	private static float[] convertVerticesToArrays(List<Vertex> vertices) {
		float verticesArray[] = new float[vertices.size() * 3];
		
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);

			verticesArray[i * 3] = currentVertex.getPosition().x;
			verticesArray[i * 3 + 1] = currentVertex.getPosition().y;
			verticesArray[i * 3 + 2] = currentVertex.getPosition().z;
		}
		return verticesArray;
	}
	
	private static float[] convertNormalsToArrays(List<Vertex> vertices, float[] normalsArrayList) {
		float normalsArray[] = new float[vertices.size() * 3];
		
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);

			normalsArray[i * 3] = normalsArrayList[currentVertex.getNormalIndex() * 3];
			normalsArray[i * 3 + 1] = normalsArrayList[currentVertex.getNormalIndex() * 3 + 1];
			normalsArray[i * 3 + 2] = normalsArrayList[currentVertex.getNormalIndex() * 3 + 2];
		}
		return normalsArray;
	}
	
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}
	
}
