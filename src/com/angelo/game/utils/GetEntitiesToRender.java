package com.angelo.game.utils;

import java.util.ArrayList;
import java.util.List;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;

public class GetEntitiesToRender{

	public static List<Entity> getEntities(Camera camera, List<Entity> entities){
		List<Entity> entitiesForRendering = new ArrayList<Entity>();	
		
		for(Entity entity : entities){
			if(entity.getRenderable(camera)){
				entitiesForRendering.add(entity);		
			}
		}	
		return entitiesForRendering;
	}
	
}
