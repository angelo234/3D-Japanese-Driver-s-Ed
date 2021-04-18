package com.angelo.game;

import javax.vecmath.Vector3f;

import com.angelo.game.line.Line;
import com.angelo.game.line.LineDataLoader;
import com.angelo.game.utils.Maths;
import com.angelo.game.utils.fileloaders.ScenarioLoader;
import com.bulletphysics.linearmath.IDebugDraw;

public class JBulletDebugDrawer extends IDebugDraw{

	private int debugMode;

	@Override
	public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
		Line line = new Line(Maths.toLWJGLVector3f(from), Maths.toLWJGLVector3f(to), Maths.toLWJGLVector3f(color), 0.01f);
		LineDataLoader.loadLineModel(line);
		
		ScenarioLoader.currentScenario.lines.add(line);
		//System.out.println(ScenarioLoader.currentScenario.lines.size());
	}

	@Override
	public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color) {
		/*Vector3f vector = new Vector3f(PointOnB.x, PointOnB.y+0.1f, PointOnB.z);
		
		Line line = new Line(Maths.toLWJGLVector3f(PointOnB), Maths.toLWJGLVector3f(vector), Maths.toLWJGLVector3f(color), 0.1f);
		LineDataLoader.loadLineModel(line);
		
		ScenarioLoader.currentScenario.lines.add(line);*/
	}

	@Override
	public void reportErrorWarning(String warningString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw3dText(Vector3f location, String textString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebugMode(int debugMode) {
		// TODO Auto-generated method stub
		this.debugMode = debugMode;
	}

	@Override
	public int getDebugMode() {
		return debugMode;
	}

}
