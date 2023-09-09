package particles;

public class ParticleTexture {
	
	private int textureID;
	private int numberOfRows;
	private boolean useAdditive;
	
	public ParticleTexture(int textureID, int numberOfRows, boolean useAdditive) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
		this.useAdditive = useAdditive;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public boolean isUseAdditive() {
		return useAdditive;
	}
	
}
