package cz.dat.oots.render;

import cz.dat.oots.Game;
import cz.dat.oots.TextureManager;
import cz.dat.oots.block.Block;
import cz.dat.oots.console.CommandCullLock;
import cz.dat.oots.movable.entity.PlayerEntity;
import cz.dat.oots.render.shader.ShaderProgram;
import cz.dat.oots.util.Coord2D;
import cz.dat.oots.util.GLHelper;
import cz.dat.oots.util.gl.FramebufferObject;
import cz.dat.oots.util.gl.Texture2D;
import cz.dat.oots.world.ChunkDistanceComparator;
import cz.dat.oots.world.World;
import cz.dat.oots.world.chunk.Chunk;
import cz.dat.oots.world.chunk.ChunkProvider;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.TextureImpl;

import java.nio.FloatBuffer;
import java.util.*;

public class RenderEngine {

    public static final String FLAG_LIGHTING = "lighting";
    public static final String FLAG_TEXTURE = "texture";
    public static final String FLAG_FOG = "fog";

    public static final String UNIFORM_TIME = "time";
    public static final String UNIFORM_FOG_DISTANCE = "fogDist";
    public int chunksDrawn = 0;
    public int chunksLoaded = 0;
    public int vertices = 0;
    public boolean building = false;
    private Frustum frustum;
    private ChunkDistanceComparator chunkDistComp;
    private float ptt;
    private float[] rightModelviewVec;
    private float[] upModelviewVec;
    private ShaderProgram worldShader = new ShaderProgram("cz/dat/oots/shaders/world");
    private ShaderProgram screenShader = new ShaderProgram("cz/dat/oots/shaders/screen");

    private List<IWorldRenderer> renderables;
    private List<IWorldRenderer> renderablesToRemove;
    private List<IWorldRenderer> renderablesToAdd;

    private World renderWorld;
    private Game game;

    private Map<String, FramebufferObject> fbos;
    private Texture2D screenTex;

    public RenderEngine(Game game) {
        this(game, false);
    }

