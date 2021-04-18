package com.angelo.game.guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GUIRenderer {

	private GUIShader shader;
	
	public GUIRenderer(){
		shader = new GUIShader();
	}
	
	public void render(List<GUITexture> guis){
		shader.start();	
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		for(GUITexture gui: guis){
			if(gui != null){
				if(gui.getVisible()){
					GL30.glBindVertexArray(gui.getQuad().getVAOID());
					GL20.glEnableVertexAttribArray(0);	
					GL20.glEnableVertexAttribArray(1);
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, gui.getQuad().getVertexCount());
				}	
			}		
		}	
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
