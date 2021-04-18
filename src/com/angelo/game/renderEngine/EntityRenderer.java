package com.angelo.game.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;
import com.angelo.game.models.MTLModel;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.shaders.StaticShader;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.Maths;

public class EntityRenderer {

	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<TexturedModel,List<Entity>> entities, Camera camera, Matrix4f toShadowMapSpace){
		if(MasterRenderer.SHADOW_MAP){
			shader.loadToShadowSpaceMatrix(toShadowMapSpace);
		}
		
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){		
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);		
			}
			unbindTexturedModel();
			
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();

		if (model instanceof PNGModel) {
			PNGModel pngModel = (PNGModel) model;

			GL30.glBindVertexArray(rawModel.getVAOID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			ModelTexture texture = pngModel.getTexture();
			shader.loadNumberOfRows(texture.getNumberOfRows());
			if (texture.isTransparent()) {
				MasterRenderer.disableCulling();
			}
			shader.loadFakeLightingVariable(texture.isUsingFakeLighting());
			shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());

			shader.loadUseSpecularMap(texture.hasSpecularMap());

			if (texture.hasSpecularMap()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
			}
		}
		else if(model instanceof MTLModel){
			
		}
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		
		if(entity.getPhysicsObject() != null){
			shader.loadTransformationMatrix(entity.getPhysicsObject().getTransformationMatrix());
		}
		else{
			shader.loadTransformationMatrix(transformationMatrix);
		}
		
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
