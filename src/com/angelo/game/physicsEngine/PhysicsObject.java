package com.angelo.game.physicsEngine;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.utils.Maths;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;

public class PhysicsObject {

	private CollisionShape collisionShape;
	private RigidBody body;
	
	public PhysicsObject(Object position, Object rotation, CollisionShape colShape, float mass, float friction, float restitution){
	
		Vector3f newPosition = null;
		Vector3f newRotation = null;
		
		//If position or rotation object is an instance of org.lwjgl.util.vector.Vector3f, then convert into a javax.vecmath.Vector3f 
		if(position instanceof org.lwjgl.util.vector.Vector3f || rotation instanceof org.lwjgl.util.vector.Vector3f){
			newPosition = Maths.toVecMathVector3f((org.lwjgl.util.vector.Vector3f) position);
			newRotation = Maths.toVecMathVector3f((org.lwjgl.util.vector.Vector3f) rotation);
		}
		else{
			newPosition = (Vector3f) position;
			newRotation = (Vector3f) rotation;
		}
		
		// Create Dynamic Objects
		Transform transform = new Transform();
		transform.setIdentity();
		
		transform.origin.set(newPosition);
		MatrixUtil.setEulerZYX(transform.basis, (float)Math.toRadians(newRotation.x), (float)Math.toRadians(newRotation.y), (float)Math.toRadians(newRotation.z));
		
		//System.out.println(rotation);
		
		Vector3f localInertia = new Vector3f(0, 0, 0);
		// rigidbody is dynamic if and only if mass is non zero,
		// otherwise static
		
		if (mass != 0) {
			colShape.calculateLocalInertia(mass, localInertia);
		}
		
		// using motionstate is recommended, it provides
		// interpolation capabilities, and only synchronizes
		// 'active' objects
		DefaultMotionState motionState = new DefaultMotionState(transform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motionState, colShape, localInertia);

		body = new RigidBody(rbInfo);
		
		body.setCenterOfMassTransform(transform);		
		body.setFriction(friction);
		body.setRestitution(restitution);
		
		PhysicsEngine.getDynamicWorld().addRigidBody(body);
	}

	public CollisionShape getCollisionShape() {
		return collisionShape;
	}
	
	public RigidBody getRigidBody() {
		return body;
	}
	
	public Matrix4f getTransformationMatrix(){
		Transform transform = new Transform();
		this.getRigidBody().getMotionState().getWorldTransform(transform);
		
		float matrix[] = new float[16];
		
		transform.getOpenGLMatrix(matrix);
	
		return Maths.toLWJGLMatrix4f(matrix);	
	}
	
	public Vector3f getPosition(){
		Transform transform = new Transform();
		body.getWorldTransform(transform);
		
		return transform.origin;
	}
	
	public void setPosition(Vector3f vector){
		Transform transform = new Transform();
		
		body.getWorldTransform(transform);
		
		transform.origin.set(vector);
	}
	
	public void setPosition(float x, float y, float z){
		Transform transform = new Transform();
		
		body.getWorldTransform(transform);
		
		transform.origin.set(new Vector3f(x,y,z));
	}
	
	public Vector3f getRotation(){
		Transform transform = new Transform();
		body.getWorldTransform(transform);
		
		Quat4f quat = new Quat4f();
		MatrixUtil.getRotation(transform.basis, quat);
		
		return Maths.toEuler(quat);
	}
	
	public void setRotation(Vector3f vector){
		Transform transform = new Transform();
		
		body.getWorldTransform(transform);
		
		MatrixUtil.setEulerZYX(transform.basis, (float)Math.toRadians(vector.x), (float)Math.toRadians(vector.y), (float)Math.toRadians(vector.z));
	}
	
	public void setRotation(float x, float y, float z){
		Transform transform = new Transform();
		
		body.getWorldTransform(transform);
		
		MatrixUtil.setEulerZYX(transform.basis, x, y, z);
	}
}
