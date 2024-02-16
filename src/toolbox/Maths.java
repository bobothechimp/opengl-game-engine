package toolbox;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	
	private static final Random RANDOM = new Random();
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = new Vector3f(camera.getPosition());
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static Vector3f HSVtoRGB(Vector3f hsv) {
		float c = hsv.getZ() * hsv.getY();
		float x = (float) (c * (1 - Math.abs((hsv.getX() / 60.0) % 2 - 1)));
		float m = hsv.getZ() - c;
		
		float h = hsv.getX();
		if(0 <= h && h < 60) {
			return new Vector3f(255 * (c + m), 255 * (x + m), 255 * (0 + m));
		} else if(60 <= h && h < 120) {
			return new Vector3f(255 * (x + m), 255 * (c + m), 255 * (0 + m));
		} else if(120 <= h && h < 180) {
			return new Vector3f(255 * (0 + m), 255 * (c + m), 255 * (x + m));
		} else if(180 <= h && h < 240) {
			return new Vector3f(255 * (0 + m), 255 * (x + m), 255 * (c + m));
		} else if(240 <= h && h < 300) {
			return new Vector3f(255 * (x + m), 255 * (0 + m), 255 * (c + m));
		} else {
			return new Vector3f(255 * (c + m), 255 * (0 + m), 255 * (x + m));
		}
	}
	
	public static float NormalDistribution(float mean, float var) {
		return (float) (RANDOM.nextGaussian() * Math.sqrt(var) + mean);
	}
	
	public static float distance3d(Vector3f p1, Vector3f p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;
		float dz = p1.z - p2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
				
	}
}
