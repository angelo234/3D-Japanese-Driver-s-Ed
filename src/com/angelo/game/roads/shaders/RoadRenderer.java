package com.angelo.game.roads.shaders;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.renderEngine.MasterRenderer;
import com.angelo.game.roads.Road;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.Maths;

public class RoadRenderer {

	private RoadShader shader;
	
	public RoadRenderer(RoadShader shader,Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<TexturedModel,List<Road>> roads, Camera camera){
		for(TexturedModel model:roads.keySet()){
			prepareTexturedModel(model);
			List<Road> batch = roads.get(model);
			
			for(Road road:batch){
				prepareInstance(road);
				GL11.glDrawElements(GL11.GL_TRIANGLES, road.getModel().getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);	
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();

		if(model instanceof PNGModel){
			PNGModel pngModel = (PNGModel) model;
			
			GL30.glBindVertexArray(rawModel.getVAOID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			ModelTexture texture = pngModel.getTexture();
			MasterRenderer.disableCulling();
			shader.loadFakeLightingVariable(texture.isUsingFakeLighting());
			shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
			
			shader.loadUseSpecularMap(texture.hasSpecularMap());
			
			if(texture.hasSpecularMap()){
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
			}
		}
		
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Road road){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(road.getPosition(), road.getRotation(), 1);

		shader.loadTransformationMatrix(transformationMatrix);
	}
}
