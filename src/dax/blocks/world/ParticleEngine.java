package dax.blocks.world;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import dax.blocks.Particle;
import dax.blocks.render.ITickListener;
import dax.blocks.render.RenderEngine;

public class ParticleEngine implements ITickListener {

	private static final int MAX_PARTICLES = 35000;
	private static final int BUFFER_SIZE = MAX_PARTICLES * 32;

	private List<Particle> particles;
	// private ParticleComparator sorter;
	private FloatBuffer drawBuffer;

	private World world;
	private RenderEngine renderEngine;
	private int handle;

	public ParticleEngine(World world) {
		this.world = world;
		this.renderEngine = world.getRenderEngine();
		this.particles = new LinkedList<Particle>();
		this.drawBuffer = BufferUtils.createFloatBuffer(BUFFER_SIZE);
		this.handle = GL15.glGenBuffers();
	}

	public void addParticle(Particle p) {
		this.particles.add(p);
	}

	@Override
	public void onTick() {
		Iterator<Particle> it = this.particles.iterator();
		while(it.hasNext()) {
			Particle p = it.next();
			if(this.particles.size() > MAX_PARTICLES) {
				p.dead = true;
			} else {
				p.onTick();
			}
			if(p.dead) {
				it.remove();
			}
		}
		System.out.println(particles.size());
	}

	@Override
	public void onRenderTick(float partialTickTime) {
		if(Keyboard.isKeyDown(Keyboard.KEY_I)) {
			
			float[] rightModelviewVec = this.renderEngine
					.getRightModelviewVec();
			float[] upModelviewVec = this.renderEngine.getUpModelviewVec();

			GL11.glBegin(GL11.GL_QUADS);
			Iterator<Particle> it = this.particles.iterator();
			while(it.hasNext()) {
				Particle p = it.next();

				GL11.glColor4f(p.r, p.g, p.b, 1);

				float h = Particle.PARTICLE_SIZE / 2;
				float sizemutipler = (h / 1);

				float rightup0p = (rightModelviewVec[0] + upModelviewVec[0])
						* sizemutipler;
				float rightup1p = (rightModelviewVec[1] + upModelviewVec[1])
						* sizemutipler;
				float rightup2p = (rightModelviewVec[2] + upModelviewVec[2])
						* sizemutipler;
				float rightup0n = (rightModelviewVec[0] - upModelviewVec[0])
						* sizemutipler;
				float rightup1n = (rightModelviewVec[1] - upModelviewVec[1])
						* sizemutipler;
				float rightup2n = (rightModelviewVec[2] - upModelviewVec[2])
						* sizemutipler;

				float px = p.getPartialX(partialTickTime);
				float py = p.getPartialY(partialTickTime);
				float pz = p.getPartialZ(partialTickTime);

				GL11.glVertex3f(px - rightup0p, py - rightup1p, pz - rightup2p);
				GL11.glVertex3f(px + rightup0n, py + rightup1n, pz + rightup2n);
				GL11.glVertex3f(px + rightup0p, py + rightup1p, pz + rightup2p);
				GL11.glVertex3f(px - rightup0n, py - rightup1n, pz - rightup2n);
			}
			GL11.glEnd();
		} else {

		
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			this.drawBuffer.clear();

			float[] rightModelviewVec = this.renderEngine
					.getRightModelviewVec();
			float[] upModelviewVec = this.renderEngine.getUpModelviewVec();

			// GL11.glBegin(GL11.GL_QUADS);
			Iterator<Particle> it = this.particles.iterator();
			while(it.hasNext()) {
				Particle p = it.next();

				// GL11.glColor4f(p.r, p.g, p.b, 1);

				float h = Particle.PARTICLE_SIZE / 2;
				float sizemutipler = (h / 1);

				float rightup0p = (rightModelviewVec[0] + upModelviewVec[0])
						* sizemutipler;
				float rightup1p = (rightModelviewVec[1] + upModelviewVec[1])
						* sizemutipler;
				float rightup2p = (rightModelviewVec[2] + upModelviewVec[2])
						* sizemutipler;
				float rightup0n = (rightModelviewVec[0] - upModelviewVec[0])
						* sizemutipler;
				float rightup1n = (rightModelviewVec[1] - upModelviewVec[1])
						* sizemutipler;
				float rightup2n = (rightModelviewVec[2] - upModelviewVec[2])
						* sizemutipler;

				float px = p.getPartialX(partialTickTime);
				float py = p.getPartialY(partialTickTime);
				float pz = p.getPartialZ(partialTickTime);

				// GL11.glVertex3f(px - rightup0p, py - rightup1p, pz -
				// rightup2p);
				// GL11.glVertex3f(px + rightup0n, py + rightup1n, pz +
				// rightup2n);
				// GL11.glVertex3f(px + rightup0p, py + rightup1p, pz +
				// rightup2p);
				// GL11.glVertex3f(px - rightup0n, py - rightup1n, pz -
				// rightup2n);

				this.drawBuffer.put(px - rightup0p).put(py - rightup1p)
						.put(pz - rightup2p).put(p.r).put(p.g).put(p.b).put(0)
						.put(0);
				this.drawBuffer.put(px + rightup0n).put(py + rightup1n)
						.put(pz + rightup2n).put(p.r).put(p.g).put(p.b).put(0)
						.put(0);
				this.drawBuffer.put(px + rightup0p).put(py + rightup1p)
						.put(pz + rightup2p).put(p.r).put(p.g).put(p.b).put(0)
						.put(0);
				this.drawBuffer.put(px - rightup0n).put(py - rightup1n)
						.put(pz - rightup2n).put(p.r).put(p.g).put(p.b).put(0)
						.put(0);

			}
			this.drawBuffer.flip();
			// GL11.glEnd();

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.drawBuffer,
					GL15.GL_STATIC_DRAW);

			int stride = 8 << 2;

			GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
			GL11.glColorPointer(3, GL11.GL_FLOAT, stride, 3 << 2);
			GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, 6 << 2);

			GL11.glDrawArrays(GL11.GL_QUADS, 0, this.particles.size() * 4);

			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		}

	}

}
