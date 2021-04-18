package com.angelo.game.scenario;

import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.line.Line;
import com.angelo.game.line.LineDataLoader;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

public class BoundsGenerator {

	public static void generateBounds(Scenario course){
		if(course.getCourseBounds() != null){
			//Construct lines of course box
			
			//Near X Left Line to Near X Right
			Line line = new Line(course.getCourseBounds().getNearXLeft(), course.getCourseBounds().getNearXRight(), new Vector3f(1,1,1), 0.1f);
			
			//Far X Left Line to Far X Right
			Line line2 = new Line(course.getCourseBounds().getFarXLeft(), course.getCourseBounds().getFarXRight(), new Vector3f(1,1,1), 0.1f);
			
			//Near X Left Line to Far X Left
			Line line3 = new Line(course.getCourseBounds().getNearXLeft(), course.getCourseBounds().getFarXLeft(), new Vector3f(1,1,1), 0.1f);
			
			//Near X Right Line to Far X Right
			Line line4 = new Line(course.getCourseBounds().getNearXRight(), course.getCourseBounds().getFarXRight(), new Vector3f(1,1,1), 0.1f);
			
			LineDataLoader.loadLineModel(line);
			LineDataLoader.loadLineModel(line2);
			LineDataLoader.loadLineModel(line3);
			LineDataLoader.loadLineModel(line4);
			
			ScenarioLoader.currentScenario.lines.add(line);
			ScenarioLoader.currentScenario.lines.add(line2);
			ScenarioLoader.currentScenario.lines.add(line3);
			ScenarioLoader.currentScenario.lines.add(line4);
		}		
	}
	
}
