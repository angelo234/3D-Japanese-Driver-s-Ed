package com.angelo.game.utils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.entities.Camera;
import com.angelo.game.objConverter.Vertex;
import com.angelo.game.roads.Face;

import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.getX()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.getY()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.getZ()), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().getX()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().getY()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().getZ()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
	public static Matrix4f toLWJGLMatrix4f(javax.vecmath.Matrix4f matrix){
		Matrix4f newMatrix = new Matrix4f();	
		newMatrix.setIdentity();
		
		newMatrix.m00 = matrix.m11; //
		newMatrix.m01 = matrix.m12;
		newMatrix.m02 = matrix.m10;
		newMatrix.m03 = matrix.m30; //
		newMatrix.m10 = matrix.m21;
		newMatrix.m11 = matrix.m22; //
		newMatrix.m12 = matrix.m20; //
		newMatrix.m13 = matrix.m31; //
		newMatrix.m20 = matrix.m01; 
		newMatrix.m21 = matrix.m02; //
		newMatrix.m22 = matrix.m00; //
		newMatrix.m23 = matrix.m32; //
		newMatrix.m30 = matrix.m03; //
		newMatrix.m31 = matrix.m13; //
		newMatrix.m32 = matrix.m23; //
		newMatrix.m33 = matrix.m33; //
		
		/*
		newMatrix.m00 = matrix.m11; //
		newMatrix.m10 = matrix.m01;
		newMatrix.m20 = matrix.m21; //
		newMatrix.m30 = matrix.m03; // 
		newMatrix.m01 = matrix.m10;
		newMatrix.m11 = matrix.m00; 
		newMatrix.m21 = matrix.m20; //
		newMatrix.m31 = matrix.m13; //
		newMatrix.m02 = matrix.m13; 
		newMatrix.m12 = matrix.m21; //
		newMatrix.m22 = matrix.m00; 
		newMatrix.m32 = matrix.m23; //
		newMatrix.m03 = matrix.m30; 
		newMatrix.m13 = matrix.m31;
		newMatrix.m23 = matrix.m32;
		newMatrix.m33 = matrix.m33; //
		*/
		return newMatrix;
	}
	
	public static Matrix3f toLWJGLMatrix3f(javax.vecmath.Matrix3f matrix){
		Matrix3f newMatrix = new Matrix3f();	
		newMatrix.setIdentity();
		
		newMatrix.m00 = matrix.m00; //
		newMatrix.m01 = matrix.m01;
		newMatrix.m02 = matrix.m02;
		newMatrix.m10 = matrix.m10;
		newMatrix.m11 = matrix.m11; //
		newMatrix.m12 = matrix.m12; //
		newMatrix.m20 = matrix.m20; 
		newMatrix.m21 = matrix.m21; //
		newMatrix.m22 = matrix.m22; //

		return newMatrix;
	}
	
	public static Matrix4f toLWJGLMatrix4f(float[] matrix){
		Matrix4f newMatrix = new Matrix4f();	
		newMatrix.setIdentity();
		
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		
		matrixBuffer.clear();
		matrixBuffer.put(matrix);
		matrixBuffer.flip();
		
		newMatrix.load(matrixBuffer);
		
		return newMatrix;
	}
	
	public static String matrix4fToString(Matrix4f m) {

		StringBuilder buf = new StringBuilder();
		buf.append(m.m00).append(' ').append(m.m01).append(' ').append(m.m02).append(' ').append(m.m03).append('\n');
		buf.append(m.m10).append(' ').append(m.m11).append(' ').append(m.m12).append(' ').append(m.m13).append('\n');
		buf.append(m.m20).append(' ').append(m.m21).append(' ').append(m.m22).append(' ').append(m.m23).append('\n');
		buf.append(m.m30).append(' ').append(m.m31).append(' ').append(m.m32).append(' ').append(m.m33).append('\n');
		return buf.toString();
	}
	
	public static float getXYZDistance(float x1, float y1, float z1, float x2, float y2, float z2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(x2) - Math.abs(x1)), 2) + Math.pow((Math.abs(y2) - Math.abs(y1)), 2) + Math.pow((Math.abs(z2) - Math.abs(z1)), 2));				
		
		return distance;
		
	}
	
	public static int getIntXYZDistance(float x1, float y1, float z1, float x2, float y2, float z2){
		int distance = (int)Math.sqrt(Math.pow((Math.abs(x2) - Math.abs(x1)), 2) + Math.pow((Math.abs(y2) - Math.abs(y1)), 2) + Math.pow((Math.abs(z2) - Math.abs(z1)), 2));				
		
		return distance;
	}
	
	public static float getXYZDistance(Vector3f pos1, Vector3f pos2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(pos2.x) - Math.abs(pos1.x)), 2) + Math.pow((Math.abs(pos2.y) - Math.abs(pos1.y)), 2) + Math.pow((Math.abs(pos2.z) - Math.abs(pos1.z)), 2));				
		
		return distance;
	
	}
	
	public static float getXZDistance(Vector3f pos1, Vector3f pos2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(pos2.x) - Math.abs(pos1.x)), 2) + Math.pow((Math.abs(pos2.z) - Math.abs(pos1.z)), 2));				
		
		return distance;
	}
	
	public static float getXZDistance(float x1, float z1, float x2, float z2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(x2) - Math.abs(x1)), 2) + Math.pow((Math.abs(z2) - Math.abs(z1)), 2));				
		
		return distance;	
	}
	
	public static float getXZDistance(double x1, double z1, double x2, double z2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(x2) - Math.abs(x1)), 2) + Math.pow((Math.abs(z2) - Math.abs(z1)), 2));				
		
		return distance;	
	}
	
	public static float getYDistance(Vector3f pos1, Vector3f pos2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(pos2.y) - Math.abs(pos1.y)), 2));	
		
		return distance;
	}
	
	public static float getYDistance(float y1, float y2){
		float distance = (float)Math.sqrt(Math.pow((Math.abs(y2) - Math.abs(y1)), 2));	
		
		return distance;
	}
	
	public static float getDistanceFromObjective(Vector3f pos1, Vector3f pos2){
		float distance = (float)Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2) + Math.pow(pos2.z - pos1.z, 2));				
		return distance;
	
	}
	
	public static Vector3f getAngles(Vector3f a, Vector3f b){
		float xzDist = Maths.getXZDistance(a, b);
		float yDist = Maths.getYDistance(a.y, b.y);	
		
		float yaw = Maths.getAngleFromCoords(a.x, a.z, b.x, b.z);			
		float pitch = Maths.getPitchFromCoords(xzDist, yDist);
		float roll = 0;
		
		return new Vector3f(pitch, yaw, roll);
	}

	public static float getAngleFromCoords(float x1, float z1, float x2, float z2){
		float angle = (float) Math.toDegrees(Math.atan2((z2-z1),(x2-x1)));
		
		if(angle < 0){
	        angle += 360;
	    }
		if(angle > 360){
			angle -= 360;
		}
		 
	    return angle;
		
	}
	
	public static float getPitchFromCoords(float xzDistance, float yDistance){
		
		float angle = (float) Math.toDegrees(Math.atan(yDistance / xzDistance));
		
		if(angle < 0){
			angle += 360;
		}
		if(angle > 360){
			angle -= 360;
		}
		
		return angle;
	}	
	
	public static Vector3f getMidPoint(Vector3f pos1, Vector3f pos2){
		float x = (pos1.x+pos2.x)/2;
		float y = (pos1.y+pos2.y)/2;
		float z = (pos1.z+pos2.z)/2;
		
		return new Vector3f(x,y,z);
	}
	
	public static Vector3f getMidPoint(float x1, float y1, float z1, float x2, float y2, float z2){
		float x = (x1+x2)/2;
		float y = (y1+y2)/2;
		float z = (z1+z2)/2;
		
		return new Vector3f(x,y,z);
	}

	public static Vector2f getPointsOnLine(float x, float slope){
		float y = slope*x;
		
		return new Vector2f(x,y);
	}
	
	public static float normalize(float value){
		
		float min = 0;
		float max = 0;

		if (1 - value >= 0) {
			// 0 ~ 1
			max = 1;
		}
		else if (10 - value >= 1 && value < 10) {
			// 1 ~ 10
			max = 10;
		}
		else if (100 - value >= 10 && value < 100) {
			// 10 ~ 100
			max = 100;
		}
		else if (1000 - value >= 100 && value < 1000) {
			// 100 ~ 1000
			max = 1000;
		}
		else if (10000 - value >= 1000 && value < 10000) {
			// 1000 ~ 10000
			max = 10000;
		}
		else if (100000 - value >= 10000 && value < 100000) {
			// 10000 ~ 100000
			max = 100000;
		}
		else if (1000000 - value >= 100000 && value < 1000000) {
			// 100000 ~ 1000000
			max = 1000000;
		}
		
		float result = (value-Math.min(value, min))/(Math.max(value, max)-Math.min(value, min));
		
		return result;
	}
	
	public static float normalizeToRange(float min, float max, float value){
		float result = (value-Math.min(value, min))/(Math.max(value, max)-Math.min(value, min));
	
		return result;
	}
	
	/**Returns a Vector3f and a int index. Index 0 = Vector3f, Index 1 = minindex*/
	public static Object[] closetValue(Vector3f of, List<Vector3f> in, float error){
		Object[] datas = new Object[2];
		
		float min = 10;
		
		int index = 0;
		int minIndex = 0;
		
	    Vector3f closest = new Vector3f();

	    for (Vector3f v : in) {
	        
	    	float distance = Maths.getXYZDistance(of, v);
	    	
	    	if(distance < min){
	    		min = distance;
	    		closest = v;
	    		minIndex = index;
	    	}
	    	index++;
	    }
	    
	    datas[0] = closest;
	    datas[1] = minIndex;
	        
	    return datas;
	}
	
	public static List<Vector3f> getAllPointsOnLine(Vector3f startPos, Vector3f endPos, float minDistance){
		float dx = (endPos.x - startPos.x);
		float dy = (endPos.y - startPos.y);
		float dz = (endPos.z - startPos.z);

		int numPoints = (int) (Math.floor(Maths.getXYZDistance(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z) / minDistance) - 1);

		List<Vector3f> result = new ArrayList<Vector3f>();	

		float stepx = dx / numPoints;
	    float stepy = dy / numPoints;
	    float stepz = dz / numPoints;
	    float px = startPos.x + stepx;
	    float py = startPos.y + stepy;
	    float pz = startPos.z + stepz;
	    //float px = startPos.x;
	    //float py = startPos.y;
	    //float pz = startPos.z;
	    	   // System.out.println(numPoints);
	   
	    for (int ix = 0; ix < numPoints; ix++)
	    {    	
	        result.add(new Vector3f(px,py,pz));
	        px += stepx;
	        py += stepy;
	        pz += stepz;
	    }
		
	    return result;
	}
	
	public static List<Vector3f> getAllPointsOnLineForPath(Vector3f startPos, Vector3f endPos, float minDistance){
		float dx = (endPos.x - startPos.x);
		float dy = (endPos.y - startPos.y);
		float dz = (endPos.z - startPos.z);

		int numPoints = (int) (Math.floor(Maths.getXYZDistance(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z) / minDistance));

		List<Vector3f> result = new ArrayList<Vector3f>();	

		float stepx = dx / numPoints;
	    float stepy = dy / numPoints;
	    float stepz = dz / numPoints;
	    float px = startPos.x;
	    float py = startPos.y;
	    float pz = startPos.z;

	    for (int ix = 0; ix < numPoints+1; ix++)
	    {    
	    	//System.out.println(px+"_"+py+"_"+pz);
	        result.add(new Vector3f(px,py,pz));
	        px += stepx;
	        py += stepy;
	        pz += stepz;
	    }
		
	    return result;
	}
	
	public static Vector3f getCenterOfCircle(Vector3f pos1, Vector3f pos2, float radius, int circle){
		Vector3f center = new Vector3f();
		//y axis will not be used at the moment
		float x = 0;
		float z = 0;
		float x3 = 0;
		float z3 = 0;
		float q = 0;
		
		float x1 = pos1.getX();
		float z1 = pos1.getZ();
		float x2 = pos2.getX();
		float z2 = pos2.getZ();
		
		q = Maths.getXZDistance(x1, z1, x2, z2);
		x3 = (x1+x2)/2;
		z3 = (z1+z2)/2;
		
		if(circle == 1){
			x = x3 + (float) (Math.sqrt(Math.pow(radius, 2)-Math.pow((q/2), 2))*(z1-z2)/q);
			z = z3 + (float) (Math.sqrt(Math.pow(radius, 2)-Math.pow((q/2), 2))*(x2-x1)/q);
		}else if(circle == -1){
			x = x3 - (float) (Math.sqrt(Math.pow(radius, 2)-Math.pow((q/2), 2))*(z1-z2)/q);
			z = z3 - (float) (Math.sqrt(Math.pow(radius, 2)-Math.pow((q/2), 2))*(x2-x1)/q);
		}
		center.set(x, 0, z);
		
		return center;		
	}
	
	public static Vector2f getPointOnCircleCircumference(Vector2f origin, float radius, float angle){
		
		Vector2f pos = new Vector2f();
		
		pos.set((float)(origin.x + radius * Math.cos(Math.toRadians(angle))), (float)(origin.y + radius * Math.sin(Math.toRadians(angle))));
			
		return pos;	
	}
	
	/**Returns the angle from 3 points
	 * @param p1 first point
	 * @param p2 center point
	 * @param p3 second point
	 * */
	public static float getAngleFrom3Points(Vector3f p1, Vector3f p2, Vector3f p3){
		
		//float AB = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.z - p1.z, 2));
		//float BC = (float) Math.sqrt(Math.pow(p2.x - p3.x, 2) + Math.pow(p2.z - p3.z, 2));
		//float AC = (float) Math.sqrt(Math.pow(p3.x - p1.x, 2) + Math.pow(p3.z - p1.z, 2));
		//System.out.println(AB+"_"+BC+"_"+AC);
		//return (float) Math.acos(Math.toRadians((BC*BC+AB*AB-AC*AC)/(2*BC*AB)));
		
		float angle1 = (float) Math.atan2(p1.z - p2.z, p1.x - p2.x);
		float angle2 = (float) Math.atan2(p3.z - p2.z, p3.x - p2.x);
		
		if(Math.toDegrees(angle1 - angle2) < 0){
			return (float) Math.toDegrees(angle1 - angle2) + 360;
		}
		return (float) Math.toDegrees(angle1 - angle2);
		
	}
	
	public static float getArcLength(float radius, float angle){
		return (float) Math.toRadians(radius * angle);
	}
	
	public static float getAngleFromCoordsOnCircle(Vector3f pos, Vector3f centerPos){
		float angle = (float) Math.toDegrees(Math.atan2(pos.z - centerPos.z, pos.x - centerPos.x)); 
		
		if(angle < 0){
			return angle+=360;
		}
		return angle;
		
	}
	
	public static double getAverage(List<Double> values){
		
		int size = values.size();
		double totalSum = 0;	
		
		for(Double value : values){
			totalSum += value;
		}
		
		return totalSum / size;
		
	}
	
	/*public static List<Vector3f> getCurvedPath(Vector3f p0, Vector3f p1, Vector3f p2, float increment, float stopAt){
		
		List<Vector3f> points = new ArrayList<Vector3f>();
		
		for(float t = 0;t<stopAt;t+=increment){
			float x = (1-t)*((1-t)*p0.x+t*p1.x)+t*((1-t)*p1.x+t*p2.x); 
			float z = (1-t)*((1-t)*p0.z+t*p1.z)+t*((1-t)*p1.z+t*p2.z); 
			
			points.add(new Vector3f(x,0,z));
		}
				
		return points;
	}*/
	
	
	public static double getInchesFromCentimeters(double centimeters){
		return centimeters * 0.393701; 
	}
	
	public static boolean isEven(int number){
		if(number % 2 == 0){
			return true;
		}
		return false;
	}
	
	public static float roundUpNumber(float num, int places){
		return (float)(Math.round(num * Math.pow(10, places)) / Math.pow(10, places));	
	}
	
	public static float roundDownNumber(float num, int places){
		return (float)(Math.floor(num * Math.pow(10, places)) / Math.pow(10, places));	
	}
	
	public static double getAngleBFromTriangle(Triangle2D triangle){
		float ab = Maths.getXZDistance(triangle.a.x, triangle.a.y, triangle.b.x, triangle.b.y);
		float bc = Maths.getXZDistance(triangle.b.x, triangle.b.y, triangle.c.x, triangle.c.y);
		float ac = Maths.getXZDistance(triangle.a.x, triangle.a.y, triangle.c.x, triangle.c.y);
		
		return Math.toDegrees(Math.acos((Math.pow(bc, 2) + Math.pow(ab, 2) - Math.pow(ac, 2)) / (2 * bc * ab)));
		
	}
	
	public static List<Vector2D> convertVector3fList(List<Vector3f> vec3s){
		List<Vector2D> vec2s = new ArrayList<Vector2D>();
		
		for(Vector3f vec3 : vec3s){
			vec2s.add(new Vector2D(vec3.getX(), vec3.getZ()));
		}
		
		return vec2s;
	}
	
	public static List<Vector2D> convertVertexListToPositions(List<Vertex> vertexs){
		List<Vector2D> vec2s = new ArrayList<Vector2D>();
		
		for(Vertex vertex : vertexs){
			vec2s.add(new Vector2D(vertex.getPosition().getX(), vertex.getPosition().getZ()));
		}
		
		return vec2s;
	}
	
	public static boolean isFrontFacing(Face face){
		
		Vector3f sub1 = Vector3f.sub(face.getVertex2().getPosition(), face.getVertex3().getPosition(), null);
		Vector3f sub2 = Vector3f.sub(face.getVertex3().getPosition(), face.getVertex1().getPosition(), null);
		
		Vector3f calculated_normal = Vector3f.cross(sub1, sub2, null);
		
		if(Vector3f.dot(new Vector3f(0,1,0), calculated_normal) > 0){
			return true;
		}
		
		return false;
	}
	
	public static Vector2f toVector2f(Vector3f vec){
		return new Vector2f(vec.getX(), vec.getZ());
	}
	
	public static Vector3f toLWJGLVector3f(javax.vecmath.Vector3f vector){
		return new Vector3f(vector.x, vector.y, vector.z);
	}
	
	public static javax.vecmath.Vector3f toVecMathVector3f(Vector3f vector){
		return new javax.vecmath.Vector3f(vector.x, vector.y, vector.z);
	}
	
	public static javax.vecmath.Vector3f toVecMathRotationVector3f(Vector3f vector){
		return new javax.vecmath.Vector3f((float)Math.toDegrees(vector.x), (float)Math.toDegrees(vector.y), (float)Math.toDegrees(vector.z));
	}
	
	public static AxisAngle4f toAxisAngle(Vector3f eulerRotation){
		float c1 = (float) Math.cos(eulerRotation.getX() / 2);
		float s1 = (float) Math.sin(eulerRotation.getX() / 2);
		float c2 = (float) Math.cos(eulerRotation.getY() / 2);
		float s2 = (float) Math.sin(eulerRotation.getY() / 2);
		float c3 = (float) Math.cos(eulerRotation.getZ() / 2);
		float s3 = (float) Math.sin(eulerRotation.getZ() / 2);
		
		float w = (c1 * c2 * c3) - (s1 * s2 * s3);
		float x = (c1 * c2 * s3) + (s1 * s2 * c3);
		float y = (s1 * c2 * c3) + (c1 * s2 * s3);
		float z = (c1 * s2 * c3) - (s1 * c2 * s3);
		float angle = (float) (2 * Math.acos(w));
		float normal = (float) (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		
		if (normal < 0.001) { // when all euler angles are zero angle =0 so
			// we can set axis to anything to avoid divide by zero
			x=1;
			y=z=0;
		} else {
			normal = (float) Math.sqrt(normal);
	    	x /= normal;
	    	y /= normal;
	    	z /= normal;
		}
		return new AxisAngle4f(x, y, z, angle);
	}
	
	public static AxisAngle4f toAxisAngle(javax.vecmath.Vector3f eulerRotation){
		float c1 = (float) Math.cos(eulerRotation.x / 2);
		float s1 = (float) Math.sin(eulerRotation.x / 2);
		float c2 = (float) Math.cos(eulerRotation.y / 2);
		float s2 = (float) Math.sin(eulerRotation.y / 2);
		float c3 = (float) Math.cos(eulerRotation.z / 2);
		float s3 = (float) Math.sin(eulerRotation.z / 2);
		
		float w = (c1 * c2 * c3) - (s1 * s2 * s3);
		float x = (c1 * c2 * s3) + (s1 * s2 * c3);
		float y = (s1 * c2 * c3) + (c1 * s2 * s3);
		float z = (c1 * s2 * c3) - (s1 * c2 * s3);
		float angle = (float) (2 * Math.acos(w));
		float normal = (float) (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		
		if (normal < 0.001) { // when all euler angles are zero angle =0 so
			// we can set axis to anything to avoid divide by zero
			x=1;
			y=z=0;
		} else {
			normal = (float) Math.sqrt(normal);
	    	x /= normal;
	    	y /= normal;
	    	z /= normal;
		}
		return new AxisAngle4f(x, y, z, angle);
	}
	
	public static javax.vecmath.Vector3f toEulerForCar(Quat4f q){
		float pitch = (float) Math.toDegrees(Math.atan2(2.0 * (q.y * q.z + q.w * q.x), q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z));

		float yaw = (float) Math.toDegrees(Math.asin(-2.0 * (q.x * q.z - q.w * q.y)));

		float roll = (float) Math.toDegrees(Math.atan2(2.0 * (q.x * q.y + q.w * q.z), q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z));
		
		float newYaw = 0;
		
		if(roll < 90 && roll > -90){
			newYaw = (180 - yaw) % 360;
		}
		else{
			newYaw = (360 + yaw) % 360;
		}
		
		return new javax.vecmath.Vector3f(0,newYaw,0);
	}
	
	public static javax.vecmath.Vector3f toEuler(Quat4f q){
		float pitch = (float) Math.toDegrees(Math.atan2(2.0 * (q.y * q.z + q.w * q.x), q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z));

		float yaw = (float) Math.toDegrees(Math.asin(-2.0 * (q.x * q.z - q.w * q.y)));

		float roll = (float) Math.toDegrees(Math.atan2(2.0 * (q.x * q.y + q.w * q.z), q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z));
		
		return new javax.vecmath.Vector3f(pitch,yaw,roll);
	}


	public static javax.vecmath.Vector3f toEuler(Matrix3f basis) {
		float pitch = (float) Math.toDegrees(Math.atan2(-basis.m20, Math.sqrt(Math.pow(basis.m21, 2) + Math.pow(basis.m22, 2))));

		float yaw = (float) Math.toDegrees(Math.atan2(basis.m21, basis.m22));

		float roll = (float) Math.toDegrees(Math.atan2(basis.m10, basis.m00));
		
		return new javax.vecmath.Vector3f(pitch,yaw,roll);
	}

	public static List<Vector3f> generateBezierCurve(int lines, Vector3f a, Vector3f b, Vector3f c, Vector3f d){
		List<Vector3f> points = new ArrayList<Vector3f>();
		
		float x = 0;
		float y = 0;
		float z = 0;
		
		float chord = Vector3f.sub(d, a, null).length();
		
		float cont_net = Vector3f.sub(a, b, null).length() + Vector3f.sub(c, b, null).length() + Vector3f.sub(d, c, null).length();
		
		float app_arc_length = (cont_net + chord / 2);
		
		float stepSmoothness = app_arc_length / lines / app_arc_length;
		
		for(float t = 0; t < 1.05f; t += stepSmoothness){
			x = (float)(Math.pow((1 - t), 3) * a.x + 3 * Math.pow(1 - t, 2) * t * b.x + 3 * (1 - t) * Math.pow(t, 2) * c.x + Math.pow(t, 3) * d.x);
			y = (float)(Math.pow((1 - t), 3) * a.y + 3 * Math.pow(1 - t, 2) * t * b.y + 3 * (1 - t) * Math.pow(t, 2) * c.y + Math.pow(t, 3) * d.y);
			z = (float)(Math.pow((1 - t), 3) * a.z + 3 * Math.pow(1 - t, 2) * t * b.z + 3 * (1 - t) * Math.pow(t, 2) * c.z + Math.pow(t, 3) * d.z);
			
			points.add(new Vector3f(x, y, z));
		}
		
		return points;
	}
	
	/*public static Vector3f toEuler(AxisAngle4f axisAngle){
		float s=Math.sin(axisAngle.angle);
		float c=Math.cos(axisAngle.angle);
		float t=1-c;
		float heading = 0;
		float attitude = 0;
		float bank = 0;
		//  if axis is not already normalised then uncomment this
		// double magnitude = Math.sqrt(x*x + y*y + z*z);
		// if (magnitude==0) throw error;
		// x /= magnitude;
		// y /= magnitude;
		// z /= magnitude;
		if ((axisAngle.x*axisAngle.y*t + axisAngle.z*s) > 0.998) { // north pole singularity detected
			heading = 2*Math,atan2(axisAngle.x*Math.sin(axisAngle.angle/2),Math.cos(axisAngle.angle/2));
			attitude = Math.PI/2;
			bank = 0;
			return;
		}
		if ((axisAngle.x*axisAngle.y*t +axisAngle. z*s) < -0.998) { // south pole singularity detected
			heading = -2*atan2(axisAngle.x*Math.sin(axisAngle.angle/2),Math.cos(axisAngle.angle/2));
			attitude = -Math.PI/2;
			bank = 0;
			return;
		}
		heading = Math.atan2(y * s- x * z * t , 1 - (y*y+ z*z ) * t);
		attitude = Math.asin(x * y * t + z * s) ;
		bank = Math.atan2(x * s - y * z * t , 1 - (x*x + z*z) * t);
	}*/
}
