package com.angelo.game.physicsEngine;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.angelo.game.JBulletDebugDrawer;
import com.angelo.game.car.Car;
import com.angelo.game.entities.Entity;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.utils.Maths;
import com.angelo.game.utils.fileloaders.ScenarioLoader;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.SimpleBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.collision.shapes.TriangleMeshShape;
import com.bulletphysics.collision.shapes.TriangleShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsEngine {
	
	private static List<PhysicsObject> physicsObjects = new ArrayList<PhysicsObject>();

	public static final float GRAVITY = -9.81f;
	
	private static DiscreteDynamicsWorld dynamicWorld;
	private static CollisionDispatcher dispatcher;
	private static ConstraintSolver solver;
	private static DefaultCollisionConfiguration collisionConfiguration;
	
	public PhysicsEngine(){
		initWorldPhysics();
		initGround();
		
		//CollisionShape groundShape = new BoxShape(new Vector3f(0.5f,0.5f,0.5f));
		
		//PhysicsObject ground = new PhysicsObject(new Vector3f(0,100,0), new Vector3f(0,0,0), groundShape, 1f, 1, 0f);
	
		//physicsObjects.add(ground);
	}

	public void update(){			
		for(Entity entity : ScenarioLoader.currentScenario.entities){
			if(entity.getPhysicsObject() != null){
				PhysicsObject physics = entity.getPhysicsObject();
				
				entity.setPosition(Maths.toLWJGLVector3f(physics.getPosition()));
				entity.setRotation(Maths.toLWJGLVector3f(physics.getRotation()));
			}		
		}
		for(Car car : ScenarioLoader.currentScenario.cars){
			if(car.getCarPhysics() != null){
				CarPhysicsObject carPhysics = car.getCarPhysics();
				
				//System.out.println(carPhysics.getPosition().toString());
				//System.out.println(carPhysics.getRotation().toString());
				carPhysics.update();
				
				//car.setPosition(carPhysics.getPosition().x, carPhysics.getPosition().y, carPhysics.getPosition().z);
			}
		}
		
		dynamicWorld.stepSimulation(DisplayManager.getDeltaTime(), 60);
		dynamicWorld.debugDrawWorld();
	
	}
	
	private void initWorldPhysics(){
		// collision configuration contains default setup for memory, collision
		// setup. Advanced users can create their own configuration.
	    collisionConfiguration = new DefaultCollisionConfiguration();

		// use the default collision dispatcher. For parallel processing you
		// can use a diffent dispatcher (see Extras/BulletMultiThreaded)
	    dispatcher = new CollisionDispatcher(collisionConfiguration);

		// the maximum size of the collision world. Make sure objects stay
		// within these boundaries
		// Don't make the world AABB size too large, it will harm simulation
		// quality and performance
		Vector3f worldAabbMin = new Vector3f(-500, -500, -500);
		Vector3f worldAabbMax = new Vector3f(500, 500, 500);
		int maxProxies = 1024;
		AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		BroadphaseInterface overlappingPairCache2 = new DbvtBroadphase();

		// the default constraint solver. For parallel processing you can use a
		// different solver (see Extras/BulletMultiThreaded)
		solver = new SequentialImpulseConstraintSolver();
		
		dynamicWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache2, solver, collisionConfiguration);
		dynamicWorld.setGravity(new Vector3f(0,GRAVITY,0));
		dynamicWorld.getDispatchInfo().allowedCcdPenetration = 0f;	
		
		//JBulletDebugDrawer debugDrawer = new JBulletDebugDrawer();
		
		//debugDrawer.setDebugMode(com.bulletphysics.linearmath.DebugDrawModes.DRAW_AABB);
		
		//dynamicWorld.setDebugDrawer(debugDrawer);
	}
	
	private void initGround() {	
		//TriangleIndexVertexArray gTIVA = new TriangleIndexVertexArray();
		//CollisionShape groundShape = new BvhTriangleMeshShape(gTIVA, true);		
		
		float gridSizeX = 50;
		float gridSizeZ = 50;
		float sizeX = 500;
		float sizeZ = 500;
		
		CollisionShape groundShape = new BoxShape(new Vector3f(gridSizeX, 5f, gridSizeZ));	
		
		//Construct ground of 10 x 10 tiles (0,0) to (500,500)
		for(int x = 0; x < sizeX / gridSizeX; x++){
			for(int z = 0; z < sizeZ / gridSizeZ; z++){
				float newX = x * gridSizeX + (gridSizeX / 2);
				float newZ = z * gridSizeZ + (gridSizeZ / 2);
				
				PhysicsObject ground = new PhysicsObject(new Vector3f(newX,-5f,newZ), new Vector3f(0,0,0), groundShape, 0, 1f, 0f);
				
				PhysicsEngine.addPhysicsObject(ground);
			}
		}	
	}

	public static void addPhysicsObject(PhysicsObject colShape) {
		PhysicsEngine.physicsObjects.add(colShape);
	}

	public static DiscreteDynamicsWorld getDynamicWorld() {
		return dynamicWorld;
	}
	
}
