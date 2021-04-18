package com.angelo.game.audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

public class AudioMaster {
	
	private static List<Integer> buffers = new ArrayList<Integer>();
	
	public static void init(){
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static Vector3f getListenerData(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		
		AL10.alListener(AL10.AL_POSITION, buffer);
		
		return new Vector3f(buffer.get(0), buffer.get(1), buffer.get(2));
	}
	
	public static void setListenerData(Vector3f position, Vector3f rotation){
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		AL10.alListener3f(AL10.AL_ORIENTATION, rotation.x, rotation.y, rotation.z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static void setListenerData(float x, float y, float z, float rx, float ry, float rz){
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_ORIENTATION, rx, ry, rz);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static int loadSound(String file){
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);

		try {
			WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e){
			System.err.println("Could not use file "+file);
		}
				
		return buffer;
	}
	
	public static void cleanUp(){
		for(int buffer : buffers){
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
}
