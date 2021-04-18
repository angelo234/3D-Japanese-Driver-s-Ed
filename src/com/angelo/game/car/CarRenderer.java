package com.angelo.game.car;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.PlayerCamera;
import com.angelo.game.models.MTLModel;
import com.angelo.game.models.PNGModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.models.TexturedModel;
import com.angelo.game.objLoader.MTLMeshData;
import com.angelo.game.renderEngine.MasterRenderer;
import com.angelo.game.shaders.mtl.MTLStaticShader;
import com.angelo.game.textures.ModelTexture;
import com.angelo.game.utils.Maths;

public class CarRenderer {

	private MTLStaticShader mtlShader;
	
	public CarRenderer(MTLStaticShader mtlShader, Matrix4f projectionMatrix){
		this.mtlShader = mtlShader;
		
		mtlShader.start();
		mtlShader.loadProjectionMatrix(projectionMatrix);
		mtlShader.connectTextureUnits();
		mtlShader.stop();
	}

	public void render(Map<TexturedModel[], List<Car>> cars, Camera camera, Matrix4f toShadowMapSpace){
		
		if(MasterRenderer.SHADOW_MAP){
			mtlShader.loadToShadowSpaceMatrix(toShadowMapSpace);
		}
		
		for(TexturedModel[] models:cars.keySet()){
			List<Car> batch = cars.get(models);
			
			if(camera instanceof PlayerCamera){
				PlayerCamera playerCamera = (PlayerCamera) camera;
				
				if(playerCamera.getCameraMode() == Camera.FIRST_PERSON){
					MTLModel currentModel = (MTLModel) models[1];

					//Render interior						
					for(Car car:batch){
						if(currentModel instanceof MTLModel){
							for(int i = 0; i<currentModel.getMTLData().getMTLMeshDatas().size(); i++){
								int indicesArray[] = currentModel.getRawModel().getMeshesIndices().get(i);
								MTLMeshData mesh = currentModel.getMTLData().getMTLMeshDatas().get(i);
								
								prepareTexturedModel(currentModel, mesh, i);		
								prepareCarModelInstance(car);

								GL11.glDrawElements(GL11.GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
								
								unbindTexturedModel();
							}
						}				
					}
				}
				else if(playerCamera.getCameraMode() == Camera.THIRD_PERSON){
					MTLModel currentModel = (MTLModel) models[0];

					//Render car body
					for(Car car:batch){
						if(currentModel instanceof MTLModel){
							for(int i = 0; i<currentModel.getMTLData().getMTLMeshDatas().size(); i++){
								int indicesArray[] = currentModel.getRawModel().getMeshesIndices().get(i);
								MTLMeshData mesh = currentModel.getMTLData().getMTLMeshDatas().get(i);
								
								prepareTexturedModel(currentModel, mesh, i);		
								prepareCarModelInstance(car);

								GL11.glDrawElements(GL11.GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
								
								unbindTexturedModel();
							}
						}		
						
					}
				}
				else{
					MTLModel currentModel = (MTLModel) models[1];
					
					//Render car interior
					for(Car car:batch){
						if(currentModel instanceof MTLModel){
							for(int i = 0; i<currentModel.getMTLData().getMTLMeshDatas().size(); i++){
								int indicesArray[] = currentModel.getRawModel().getMeshesIndices().get(i);
								MTLMeshData mesh = currentModel.getMTLData().getMTLMeshDatas().get(i);
								
								prepareTexturedModel(currentModel, mesh, i);		
								prepareCarModelInstance(car);

								GL11.glDrawElements(GL11.GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
								
								unbindTexturedModel();
							}
						}							
					}
				}
			}
			else{
				MTLModel currentModel = (MTLModel) models[1];
				
				//Render car interior
				for(Car car:batch){
					if(currentModel instanceof MTLModel){
						for(int i = 0; i<currentModel.getMTLData().getMTLMeshDatas().size(); i++){
							int indicesArray[] = currentModel.getRawModel().getMeshesIndices().get(i);
							MTLMeshData mesh = currentModel.getMTLData().getMTLMeshDatas().get(i);
							
							prepareTexturedModel(currentModel, mesh, i);		
							prepareCarModelInstance(car);

							GL11.glDrawElements(GL11.GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
							
							unbindTexturedModel();
						}
					}							
				}
			}
		}
	}
	
	private void prepareTexturedModel(TexturedModel model, MTLMeshData mtlMeshData, int mesh){
		RawModel rawModel = model.getRawModel();

		if(model instanceof MTLModel){		
			GL30.glBindVertexArray(rawModel.getVAOIDFromArray(mesh));
			
			MTLModel mtlModel = (MTLModel) model;
			
			GL20.glEnableVertexAttribArray(0);
			//GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);

			mtlShader.loadMTLMeshData(mtlMeshData);
		}
		
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareCarModelInstance(Car car){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(car.getPosition(), car.getRotation(), 1);
		
		mtlShader.loadTransformationMatrix(car.getTransformationMatrix());
		//shader.loadOffset(car.getTextureXOffset(), car.getTextureYOffset());
	}
	
	
}
