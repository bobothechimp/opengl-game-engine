package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 10;
	private float yaw = 0;
	private float roll;
	
	private Player player;
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private static final float MAX_ZOOM = 80;
	private static final float MIN_ZOOM = 15;
	
	private static final float MAX_PITCH = 90;
	private static final float MIN_PITCH = 0;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			resetCamera();
		}
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public void invertPitch() {
		pitch *= -1;
	}
	
	private void resetCamera() {
		pitch = 10;
		yaw = 0;
		angleAroundPlayer = 0;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float hDist, float vDist) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (hDist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (hDist * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vDist + 5;
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		distanceFromPlayer = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, distanceFromPlayer));
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, pitch));
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.1f;
			angleAroundPlayer -= angleChange;
		}
	}
}
