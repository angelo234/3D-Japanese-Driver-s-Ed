package com.angelo.game.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import com.angelo.game.Main;

public class DisplayManager {
	
	private static final int FPS_CAP = 144;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createDisplay(int width, int height, String title){	
		ContextAttribs attribs = new ContextAttribs(3,3).withForwardCompatible(true).withProfileCore(true);
		
		try{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			Display.setTitle(title);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			Display.setVSyncEnabled(true);
		
		}catch (LWJGLException e){
			e.printStackTrace();
		}
		
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;	
	}
	
	public static float getDeltaTime(){
		return delta;
	}
	
	public static int getFPS(){
		return Main.framesCountAvg;
	}
	
	public static void destroyDisplay(){
		Display.destroy();
	}
	
	public static long getCurrentTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
		
	}
}
