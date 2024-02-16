package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;
import toolbox.Maths;

public class Vehicle extends Entity {
	
	private static final float MAX_SPEED = 250;
	private static final float ACCEL = 1f;
	private static final float DAMP = 0.999f;
	private static final float MAX_TURN_SPEED = 100;
	private static final float TURN_ACCEL = 10;
	public static final float GRAVITY = -50;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	
	private Entity driver = null;
	private Vector3f driverDisplacement = new Vector3f(0f, 15, 0f);
	private Vector3f dropoffDisplacement = new Vector3f(-8, -3, 0f);
	
	private static List<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	public Vehicle(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		vehicles.add(this);
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
	
	private void drift() {
		
	}
	
	private void checkInputs() {
		if(driver != null) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.currentSpeed = Math.min(MAX_SPEED, this.currentSpeed + ACCEL);
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.currentSpeed = Math.max(-MAX_SPEED, this.currentSpeed - ACCEL);
			} else if(!isInAir){
				this.currentSpeed *= DAMP;
				if(this.currentSpeed <= 0.1 && this.currentSpeed >= -0.1) this.currentSpeed = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.currentTurnSpeed = Math.min(MAX_TURN_SPEED, this.currentTurnSpeed + TURN_ACCEL);
			} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.currentTurnSpeed = Math.max(-MAX_TURN_SPEED, this.currentTurnSpeed - TURN_ACCEL);
			} else {
				this.currentTurnSpeed *= 0.99;
				if(this.currentTurnSpeed <= 0.1 && this.currentTurnSpeed >= -0.1) this.currentTurnSpeed = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				drift();
			}
		} else {
			if(!isInAir){
				this.currentSpeed *= DAMP;
				if(this.currentSpeed <= 0.1 && this.currentSpeed >= -0.1) this.currentSpeed = 0;
			}
			this.currentTurnSpeed *= 0.99;
			if(this.currentTurnSpeed <= 0.1 && this.currentTurnSpeed >= -0.1) this.currentTurnSpeed = 0;
		}
	}
	
	public static Vehicle closestVehicle(Entity target) {
		if(vehicles.isEmpty()) return null;
		Vehicle closest = vehicles.get(0);
		float closestDist = Maths.distance3d(target.getPosition(), closest.getPosition());
		for(int i = 1; i < vehicles.size(); i++) {
			float dist = Maths.distance3d(target.getPosition(), vehicles.get(i).getPosition());
			if(dist < closestDist) {
				closest = vehicles.get(i);
				closestDist = dist;
			}
		}
		return closest;
	}
	
	public void setDriver(Entity e) {
		driver = e;
	}
	
	public Vector3f getDriverDisplacement() {
		return driverDisplacement;
	}
	
	public Vector3f getDropOffDisplacement() {
		return dropoffDisplacement;
	}
}
