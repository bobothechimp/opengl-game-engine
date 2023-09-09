package water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;

public class WaterTile {
	
	public static final float TILE_SIZE = 40;
	
	private float height;
	private float x,z;
	private Matrix4f transformationMatrix;
	
	public WaterTile(float centerX, float centerZ, float height){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.transformationMatrix = Maths.createTransformationMatrix(new Vector3f(x, height, z), 0, 0, 0, TILE_SIZE);
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}
	
}
