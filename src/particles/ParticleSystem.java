package particles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import toolbox.Maths;

public class ParticleSystem {
	
	private float pps;
	private float speedMean;
	private float speedVar;
	private float gravityComplient;
	private float lifeLength;
	private float rotationVar;
	private float scaleMean;
	private float scaleVar;
	
	private ParticleTexture texture;
	
	public ParticleSystem(ParticleTexture texture, float pps, float speed, float speedVar, float rotationVar,
			float scale, float scaleVar, float gravityComplient, float lifeLength) {
		this.texture = texture;
		this.pps = pps;
		this.speedMean = speed;
		this.speedVar = speedVar;
		this.rotationVar = rotationVar;
		this.scaleMean = scale;
		this.scaleVar = scaleVar;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
	}
	
	public void generateParticles(Vector3f systemCenter) {
		float delta = DisplayManager.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for(int i = 0; i < count; i++){
			emitParticle(systemCenter);
		}
		if(Math.random() < partialParticle){
			emitParticle(systemCenter);
		}
	}
	
	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		float randomSpeed = Maths.NormalDistribution(speedMean, speedVar);
		randomSpeed = Math.max(randomSpeed, 0);
		velocity.scale(randomSpeed);
		float randomRotation = Maths.NormalDistribution(0, rotationVar);
		float randomScale = Maths.NormalDistribution(scaleMean, scaleVar);
		randomScale = Math.max(randomScale, 0);
		new Particle(texture, new Vector3f(center), velocity, gravityComplient,
				lifeLength, randomRotation, randomScale);
	}
	
}