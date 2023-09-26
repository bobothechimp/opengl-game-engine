package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private boolean show;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale, boolean show) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.show = show;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public boolean isShowing() {
		return show;
	}
	
	public void setShow(boolean show) {
		this.show = show;
	}
	
}
