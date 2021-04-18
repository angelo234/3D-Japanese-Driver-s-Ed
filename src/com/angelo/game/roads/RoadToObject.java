package com.angelo.game.roads;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.entities.Entity;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.objConverter.ModelData;
import com.angelo.game.objConverter.Vertex;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.GlobalVariables;
import com.angelo.game.utils.Loader;
import com.angelo.game.utils.Maths;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public class RoadToObject {

	public static List<Node> nodes = new ArrayList<Node>();
	
	public static TexturedModel convertRoad(Road road, ModelTexture texture, TexturedModel marker1, TexturedModel marker2){		
		List<Vertex> vertices = createVerticesFromNodes(road, marker1, marker2);
		List<Integer> indices = new ArrayList<Integer>();

		DelaunayTriangulator delaunayTriangulator = null;
		List<Triangle2D> oldfaces = null;

		try {
		    List<Vector2D> pointSet = Maths.convertVertexListToPositions(vertices);
		    for(Vector2D vector : pointSet){
				//System.out.println(vector.toString());
			}
		    delaunayTriangulator = new DelaunayTriangulator(pointSet);
		    delaunayTriangulator.triangulate();
		    oldfaces = ridUselessFaces(delaunayTriangulator.getTriangles());  
		    //oldfaces = delaunayTriangulator.getTriangles();
		    
		} catch (NotEnoughPointsException e) {
		}
			
		List<Face> newfaces = new ArrayList<Face>();
		
		for(Triangle2D triangle : oldfaces){
			Face face = new Face();
			for(Vertex vertex : vertices){
				if(triangle.a.x == vertex.getPosition().x && triangle.a.y == vertex.getPosition().z){
					face.setNextVertex(vertex);
				}
				if(triangle.b.x == vertex.getPosition().x && triangle.b.y == vertex.getPosition().z){
					face.setNextVertex(vertex);
				}
				if(triangle.c.x == vertex.getPosition().x && triangle.c.y == vertex.getPosition().z){
					face.setNextVertex(vertex);
				}
			}
			if(face.getVerticesSet() == 3){
				newfaces.add(face);
			}		
		}
		
		List<Vertex> formedVertices = new ArrayList<Vertex>();
		
		for(Face face : newfaces){
			face.setFrontFace();
			formedVertices.add(face.getVertex1());
			formedVertices.add(face.getVertex2());
			formedVertices.add(face.getVertex3());	
		}
		
		for(int i = 0;i<formedVertices.size();i++){
			indices.add(i);
		}
		
		for(Face face : newfaces){
			face.getVertex1().setNormalIndex(0);
			face.getVertex2().setNormalIndex(0);
			face.getVertex3().setNormalIndex(0);
			face.getVertex1().setTextureIndex(0);
			face.getVertex2().setTextureIndex(0);
			face.getVertex3().setTextureIndex(0);
		}
		
		float verticesArray[] = convertVerticesToArrays(formedVertices);
		float normalsArrayList[] = {0,1,0,0,-1,0};
		float normalsArray[] = convertNormalsToArrays(formedVertices, normalsArrayList);
		
		float texturesArray[] = {0,0};
		int indicesArray[] = convertIndicesListToArray(indices);
		
		ModelData data = new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, 1);
		
		RawModel rawModel = Loader.loadToVAO(data);
		
		TexturedModel texmodel = new PNGModel(null, rawModel, texture);
		
		Vector3f midpoint = Maths.getMidPoint(road.getStartingNode().getPosition(), road.getEndingNode().getPosition());
		//System.out.println(midpoint);
		
		
		road.setModel(texmodel);
		road.setPosition(midpoint);
		road.setRotation(new Vector3f(0,0,0));
		
		//GlobalVariables.entities.add(new Entity(texmodel, midpoint, new Vector3f(0,0,0), 1));
		
		return texmodel;
		
		/*for(Node node : road.getNodes()){
			if(node.getNextNode() != null && road.getEndingNode() != node){
				Vector3f midPos = Maths.getMidPoint(node.getPosition(), node.getNextNode().getPosition());
				Vector3f angle = Maths.getAngles(node.getPosition(), node.getNextNode().getPosition());

				angle.setY(-angle.getY()-90);
				
				Entity roadEntity = new Entity(model, midPos, angle, 1);

				GlobalVariables.entities.add(roadEntity);
				
				
				//GlobalVariables.entities.add(new Entity(marker, node.getNextNode().getPosition(), angle, 1));
			}
		}*/
	}
	
	public static void setRoadNode(Vector3f position, boolean curved){
		Node node = new Node(position, curved);

		nodes.add(node);
	}
	
	public static void createRoad(int index){
		Road segment = new Road(null, nodes);

		for(int i=0;i<segment.getNodes().size();i++){
			Node current = segment.getNodes().get(i);
			
			try{
				//if there is a node after this node
				if(segment.getNodes().get(i+1) != null){
					current.setNextNode(segment.getNodes().get(i+1));
				}
			}catch(Exception e){}		
			try{
				//if there is a node before this node
				if(segment.getNodes().get(i-1) != null){
					current.setPrevNode(segment.getNodes().get(i-1));
				}
			}catch(Exception e){}
		}
		
		ScenarioLoader.currentScenario.roads.add(index, segment);	
		RoadToObject.nodes.clear();
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
	
	private static List<Triangle2D> ridUselessFaces(List<Triangle2D> triangles){
		List<Triangle2D> newTriangles = new ArrayList<Triangle2D>();
		
		for(Triangle2D triangle : triangles){
			
	    	if(Maths.getAngleBFromTriangle(triangle) <= 90 || Double.isNaN(Maths.getAngleBFromTriangle(triangle))){
	    		newTriangles.add(triangle);
	    	}
	    }
		triangles.clear();
		
		return newTriangles;
	}
	
	private static List<Vertex> createVerticesFromNodes(Road road, TexturedModel model, TexturedModel model2){
		List<Vertex> vertices = new ArrayList<Vertex>();
		
		//Each node has two associated vertices
		for(Node node : road.getNodes()){
			//Cannot determine the two vertices for the first node as angle between another node is needed to calculate position of vertices
			//Starts at 2nd node
			if(node != road.getStartingNode()){
				Vector3f angleBetweenTwo = Maths.getAngles(node.getPrevNode().getPosition(), node.getPosition());
				
				System.out.println(angleBetweenTwo.getY());
				
				//If it is the 2nd node calculate 1st node vertices
				if(node == road.getNodes().get(1)){
					vertices.add(new Vertex(0, getPositionOfVertex1(node.getPrevNode().getPosition(), angleBetweenTwo, model)));
					vertices.add(new Vertex(1, getPositionOfVertex2(node.getPrevNode().getPosition(), angleBetweenTwo, model2)));
				}

				vertices.add(new Vertex(vertices.size(), getPositionOfVertex1(node.getPosition(), angleBetweenTwo, model)));
				vertices.add(new Vertex(vertices.size(), getPositionOfVertex2(node.getPosition(), angleBetweenTwo, model2)));							
			}
			Vector3f markerpos = new Vector3f(node.getPosition());
			markerpos.setY(1);
			ScenarioLoader.currentScenario.entities.add(new Entity(model, markerpos, new Vector3f(0,0,0), 1));
		}
		
		createCurvedRoadVertices(road, vertices, model);
			
		return vertices;
	}
	
	private static void createCurvedRoadVertices(Road road, List<Vertex> vertices, TexturedModel model) {
		// Checks for curves in road and adds vertices
		for (Node node : road.getNodes()) {
			if (node != road.getStartingNode() && node.getNextNode() != null) {
				if (node.isCurved() && node.getNextNode().isCurved()) {
					Vector3f angleBetweenTwo = Maths.getAngles(node.getPrevNode().getPosition(), node.getPosition());
					float radius = 10;

					Vector3f vertex1Pos = getPositionOfVertex1(node.getPosition(), new Vector3f(0, angleBetweenTwo.getY(), 0), model);
					Vector3f vertex2Pos = getPositionOfVertex2(node.getPosition(), new Vector3f(0, angleBetweenTwo.getY(), 0), model);

					// Vector3f circlePos =
					// Maths.getCenterOfCircle(node.getPosition(),
					// node.getNextNode().getPosition(), radius, -1);

					float arcAngle = Maths.getAngleFrom3Points(node.getPosition(), vertex1Pos, node.getNextNode().getPosition());
					float arcLength = Maths.getArcLength(radius, arcAngle);
					float angle = Maths.getAngleFromCoordsOnCircle(node.getNextNode().getPosition(), vertex1Pos);

					int points = (int) Math.floor((arcLength / 1));

					for (int k = 0; k <= points; k++) {
						float yaw = ((arcAngle / points) * k + angle);
						// Vector3f nodePoint =
						// Maths.getPointOnCircleCircumference(circlePos,
						// radius, yaw);

						//System.out.println("Curved " + yaw);

						// vertices.add(new Vertex(vertices.size(), ));
						// vertices.add(new Vertex(vertices.size(),
						// getPositionOfVertex2(nodePoint, new
						// Vector3f(0,yaw,0), model)));
					}

				}
			}

		}
	}
	
	private static Vector3f getPositionOfVertex1(Vector3f nodePos, Vector3f rotation, TexturedModel model){
		float yaw = rotation.getY()+90;
		float pitch = rotation.getZ();
		
		Vector2f pos = Maths.getPointOnCircleCircumference(Maths.toVector2f(nodePos), 4.05f, yaw);
		Vector3f vec3pos = new Vector3f(pos.getX(), 0, pos.getY());
		
		ScenarioLoader.currentScenario.entities.add(new Entity(model, new Vector3f(pos.getX(),0,pos.getY()), new Vector3f(0,0,0), 1));
		
		return vec3pos;
	}
	
	private static Vector3f getPositionOfVertex2(Vector3f nodePos, Vector3f rotation, TexturedModel model){
		float yaw = rotation.getY()-90;
		float pitch = rotation.getZ();
		
		Vector2f pos = Maths.getPointOnCircleCircumference(Maths.toVector2f(nodePos), 4.05f, yaw);
		Vector3f vec3pos = new Vector3f(pos.getX(), 0, pos.getY());
		
		ScenarioLoader.currentScenario.entities.add(new Entity(model, vec3pos, new Vector3f(0,0,0), 1));

		return vec3pos;
	}
}
