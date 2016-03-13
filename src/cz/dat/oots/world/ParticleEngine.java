package cz.dat.oots.world;

import cz.dat.oots.Particle;
import cz.dat.oots.render.ITickListener;
import cz.dat.oots.render.shader.ShaderProgram;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParticleEngine implements ITickListener {

	private static final int BYTES_PER_VERTEX = 16;

	private List<Particle> particles;

	private int handle;
	private ShaderProgram shader;
	
	private World world;

	public ParticleEngine(World world) {
		this.world = world;
		
		this.particles = new LinkedList<Particle>();

		this.handle = GL15.glGenBuffers();
		this.shader = new ShaderProgram("cz/dat/oots/shaders/particles");
	}

	public void addParticle(Particle p) {
		this.particles.add(p);
	}

	@Override
	public void onTick() {
		int max = this.world.getGame().s().maxParticles.getValue();
		Iterator<Particle> it = this.particles.iterator();
		while(it.hasNext()) {
			Particle p = it.next();
			if(this.particles.size() > max) {
				p.dead = true;
			} else {
				p.onTick();
			}
			if(p.dead) {
				it.remove();
			}
		}
	}

	@Override
	public void onRenderTick(float partialTickTime) {

		if(particles.size() > 0) {

			GL20.glUseProgram(shader.getProgramID());

			GL11.glDisable(GL11.GL_BLEND);

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);

			int bufferSize = this.particles.size() * BYTES_PER_VERTEX;
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferSize,
					GL15.GL_STREAM_DRAW);
			FloatBuffer mappedBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER,
					GL15.GL_WRITE_ONLY, bufferSize, null).asFloatBuffer();

			Iterator<Particle> it = this.particles.iterator();
			while(it.hasNext()) {
				Particle p = it.next();

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

			GL11.glDrawArrays(GL11.GL_POINTS, 0, this.particles.size());

			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			// GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL11.glDisable(GL32.GL_PROGRAM_POINT_SIZE);

			GL11.glEnable(GL11.GL_BLEND);
		}
	}

	public int getVertexCount() {
		return this.particles.size();
	}

	public static float packColor(int r, int g, int b, int a) {
		return Float.intBitsToFloat(((a & 0xFF) << 24) | ((r & 0xFF) << 16)
				| ((g & 0xFF) << 8) | ((b & 0xFF) << 0));
	}

}
