package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {
	
	private static final float MAX_RUN_SPEED = 50;
	private static final float RUN_ACCEL = 0.5f;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain[][] terrains) {
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(dx, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), dz);
		Terrain currentTerrain = Terrain.getTerrain(terrains, super.getPosition().x, super.getPosition().z);
		float terrainHeight;
		if(currentTerrain == null) {
			terrainHeight = 0;
		} else {
			terrainHeight = currentTerrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		}
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = Math.min(MAX_RUN_SPEED, this.currentSpeed + RUN_ACCEL);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = Math.max(-MAX_RUN_SPEED, this.currentSpeed - RUN_ACCEL);
		} else if(!isInAir){
			this.currentSpeed *= 0.99;
			if(this.currentSpeed <= 0.1 && this.currentSpeed >= -0.1) this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
}
