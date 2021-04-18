package com.angelo.game.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.angelo.game.bloom.BrightFilter;
import com.angelo.game.bloom.CombineFilter;
import com.angelo.game.gaussianBlur.HorizontalBlur;
import com.angelo.game.gaussianBlur.VerticalBlur;
import com.angelo.game.models.RawModel;
import com.angelo.game.utils.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static BrightFilter brightFilter;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static CombineFilter combineFilter;

	public static void init(){
		quad = Loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		brightFilter = new BrightFilter(Display.getWidth()/2, Display.getHeight()/2);	
		hBlur = new HorizontalBlur(Display.getWidth()/5, Display.getHeight()/5);
		vBlur = new VerticalBlur(Display.getHeight()/5, Display.getHeight()/5);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colorTexture, int brightTexture){
		start();
		brightFilter.render(colorTexture);
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colorTexture, vBlur.getOutputTexture());	
		end();
	}
	
	public static void doPostProcessing(int colorTexture){
		start();
		contrastChanger.render(colorTexture);
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		brightFilter.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		combineFilter.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
