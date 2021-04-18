package com.angelo.game.car;

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
import com.angelo.game.textures.ModelTexture;

public class TireRenderer {

	private CarShader shader;
	
	public TireRenderer(CarShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(Map<TexturedModel[], List<Car>> cars, Camera camera, Matrix4f toShadowMapSpace){
		if(MasterRenderer.SHADOW_MAP){
			shader.loadToShadowSpaceMatrix(toShadowMapSpace);
		}
		
		//Render car tires
		for(TexturedModel[] models:cars.keySet()){
			List<Car> batch = cars.get(models);
			TexturedModel tireModel = models[2];	
			prepareTexturedModel(tireModel);
			for(Car car:batch){	
				for(int i = 0; i < 4; i++){
					prepareCarTireModelInstance(car, i);
					GL11.glDrawElements(GL11.GL_TRIANGLES, tireModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);		
				}			
			}
		}
			
		unbindTexturedModel();
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		
		if (model instanceof PNGModel) {
			GL30.glBindVertexArray(rawModel.getVAOID());
			
			PNGModel pngModel = (PNGModel) model;
			
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
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareCarTireModelInstance(Car car, int tire){
		shader.loadTransformationMatrix(car.getTireTransformationMatrix(tire));
	}
}
