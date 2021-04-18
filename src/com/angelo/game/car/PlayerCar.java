package com.angelo.game.car;

import org.lwjgl.input.Keyboard;

import com.angelo.game.audio.Source;
import com.angelo.game.physicsEngine.CarPhysicsObject;
import com.angelo.game.renderEngine.DisplayManager;
import com.angelo.game.utils.fileloaders.ScenarioLoader;

import joystick.JInputJoystick;

public class PlayerCar extends Car{

	private static final float STEER_ANGLE_PER_SECOND = 0.3f;
	private static final float STEER_ANGLE_BACK_PER_SECOND = 0.45f;
	
	private float gasPedalPower = 0;
	private float brakePedalPower = 0;
	
	private final float maxEnginePower = 3000;
	private final float maxBrakePower = 35;

	private boolean engineRunning;
	
	private boolean operateable;
	
	private float lastSteeringAngle;
	
	public PlayerCar(String carObjFileName, CarPhysicsObject carPhysics, Source engineSoundSource) {
		super(carObjFileName, carPhysics, engineSoundSource);
	}

	public void recieveInputing(JInputJoystick joystick){
		if(this.isOperateable()){
			if(joystick.isControllerConnected() && false){
				String os = System.getProperty("os.name");
				
				float gasPedalPos = 0;
				float brakePedalPos = 0;
				
				if(os.startsWith("Windows")){
					gasPedalPos = (100-joystick.getYAxisPercentage())/100.0f;
					brakePedalPos = (100-joystick.getZRotationPercentage())/100.0f; 
					
					//System.out.println(joystick.getZRotationPercentage());
				}
				else{
					gasPedalPos = (100-joystick.getZAxisPercentage())/100.0f;
					brakePedalPos = ((100-joystick.getZRotationPercentage())/100.0f);
				}
				
			
				//float brakePedalPos = 0;
				if(brakePedalPos > 0){
					gasPedalPos = 0;
				}
				
				float steering = joystick.getXAxisValue();
				
				gasPedalPower = gasPedalPos;
				brakePedalPower = brakePedalPos;
				
				if(brakePedalPower < 0.1) {
					brakePedalPower = 0;
				}
				//System.out.println(brakePedalPower);
				
				float steeringValue = -0.40f * steering;
				
				this.getCarPhysics().setSteering(steeringValue);
			}
			else{
				float gasPosPerSec = 0.50f;
				float brakePosPerSec = 0.75f;
				
				//Gas pedal
				if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
					brakePedalPower = 0;
					
					if(gasPedalPower < 1){
						gasPedalPower += gasPosPerSec * DisplayManager.getDeltaTime();
					}
					else{
						gasPedalPower = 1;
					}		
				}
				//Brake pedal
				else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					gasPedalPower = 0;	
					
					if(brakePedalPower < 1){
						brakePedalPower += brakePosPerSec * DisplayManager.getDeltaTime();
					}
					else{
						brakePedalPower = 1;
					}	
				}
				else{	
					setPedalsPowerZero();
				}
				
				//Handbrake
				if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
					this.handBrake();
				}	
				
				//Steer left
				if (Keyboard.isKeyDown(Keyboard.KEY_A)){
					
					//Change steering power depending on angle of steering wheel
					if(this.getSteering() < 0){
						this.steer(STEER_ANGLE_BACK_PER_SECOND);	
					}
					else{
						this.steer(STEER_ANGLE_PER_SECOND);	
					}
					
				}
				//Steer right
				else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
					
					//Change steering power depending on angle of steering wheel
					if(this.getSteering() > 0){
						this.steer(-STEER_ANGLE_BACK_PER_SECOND);
					}
					else{
						this.steer(-STEER_ANGLE_PER_SECOND);
					}
					
				}
				else{
					float steerAngle = this.getSteering();
					float degreePerSecond = 0.3f;
					
					if(steerAngle > 0){				
						this.steer(-degreePerSecond);
					}
					else if(steerAngle < 0){
						this.steer(degreePerSecond);
					}
					
					if(lastSteeringAngle > 0){
						if(this.getSteering() < 0){
							this.setSteering(0);
						}
					}
					else if(lastSteeringAngle < 0){
						if(this.getSteering() > 0){
							this.setSteering(0);
						}
					}							
				}
				lastSteeringAngle = this.getSteering();
			}
			
			
			
			if(this.isEngineRunning()){
				this.accelerate(gasPedalPower);
			}
			else{
				this.accelerate(0);
			}
			this.brake(maxBrakePower * brakePedalPower);				
					
		}
	}
	
	public void pollPressedKeyInput(){
		if(this.isOperateable()){
			int key = Keyboard.getEventKey();
			
			//Start/stop engine
			if(key == Keyboard.KEY_E){
				if(this.getCarPhysics().getGear() == 0){
					if(isEngineRunning()){
						engineRunning = false;
					}
					else{
						//System.out.println(AudioMaster.getListenerData());
						//System.out.println(source.getPosition());
						//System.out.println(source.getVolume());
						this.getEngineSoundSource().play(ScenarioLoader.currentScenario.audioBuffers.get("engineStart"));
						
						engineRunning = true;
					}
				}				
			}
			
			//Gears			
			if(this.isEngineRunning()){
				if(key == Keyboard.KEY_D){
					float lastGasPedalPower = this.gasPedalPower;
					
					this.gasPedalPower = 0;
					this.getCarPhysics().setGear(1);
					this.gasPedalPower = lastGasPedalPower;
				}
				else if(key == Keyboard.KEY_R){
					float lastGasPedalPower = this.gasPedalPower;
					
					this.gasPedalPower = 0;
					this.getCarPhysics().setGear(2);
					this.gasPedalPower = lastGasPedalPower;
				}
			}		
			if(key == Keyboard.KEY_P){
				float lastGasPedalPower = this.gasPedalPower;
				
				this.gasPedalPower = 0;
				this.getCarPhysics().setGear(0);
				this.gasPedalPower = lastGasPedalPower;
			}
		}		
	}
	
	private void setPedalsPowerZero(){
		this.gasPedalPower = 0;
		this.brakePedalPower = 0;
	}
	
	public int getGearPosition() {
		return this.getCarPhysics().getGear();
	}

	public void setGearPosition(int gear) {
		this.getCarPhysics().setGear(gear);
	}

	public boolean isEngineRunning(){
		return engineRunning;
	}
	
	public boolean isOperateable() {
		return operateable;
	}

	public void setOperateable(boolean operateable) {
		this.operateable = operateable;
	}
	
}
