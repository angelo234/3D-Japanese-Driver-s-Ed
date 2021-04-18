package com.angelo.game.line;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.angelo.game.entities.Camera;
import com.angelo.game.renderEngine.MasterRenderer;

public class LineRenderer {

	private LineShader shader;
	
	public LineRenderer(LineShader shader,Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(List<Line> lines, Camera camera, Matrix4f toShadowMapSpace){		
		if(MasterRenderer.SHADOW_MAP){
			shader.loadToShadowSpaceMatrix(toShadowMapSpace);
		}
		for(Line line : lines){
			prepareModel(line);				
			MasterRenderer.disableCulling();
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, line.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindModel();
		}
	}
	
	private void prepareModel(Line line){
		LineModel lineModel = line.getModel();
		GL30.glBindVertexArray(lineModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadLineColor(line.getColor());
	}
	
	private void unbindModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