    public RenderEngine(Game game, boolean enableShaders) {
        this.game = game;
        this.frustum = new Frustum();
        this.rightModelviewVec = new float[3];
        this.upModelviewVec = new float[3];
        this.chunkDistComp = new ChunkDistanceComparator();
        this.renderables = new LinkedList<IWorldRenderer>();
        this.renderablesToAdd = new LinkedList<IWorldRenderer>();
        this.renderablesToRemove = new LinkedList<IWorldRenderer>();

        this.fbos = new HashMap<String, FramebufferObject>();

        FramebufferObject screen = new FramebufferObject(Display.getWidth(), Display.getHeight());
        Texture2D ctex = new Texture2D(screen, GL11.GL_RGBA8, GL11.GL_RGBA, GL11.GL_INT);
        Texture2D dtex = new Texture2D(screen, GL14.GL_DEPTH_COMPONENT24, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT);

        //RenderbufferObject rbo = new RenderbufferObject(screen.getWidth(), screen.getHeight(), GL14.GL_DEPTH_COMPONENT24);

        screen.attachTexture(ctex, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
        screen.attachTexture(dtex, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT);
        //screen.attachRenderbufferObject(rbo, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT);

        System.out.println("Framebuffer status: " + screen.isComplete());

        this.fbos.put("screen", screen);
        this.screenTex = ctex;

        TextureImpl.bindNone();
        FramebufferObject.bindNone();

    }

    public void drawFullscreenTexture(Texture2D tex) {
        int w = Display.getWidth();
        int h = Display.getHeight();

        GLHelper.setOrtho(w, h);

        if (!Keyboard.isKeyDown(Keyboard.KEY_T))
            ARBShaderObjects.glUseProgramObjectARB(this.screenShader.getProgramID());

        this.screenShader.setUniform1f("time", System.nanoTime() / 1000000000f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getId());

        GL11.glColor3f(1, 1, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(w, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(w, h);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, h);
        GL11.glEnd();

        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public void setWorld(World world) {
        this.renderWorld = world;
    }

    public void updateBeforeRendering(float ptt) {
        if (!CommandCullLock.locked) {
            this.frustum.calculateFrustum();
        }

        FloatBuffer modelviewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrix);

        this.rightModelviewVec[0] = modelviewMatrix.get(0);
        this.upModelviewVec[0] = modelviewMatrix.get(1);
        this.rightModelviewVec[1] = modelviewMatrix.get(4);
        this.upModelviewVec[1] = modelviewMatrix.get(5);
        this.rightModelviewVec[2] = modelviewMatrix.get(8);
        this.upModelviewVec[2] = modelviewMatrix.get(9);
    }

    public void pushPlayerMatrix(PlayerEntity player) {
        GL11.glPushMatrix();
        GL11.glRotatef(-player.getTilt(), 1f, 0f, 0f);
        GL11.glRotatef(player.getHeading(), 0f, 1f, 0f);
        GL11.glTranslated(-player.getPosXPartial(), -player.getPosYPartial()
                - PlayerEntity.EYES_HEIGHT, -player.getPosZPartial());
    }

    public void renderWorld(float ptt) {
        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        //Texture2D.bindNone();
        this.fbos.get("screen").checkBind();

        GL11.glClearColor(1, 1, 1, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.chunksLoaded = 0;
        this.chunksDrawn = 0;

        GL11.glColor3f(1, 1, 1);

        ARBShaderObjects.glUseProgramObjectARB(this.worldShader.getProgramID());

        this.worldShader.setUniform1f("time", System.nanoTime() / 1000000000f);
        this.worldShader.setUniform1f("fogDist", this.game.getSettings().drawDistance.getValue() * 16 - 8);

        this.ptt = ptt;

        //GLHelper.setOrtho(Display.getWidth(), Display.getHeight());
        //this.renderSky(this.game.getWorldsManager().getWorld().getPlayer().getTilt());
        //GLHelper.setPerspective(Display.getWidth(), Display.getHeight());


        this.pushPlayerMatrix(this.renderWorld.getPlayer());
        this.updateBeforeRendering(this.ptt);

        FloatBuffer lp = BufferUtils.createFloatBuffer(4);
        lp.put(-1000).put(1000).put(-1000).put(0).flip();

        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lp);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        this.worldShader.setUniform1f(RenderEngine.FLAG_LIGHTING, 0);
        this.worldShader.setUniform1f(RenderEngine.FLAG_TEXTURE, 1);
        this.worldShader.setUniform1f(RenderEngine.FLAG_FOG, 1);

        this.renderSkybox(this.renderWorld.getPlayer().getPosXPartial(), this.renderWorld.getPlayer().getPosYPartial() + PlayerEntity.EYES_HEIGHT, this.renderWorld.getPlayer().getPosZPartial());

        TextureManager.atlas.bind();

        this.worldShader.setUniform1f(RenderEngine.FLAG_TEXTURE, 0);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        this.updateRenderables(ptt);

        this.renderWorld.getParticleEngine().onRenderTick(ptt);

        GL20.glUseProgram(this.worldShader.getProgramID());

        // Render chunks
        this.renderChunks(ptt);

        if (this.renderWorld.getGame().s().clouds.getValue()) {
            renderClouds(this.renderWorld.getPlayer().getPosXPartial(),
                    this.renderWorld.getPlayer().getPosZPartial());
        }

        this.worldShader.setUniform1f(RenderEngine.FLAG_TEXTURE, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.renderSelectionBox();

        GL11.glPopMatrix();

        ARBShaderObjects.glUseProgramObjectARB(0);

        FramebufferObject.bindNone();

        drawFullscreenTexture(this.screenTex);

    }

    public void renderChunks(float ptt) {

        this.worldShader.setUniform1f(RenderEngine.FLAG_LIGHTING, 1);
        this.worldShader.setUniform1f(RenderEngine.FLAG_TEXTURE, 1);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1, 1, 1, 1);

        TextureManager.atlas.bind();

        int pcx = (int) Math.floor(this.renderWorld.getPlayer().getPosX()) >> 4;
        int pcz = (int) Math.floor(this.renderWorld.getPlayer().getPosZ()) >> 4;

        List<Chunk> visibleChunks = this.renderWorld.getChunkProvider()
                .getChunksInRadius(pcx, pcz,
                        this.renderWorld.getGame().s().drawDistance.getValue());

        for (Iterator<Chunk> iter = visibleChunks.iterator(); iter.hasNext(); ) {
            Chunk c = iter.next();
            if (c == null) {
                iter.remove();
            }
        }

        this.chunkDistComp.setFrontToBack();
        Collections.sort(visibleChunks, this.chunkDistComp);

        int generatedMeshes = 0;

        IChunkRenderer chunkRenderer = this.game.chunkRenderer;

        chunkRenderer.beforeBuilding();

        for (Chunk c : visibleChunks) {
            if (c != null) {
                for (int y = 0; y < 8; y++) {

                    if (generatedMeshes >= this.renderWorld.getGame().s().rebuildsPerFrame
                            .getValue()) {
                        this.building = true;
                        break;
                    } else {
                        this.building = false;
                    }

                    if (!c.renderChunks[y].isBuilt()
                            || c.renderChunks[y].isDirty()) {
                        ChunkProvider cp = this.renderWorld.getChunkProvider();
                        if (cp.isChunkLoaded(new Coord2D(c.x + 1, c.z))
                                && cp.isChunkLoaded(new Coord2D(c.x - 1, c.z))
                                && cp.isChunkLoaded(new Coord2D(c.x, c.z + 1))
                                && cp.isChunkLoaded(new Coord2D(c.x, c.z - 1))) {
                            c.rebuild(y);
                            generatedMeshes++;
                        }
                    }
                }
            }

            if (generatedMeshes >= this.renderWorld.getGame().s().rebuildsPerFrame
                    .getValue()) {
                this.building = true;
                break;
            } else {
                this.building = false;
            }

        }

        chunkRenderer.afterBuilding();

        List<RenderChunk> builtRenderChunks = new LinkedList<RenderChunk>();

        for (Chunk c : visibleChunks) {
            RenderChunk[] crcs = c.renderChunks;

            for (int y = 0; y < 8; y++) {
                if (crcs[y].isBuilt()) {
                    builtRenderChunks.add(crcs[y]);
                }
            }

        }

        List<RenderChunk> culledRenderChunks = ChunkCull.cull(
                builtRenderChunks, this.frustum,
                this.renderWorld.getGame().s().frustumCulling.getValue(),
                this.renderWorld.getGame().s().advancedCulling.getValue());

        chunkRenderer.beforeRendering();

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        this.vertices = 0;

        for (RenderChunk r : culledRenderChunks) {
            this.vertices += r.getCm().getTotalVertices();

            if (r.getCm().isPresent(RenderPass.OPAQUE)) {
                r.getCm().render(RenderPass.OPAQUE);
                this.chunksDrawn++;
            }
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (this.renderWorld.getGame().s().twoPassTranslucent.getValue()) {
            GL11.glColorMask(false, false, false, false);
            for (RenderChunk r : culledRenderChunks) {
                if (r.getCm().isPresent(RenderPass.TRANSLUCENT)) {
                    r.getCm().render(RenderPass.TRANSLUCENT);
                    this.chunksDrawn++;
                }
            }
            GL11.glColorMask(true, true, true, true);
        }

        for (RenderChunk r : culledRenderChunks) {
            if (r.getCm().isPresent(RenderPass.TRANSLUCENT)) {
                r.getCm().render(RenderPass.TRANSLUCENT);
                this.chunksDrawn++;
            }
        }

        chunkRenderer.afterRendering();

        GL11.glDisable(GL11.GL_LIGHTING);
        this.worldShader.setUniform1f(RenderEngine.FLAG_LIGHTING, 0);
        this.worldShader.setUniform1f(RenderEngine.FLAG_FOG, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        TextureImpl.bindNone();

        GL11.glColor4f(1, 1, 1, 1);
    }

    public void renderSelectionBox() {
        if (this.renderWorld.getPlayer().hasSelectedBlock()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.25F);

            GLHelper.renderLinedBox(this.renderWorld.getPlayer()
                    .getLookingAtX() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtY() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtZ() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtX() + 1 + 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtY() + 1 + 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtZ() + 1 + 0.002f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glLineWidth(4);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);

            GLHelper.renderLinedBox(this.renderWorld.getPlayer()
                    .getLookingAtX() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtY() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtZ() - 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtX() + 1 + 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtY() + 1 + 0.002f, this.renderWorld.getPlayer()
                    .getLookingAtZ() + 1 + 0.002f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
        }
    }

    public void renderClouds(float playerX, float playerZ) {
        int size = 1024;

        int xOffset = (int) (playerX / (size * 2));
        int yOffset = (int) (playerZ / (size * 2));

        TextureManager.clouds.bind();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(1, 1, 1, 0.7f);

        for (int x = -1 + xOffset; x <= 1 + xOffset; x++) {
            for (int y = -1 + yOffset; y <= 1 + yOffset; y++) {

                GL11.glPushMatrix();
                GL11.glTranslatef(x * size * 2, 0, y * size * 2);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex3f(-size, 140.0f, -size); // Top Right Of The Quad
                // (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex3f(size, 140.0f, -size); // Top Left Of The Quad
                // (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex3f(size, 140.0f, size); // Bottom Left Of The Quad
                // (Top)
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex3f(-size, 140.0f, size); // Bottom Right Of The
                // Quad (Top)
                GL11.glEnd();
                GL11.glPopMatrix();
            }
        }

        TextureManager.atlas.bind();
    }

    public void renderSkybox(float x, float y, float z) {
        // Store the current matrix
        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, z);

        // Enable/Disable features
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        // Just in case we set all vertices to white.
        // GL11.glColor4f(1,1,1,1);
        // Render the front quad
        TextureManager.skybox_side.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        GL11.glEnd();
        // Render the left quad
        // TextureManager.skybox_left.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glEnd();
        // Render the back quad
        // TextureManager.skybox_back.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glEnd();
        // Render the right quad
        // TextureManager.skybox_right.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        GL11.glEnd();
        // Render the top quad
        TextureManager.skybox_top.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        GL11.glEnd();
        // Render the bottom quad
        TextureManager.skybox_bottom.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        GL11.glEnd();
        // Restore enable bits and matrix
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public void renderUnlockedBlock(float x, float y, float z, Block block) {
        // TODO Actually do something here
    }

    public void registerNewRenderable(IWorldRenderer r) {
        this.renderablesToAdd.add(r);
    }

    public void removeRenderable(IWorldRenderer r) {
        this.renderablesToRemove.add(r);
    }

    public void updateRenderables(float ptt) {
        for (IWorldRenderer r : this.renderables) {
            r.renderWorld(ptt, this.renderWorld, this);
        }

        for (Iterator<IWorldRenderer> it = this.renderablesToAdd.iterator(); it
                .hasNext(); ) {
            this.renderables.add(it.next());
            it.remove();
        }

        for (Iterator<IWorldRenderer> it = this.renderablesToRemove.iterator(); it
                .hasNext(); ) {
            this.renderables.remove(it.next());
            it.remove();
        }
    }

    public float[] getRightModelviewVec() {
        return rightModelviewVec;
    }

    public float[] getUpModelviewVec() {
        return upModelviewVec;
    }

    public void cleanup() {

    }
}
