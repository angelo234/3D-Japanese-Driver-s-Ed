package com.angelo.game.audio;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class Source {

	private int sourceId;
	private int currentBuffer;
	
	public Source(){
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 6);
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 20);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 50);
	}
	
	public Source(int currentBuffer){
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 6);
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 20);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 50);
		
		this.currentBuffer = currentBuffer;
	}
	
	public void play(int buffer){
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		continuePlaying();
	}
	
	public void play(){
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, currentBuffer);
		continuePlaying();
	}
	
	public void delete(){
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	public void pause(){
		AL10.alSourcePause(sourceId);
	}
	
	public void continuePlaying(){
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop(){
		AL10.alSourceStop(sourceId);
	}
	
	public void setBuffer(int buffer){
		this.currentBuffer = buffer;
	}
	
	public void setLooping(boolean loop){
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying(){
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setVolume(float volume){
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public float getVolume(){
		return AL10.alGetSourcef(sourceId, AL10.AL_GAIN);
	}
	
	public void setPitch(float pitch){
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public float getPitch(){
		return AL10.alGetSourcef(sourceId, AL10.AL_PITCH);
	}
	
	public void setVelocity(Vector3f velocity){
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	public void setVelocity(float x, float y, float z){
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	public Vector3f getVelocity(){
		Vector3f vec3 = new Vector3f();		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		
		AL10.alGetSource(sourceId, AL10.AL_VELOCITY, buffer);
		vec3.set(buffer.get(0), buffer.get(1), buffer.get(2));
		
		return vec3;	
	}
	
	public void setPosition(Vector3f position){
		AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setPosition(float x, float y, float z){
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void increasePosition(float x, float y, float z){
		AL10.alSource3f(sourceId, AL10.AL_POSITION, this.getPosition().x+x, this.getPosition().y+y, this.getPosition().z+z);
	}
	
	public Vector3f getPosition(){
		Vector3f vec3 = new Vector3f();		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		
		AL10.alGetSource(sourceId, AL10.AL_POSITION, buffer);
		vec3.set(buffer.get(0), buffer.get(1), buffer.get(2));
		
		return vec3;	
	}
}
