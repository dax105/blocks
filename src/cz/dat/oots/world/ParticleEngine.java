package cz.dat.oots.world;

import cz.dat.oots.Particle;
import cz.dat.oots.render.ITickListener;
import cz.dat.oots.render.RenderEngine;
import cz.dat.oots.render.shader.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParticleEngine implements ITickListener {

    private static final int MAX_PARTICLES = 100000;
    private static final int BUFFER_SIZE = MAX_PARTICLES * 8;
    int key = 0;
    private List<Particle> particles;
    // private ParticleComparator sorter;
    private FloatBuffer drawBuffer;
    @SuppressWarnings("unused")
    private World world;
    private RenderEngine renderEngine;
    private int handle;
    private ShaderProgram shader;

    public ParticleEngine(World world) {
        this.world = world;
        this.renderEngine = world.getRenderEngine();
        this.particles = new LinkedList<Particle>();
        this.drawBuffer = BufferUtils.createFloatBuffer(BUFFER_SIZE);
        this.handle = GL15.glGenBuffers();

        this.shader = new ShaderProgram("cz/dat/oots/shaders/particles");
    }

    public static float packColor(int r, int g, int b, int a) {
        return Float.intBitsToFloat(((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0));
    }

    public void addParticle(Particle p) {
        //this.particleMap.put(key++, p);
        this.particles.add(p);
    }

    @Override
    public void onTick() {
        Iterator<Particle> it = this.particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            if (this.particles.size() > MAX_PARTICLES) {
                p.dead = true;
            } else {
                p.onTick();
            }
            if (p.dead) {
                it.remove();
            }
        }
    }

    public int getVertexCount() {
        return this.particles.size();
    }

    @Override
    public void onRenderTick(float partialTickTime) {
        if (Keyboard.isKeyDown(Keyboard.KEY_I)) {

            float[] rightModelviewVec = this.renderEngine
                    .getRightModelviewVec();
            float[] upModelviewVec = this.renderEngine.getUpModelviewVec();

            GL11.glBegin(GL11.GL_QUADS);
            Iterator<Particle> it = this.particles.iterator();
            while (it.hasNext()) {
                Particle p = it.next();

                //GL11.glColor4f(p.r, p.g, p.b, 1);

                float h = Particle.PARTICLE_SIZE / 2;
                float sizemultipler = (h / 1);

                float rightup0p = (rightModelviewVec[0] + upModelviewVec[0])
                        * sizemultipler;
                float rightup1p = (rightModelviewVec[1] + upModelviewVec[1])
                        * sizemultipler;
                float rightup2p = (rightModelviewVec[2] + upModelviewVec[2])
                        * sizemultipler;
                float rightup0n = (rightModelviewVec[0] - upModelviewVec[0])
                        * sizemultipler;
                float rightup1n = (rightModelviewVec[1] - upModelviewVec[1])
                        * sizemultipler;
                float rightup2n = (rightModelviewVec[2] - upModelviewVec[2])
                        * sizemultipler;

                float px = p.getPartialX(partialTickTime) - p.PARTICLE_SIZE / 2f;
                float py = p.getPartialY(partialTickTime);
                float pz = p.getPartialZ(partialTickTime) - p.PARTICLE_SIZE / 2f;

                GL11.glVertex3f(px - rightup0p, py - rightup1p, pz - rightup2p);
                GL11.glVertex3f(px + rightup0n, py + rightup1n, pz + rightup2n);
                GL11.glVertex3f(px + rightup0p, py + rightup1p, pz + rightup2p);
                GL11.glVertex3f(px - rightup0n, py - rightup1n, pz - rightup2n);
            }
            GL11.glEnd();
        } else {

            GL20.glUseProgram(shader.getProgramID());

            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            //GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

            this.drawBuffer.clear();
            Iterator<Particle> it = this.particles.iterator();
            while (it.hasNext()) {
                Particle p = it.next();

                float px = p.getPartialX(partialTickTime) - p.PARTICLE_SIZE / 2f;
                float py = p.getPartialY(partialTickTime);
                float pz = p.getPartialZ(partialTickTime) - p.PARTICLE_SIZE / 2f;

                this.drawBuffer.put(px).put(py).put(pz).put(p.color)/*.put(0).put(0)*/;

            }
            this.drawBuffer.flip();

            GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);

            shader.setUniform2f("screenSize", Display.getWidth(), Display.getHeight());
            shader.setUniform1f("spriteSize", Particle.PARTICLE_SIZE);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.drawBuffer, GL15.GL_STATIC_DRAW);

            int stride = 4 << 2;

            GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, 0);
            GL11.glColorPointer(3, GL11.GL_UNSIGNED_BYTE, stride, 3 << 2);
            //GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, 6 << 2);

            GL11.glDrawArrays(GL11.GL_POINTS, 0, this.particles.size());

            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            //GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

            GL11.glDisable(GL32.GL_PROGRAM_POINT_SIZE);

        }

    }

}
