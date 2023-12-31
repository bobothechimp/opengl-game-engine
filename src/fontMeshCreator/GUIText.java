package fontMeshCreator;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontRendering.TextMaster;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {
	
	private String textString;
	private float fontSize;
	
	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;
	
	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(0f, 0f, 0f);
	private Vector3f outlineColor = new Vector3f(1f, 1f, 1f);
	
	private Vector2f offset = new Vector2f(0f, 0f);
	
	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;
	
	private FontType font;
	
	private boolean centerText = false;
	
	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param width
	 * 			  - the width of the text's characters
	 * @param edge
	 * 			  - the width of the text's smooth edge
	 * @param borderWidth
	 * 			  - the width of the text's border
	 * @param borderEdge
	 * 			  - the width of the text's border's smooth edge
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, float width, float edge, float borderWidth, float borderEdge,
			FontType font, Vector2f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.width = width;
		this.edge = edge;
		this.borderWidth = borderWidth;
		this.borderEdge = borderEdge;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		TextMaster.loadText(this);
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		TextMaster.removeText(this);
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}
	
	/**
	 * Set the width of this text.
	 * 
	 * @param width - width of text
	 * 
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Get the width of this text.
	 * 
	 * @return The width of this text.
	 * 
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Set the width of this text's edge.
	 * 
	 * @param edge - width of text's edge
	 * 
	 */
	public void setEdge(float edge) {
		this.edge = edge;
	}
	
	/**
	 * Get the edge width of this text.
	 * 
	 * @return The edge width of this text.
	 * 
	 */
	public float getEdge() {
		return edge;
	}
	
	/**
	 * Set the width of this text's border.
	 * 
	 * @param borderWidth - width of text's border
	 * 
	 */
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	/**
	 * Get the width of this text's border.
	 * 
	 * @return The width of this text's border.
	 * 
	 */
	public float getBorderWidth() {
		return borderWidth;
	}
	
	/**
	 * Set the width of the edge of this text's border.
	 * 
	 * @param borderEdge - width of the edge of this text's border
	 * 
	 */
	public void setBorderEdge(float borderEdge) {
		this.borderEdge = borderEdge;
	}
	
	/**
	 * Get the edge width of this text's border.
	 * 
	 * @return The edge width of this text's border.
	 * 
	 */
	public float getBorderEdge() {
		return borderEdge;
	}
	
	/**
	 * Set the offset of this text's border.
	 * 
	 * @param offsetX - x component of offset
	 * 
	 * @param offsetY - y component of offset
	 * 
	 */
	public void setOffset(float offsetX, float offsetY) {
		offset.set(offsetX, offsetY);
	}
	
	/**
	 * Get the offset of this text.
	 * 
	 * @return The offset vector of this text.
	 * 
	 */
	public Vector2f getOffset() {
		return offset;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	/**
	 * @return the colour of the text.
	 */
	public Vector3f getColour() {
		return colour;
	}
	
	/**
	 * Set the outline color of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setOutlineColor(float r, float g, float b) {
		outlineColor.set(r, g, b);
	}
	
	/**
	 * @return the outline color of the text.
	 */
	public Vector3f getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	/**
	 * Sets the text of this GUI
	 * 
	 * @param text
	 */
	public void setTextString(String text) {
		this.textString = text;
		TextMaster.removeText(this);
		TextMaster.loadText(this);
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}

}
