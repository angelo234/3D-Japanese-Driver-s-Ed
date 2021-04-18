package com.angelo.game.shaders.mtl;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.entities.Entity;
import com.angelo.game.models.MTLModel;
import com.angelo.game.models.RawModel;
import com.angelo.game.objLoader.MTLMeshData;
import com.angelo.game.renderEngine.MasterRenderer;
import com.angelo.game.utils.Maths;

public class MTLEntityRenderer {

	private MTLStaticShader shader;
	
	public MTLEntityRenderer(MTLStaticShader shader,Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<MTLModel, List<Entity>> mtlEntities, Camera camera, Matrix4f toShadowMapSpace){
		if(MasterRenderer.SHADOW_MAP){
			shader.loadToShadowSpaceMatrix(toShadowMapSpace);
		}
		
		for(MTLModel model:mtlEntities.keySet()){		
			List<Entity> batch = mtlEntities.get(model);		
			RawModel rawModel = model.getRawModel();
			
			for(Entity entity:batch){	
				for(int i = 0; i<model.getMTLData().getMTLMeshDatas().size(); i++){
					int indicesArray[] = rawModel.getMeshesIndices().get(i);
					MTLMeshData mesh = model.getMTLData().getMTLMeshDatas().get(i);
					
					prepareMTLModel(rawModel, mesh, i);		
					prepareInstance(entity);

					GL11.glDrawElements(GL11.GL_TRIANGLES, indicesArray.length, GL11.GL_UNSIGNED_INT, 0);
					
					unbindMTLModel(rawModel.getVAOIDFromArray(i));
				}

			}
			
		}
	}
	
	private void prepareMTLModel(RawModel rawModel, MTLMeshData meshData, int meshNumber){	
		GL30.glBindVertexArray(rawModel.getVAOIDFromArray(meshNumber));
		
		GL20.glEnableVertexAttribArray(0);
		//GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		shader.loadMTLMeshData(meshData);
		
		//mtlData.getMTLLibrary().getMaterials().get(0).g
		/*shader.loadNumberOfRows(texture.getNumberOfRows());
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
		}*/
		
	}
	
	private void unbindMTLModel(int meshIndicesVBOID){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		//GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = null;
		
		transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
