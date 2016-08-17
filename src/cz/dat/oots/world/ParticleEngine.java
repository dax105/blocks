package cz.dat.oots.world;

import cz.dat.oots.Particle;
import cz.dat.oots.render.ITickListener;
import cz.dat.oots.render.shader.ShaderProgram;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;

public class ParticleEngine implements ITickListener {

	private static final int BYTES_PER_VERTEX = 16;

	private Particle[] particles;

	private int maxParticles;

	private int pCount;
	private int startPosition;

	private int handle;
	private ShaderProgram shader;

	public ParticleEngine(World world) {
		this.particles = new Particle[(this.maxParticles = world.getGame().s().maxParticles
				.getValue())];
		
		this.pCount = 0;
		this.startPosition = 0;

		this.handle = GL15.glGenBuffers();
		this.shader = new ShaderProgram("cz/dat/oots/shaders/particles");
	}

	public void addParticle(Particle p) {
		if(this.pCount < this.maxParticles) {
			this.pCount++;
		} else {
			this.startPosition++;
			this.startPosition %= this.maxParticles;
		}

		int putPosition = (this.startPosition + (this.pCount - 1)) % this.maxParticles;
		
		particles[putPosition] = p;
	}

	@Override
	public void onTick() {
		if(this.pCount > 0) {
			int endPosition = endPosition();
			int movePosition = this.startPosition;

			for(int currentPosition = this.startPosition; currentPosition != endPosition; currentPosition++) {
				currentPosition %= this.maxParticles;
				Particle p = particles[currentPosition];
				p.onTick();
				if(!p.dead) {
					this.particles[movePosition] = p;
					movePosition++;
					movePosition %= this.maxParticles;
				} else {
					this.pCount--;
				}
			}

		}
	}

	@Override
	public void onRenderTick(float partialTickTime) {
		if(this.pCount > 0) {
			int start = this.startPosition;
			int end = endPosition();

			GL20.glUseProgram(shader.getProgramID());

			GL11.glDisable(GL11.GL_BLEND);

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);

			int bufferSize = this.pCount * BYTES_PER_VERTEX;
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferSize,
					GL15.GL_STREAM_DRAW);
			FloatBuffer mappedBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER,
					GL15.GL_WRITE_ONLY, bufferSize, null).asFloatBuffer();

			for(int i = start; i != end; i++) {
				i %= this.maxParticles;
				Particle p = particles[i];

				float px = p.getPartialX(partialTickTime)
						- Particle.PARTICLE_SIZE / 2f;
				float py = p.getPartialY(partialTickTime);
				float pz = p.getPartialZ(partialTickTime)
						- Particle.PARTICLE_SIZE / 2f;

				mappedBuffer.put(px).put(py).put(pz).put(p.color);
			}

			mappedBuffer.flip();

			GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);

			GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);

			shader.setUniform2f("screenSize", Display.getWidth(),
					Display.getHeight());
			shader.setUniform1f("spriteSize", Particle.PARTICLE_SIZE);

			int stride = 4 << 2;

			GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
			GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, stride, 3 << 2);
			// GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, 6 << 2);

			GL11.glDrawArrays(GL11.GL_POINTS, 0, this.pCount);

			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			// GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL11.glDisable(GL32.GL_PROGRAM_POINT_SIZE);

			GL11.glEnable(GL11.GL_BLEND);
		}
	}

	public int getCount() {
		return this.pCount;
	}

	private int endPosition() {
		if(this.pCount == this.maxParticles) {
			return (this.startPosition - 1) == -1 ? this.maxParticles
					: (this.startPosition - 1);
		} else {
			return ((this.startPosition + this.pCount) % (this.maxParticles + 1));
		}
	}

	public static float packColor(int r, int g, int b, int a) {
		return Float.intBitsToFloat(((a & 0xFF) << 24) | ((r & 0xFF) << 16)
				| ((g & 0xFF) << 8) | ((b & 0xFF) << 0));
	}

}
