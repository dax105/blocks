package dax.blocks.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import dax.blocks.Particle;
import dax.blocks.TextureManager;
import dax.blocks.block.Block;
import dax.blocks.console.CommandCullLock;
import dax.blocks.console.Console;
import dax.blocks.model.ModelManager;
import dax.blocks.movable.entity.PlayerEntity;
import dax.blocks.settings.Settings;
import dax.blocks.util.Coord2D;
import dax.blocks.util.GLHelper;
import dax.blocks.util.GameUtil;
import dax.blocks.world.ChunkDistanceComparator;
import dax.blocks.world.World;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.chunk.ChunkProvider;

public class RenderEngine {

	public static final String FLAG_LIGHTING = "lighting";
	public static final String FLAG_TEXTURE = "texture";
	public static final String FLAG_FOG = "fog";

	public static final String UNIFORM_TIME = "time";
	public static final String UNIFORM_FOG_DISTANCE = "fogDist";
	
	Frustum frustum;
	ChunkDistanceComparator chunkDistComp;
	float ptt;

	float[] rightModelviewVec;
	float[] upModelviewVec;

	public int chunksDrawn = 0;
	public int chunksLoaded = 0;
	int vertShader = 0, fragShader = 0;
	
	public boolean building = false;
	private boolean enableShaders;
	
	public int program = 0;
	public int blockAttributeID;
	
	private List<IWorldRenderer> renderables;
	private List<IWorldRenderer> renderablesToRemove;
	private List<IWorldRenderer> renderablesToAdd;

	public RenderEngine() {
		this(false);
	}

	public RenderEngine(boolean enableShaders) {
		this.enableShaders = enableShaders;
		this.frustum = new Frustum();
		this.rightModelviewVec = new float[3];
		this.upModelviewVec = new float[3];
		this.chunkDistComp = new ChunkDistanceComparator();
		this.renderables = new LinkedList<IWorldRenderer>();
		this.renderablesToAdd = new LinkedList<IWorldRenderer>();
		this.renderablesToRemove = new LinkedList<IWorldRenderer>();
		
		this.loadShaders();

		if (program == 0)
			return;

		if (enableShaders) {
			this.attachShaders();
		}
	}

	
	private void loadShaders() {
		try {
			this.vertShader = createShader("dax/blocks/shaders/screenN.vsh",
					ARBVertexShader.GL_VERTEX_SHADER_ARB);
			this.fragShader = createShader("dax/blocks/shaders/screenN.fsh",
					ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		} catch (Exception exc) {
			System.err.println("============================================\n"
					+ "=AN ERROR OCCURED WHILE LOADING THE SHADER!=\n"
					+ "============================================");

			exc.printStackTrace();

			System.err.println(getLogInfo(this.program));
		} finally {
			if (vertShader == 0 || fragShader == 0)
				return;
		}

		program = ARBShaderObjects.glCreateProgramObjectARB();
	}
	
	private void attachShaders() {
		/*
		 * if the vertex and fragment shaders setup sucessfully, attach them
		 * to the shader program, link the shader program (into the GL
		 * context I suppose), and validate
		 */
		ARBShaderObjects.glAttachObjectARB(this.program, this.vertShader);
		ARBShaderObjects.glAttachObjectARB(this.program, this.fragShader);

		ARBShaderObjects.glLinkProgramARB(this.program);
		if (ARBShaderObjects.glGetObjectParameteriARB(this.program,
				ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			System.err
					.println("============================================\n"
							+ "=AN ERROR OCCURED WHILE LOADING THE SHADER!=\n"
							+ "============================================");
			System.err.println(getLogInfo(this.program));
		}

		ARBShaderObjects.glValidateProgramARB(this.program);
		if (ARBShaderObjects.glGetObjectParameteriARB(this.program,
				ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			System.err
					.println("============================================\n"
							+ "=AN ERROR OCCURED WHILE LOADING THE SHADER!=\n"
							+ "============================================");
			System.err.println(getLogInfo(this.program));
		}

		blockAttributeID = GL20.glGetAttribLocation(this.program, "blockid");
		Console.println("Shader seems to be loaded!");
	}
	
	private int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if (shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader,
					GameUtil.readFileAsString(getClass().getClassLoader().getResourceAsStream(filename)));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader,
					ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: "
						+ getLogInfo(shader));

			return shader;
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}

	private String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects
				.glGetObjectParameteriARB(obj,
						ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	
	public void updateBeforeRendering(float ptt) {
		if (!CommandCullLock.locked)
			this.frustum.calculateFrustum();

		FloatBuffer modelviewMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrix);
		rightModelviewVec[0] = modelviewMatrix.get(0);
		upModelviewVec[0] = modelviewMatrix.get(1);
		rightModelviewVec[1] = modelviewMatrix.get(4);
		upModelviewVec[1] = modelviewMatrix.get(5);
		rightModelviewVec[2] = modelviewMatrix.get(8);
		upModelviewVec[2] = modelviewMatrix.get(9);
	}

	public void pushPlayerMatrix(PlayerEntity player) {
		GL11.glPushMatrix();
		GL11.glRotatef(-player.getTilt(), 1f, 0f, 0f);
		GL11.glRotatef(player.getHeading(), 0f, 1f, 0f);
		GL11.glTranslated(-player.getPosXPartial(), -player.getPosYPartial()
				- PlayerEntity.EYES_HEIGHT, -player.getPosZPartial());
	}


	public void sDisable(String flag) {
		int loc = GL20.glGetUniformLocation(program, flag);
		GL20.glUniform1f(loc, 0.0f);
	}

	public void sEnable(String flag) {
		int loc = GL20.glGetUniformLocation(program, flag);
		GL20.glUniform1f(loc, 1.0f);
	}

	public void sSetFloat(String flag, float value) {
		int loc = GL20.glGetUniformLocation(program, flag);
		GL20.glUniform1f(loc, value);
	}

	
	public void renderWorld(World world, float ptt) {
		chunksLoaded = 0;
		chunksDrawn = 0;

		GL11.glColor3f(1, 1, 1);

		if (enableShaders)
			ARBShaderObjects.glUseProgramObjectARB(program);

		sSetFloat(UNIFORM_TIME, System.nanoTime() / 1000000000f);
		sSetFloat(UNIFORM_FOG_DISTANCE,
				Settings.getInstance().drawDistance.getValue() * 16 - 8);

		this.ptt = ptt;
		pushPlayerMatrix(world.getPlayer());
		updateBeforeRendering(ptt);

		FloatBuffer lp = BufferUtils.createFloatBuffer(4);
		lp.put(-1000).put(1000).put(-1000).put(0).flip();

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lp);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);

		sDisable(FLAG_LIGHTING);
		sEnable(FLAG_TEXTURE);
		sEnable(FLAG_FOG);

		this.renderSkybox(world.getPlayer().getPosXPartial(), world.getPlayer()
				.getPosYPartial() + PlayerEntity.EYES_HEIGHT, world.getPlayer()
				.getPosZPartial());

		TextureManager.atlas.bind();

		sDisable(FLAG_TEXTURE);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		this.updateRenderables(ptt, world);

		sEnable(FLAG_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHTING);

		for (int i = 0; i < 1; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(i - 0.0625f, 49, 10);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glCallList(ModelManager.getInstance().getModel("char").getDisplayList());
			GL11.glPopMatrix();
		}


		// Render chunks
		this.renderChunks(ptt, world);

		if (Settings.getInstance().clouds.getValue()) {
			renderClouds(world.getPlayer().getPosXPartial(), world.getPlayer()
					.getPosZPartial());
		}

		this.renderSelectionBox(world);

		GL11.glPopMatrix();

		ARBShaderObjects.glUseProgramObjectARB(0);

	}
	
	public void renderChunks(float ptt, World world) {
		sEnable(FLAG_LIGHTING);
		sEnable(FLAG_TEXTURE);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		int pcx = (int) Math.floor(world.getPlayer().getPosX()) >> 4;
		int pcz = (int) Math.floor(world.getPlayer().getPosZ()) >> 4;

		List<Chunk> visibleChunks = world.getChunkProvider().getChunksInRadius(
				pcx, pcz, Settings.getInstance().drawDistance.getValue());

		for (Iterator<Chunk> iter = visibleChunks.iterator(); iter.hasNext();) {
			Chunk c = iter.next();
			if (c == null) {
				iter.remove();
			}
		}

		chunkDistComp.setFrontToBack();
		Collections.sort(visibleChunks, chunkDistComp);

		int generatedMeshes = 0;

		for (Chunk c : visibleChunks) {
			if (c != null) {
				for (int y = 0; y < 8; y++) {

					if (generatedMeshes >= Settings.getInstance().rebuildsPerFrame.getValue()) {
						building = true;
						break;
					} else {
						building = false;
					}

					if (!c.renderChunks[y].isBuilt()
							|| c.renderChunks[y].isDirty()) {
						ChunkProvider cp = world.getChunkProvider();
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

			if (generatedMeshes >= Settings.getInstance().rebuildsPerFrame.getValue()) {
				building = true;
				break;
			} else {
				building = false;
			}

		}

		List<RenderChunk> builtRenderChunks = new ArrayList<RenderChunk>();

		for (Chunk c : visibleChunks) {
			RenderChunk[] crcs = c.renderChunks;

			for (int y = 0; y < 8; y++) {
				if (crcs[y].isBuilt()) {
					builtRenderChunks.add(crcs[y]);
				}
			}

		}

		List<RenderChunk> culledRenderChunks = ChunkCull.cull(
				builtRenderChunks, this.frustum, Settings.getInstance().frustumCulling.getValue(), Settings.getInstance().advancedCulling.getValue());
		
		for (RenderChunk r : culledRenderChunks) {
			if (r.getCm().isPresent(RenderPass.OPAQUE)) {
				r.getCm().render(RenderPass.OPAQUE);
				chunksDrawn++;
			}
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		for (RenderChunk r : culledRenderChunks) {
			if (r.getCm().isPresent(RenderPass.TRANSPARENT)) {
				r.getCm().render(RenderPass.TRANSPARENT);
				chunksDrawn++;
			}
		}
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		if (Settings.getInstance().twoPassTranslucent.getValue()) {
			GL11.glColorMask(false, false, false, false);
			for (RenderChunk r : culledRenderChunks) {
				if (r.getCm().isPresent(RenderPass.TRANSLUCENT)) {
					r.getCm().render(RenderPass.TRANSLUCENT);
					chunksDrawn++;
				}
			}
			GL11.glColorMask(true, true, true, true);
		}

		for (RenderChunk r : culledRenderChunks) {
			if (r.getCm().isPresent(RenderPass.TRANSLUCENT)) {
				r.getCm().render(RenderPass.TRANSLUCENT);
				chunksDrawn++;
			}
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		sDisable(FLAG_LIGHTING);
		sDisable(FLAG_FOG);
	}
	
	public void renderSelectionBox(World world) {
		if (world.getPlayer().hasSelectedBlock()) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glLineWidth(2);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.25F);
			GLHelper.renderLinedBox(world.getPlayer().getLookingAtX() - 0.002f, world
					.getPlayer().getLookingAtY() - 0.002f, world.getPlayer()
					.getLookingAtZ() - 0.002f, world.getPlayer()
					.getLookingAtX() + 1 + 0.002f, world.getPlayer()
					.getLookingAtY() + 1 + 0.002f, world.getPlayer()
					.getLookingAtZ() + 1 + 0.002f);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glLineWidth(4);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
			GLHelper.renderLinedBox(world.getPlayer().getLookingAtX() - 0.002f, world
					.getPlayer().getLookingAtY() - 0.002f, world.getPlayer()
					.getLookingAtZ() - 0.002f, world.getPlayer()
					.getLookingAtX() + 1 + 0.002f, world.getPlayer()
					.getLookingAtY() + 1 + 0.002f, world.getPlayer()
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

	public void renderSky(float rot) {
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(0.9f, 0.9f, 1.0f);
		GL11.glVertex2f(0, 0);
		GL11.glColor3f(0.9f, 0.9f, 1.0f);
		GL11.glVertex2f(Display.getWidth(), 0);
		GL11.glColor3f(0.8f, 0.8f, 1.0f);
		GL11.glVertex2f(Display.getWidth(), Display.getHeight());
		GL11.glColor3f(0.8f, 0.8f, 1.0f);
		GL11.glVertex2f(0, Display.getHeight());
		GL11.glEnd();

		GL11.glPopAttrib();
		GL11.glPopMatrix();
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

	public void renderParticle(float ptt, Particle p) {
		GL11.glBegin(GL11.GL_QUADS);
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

		float px = p.getPartialX(ptt);
		float py = p.getPartialY(ptt);
		float pz = p.getPartialZ(ptt);

		GL11.glVertex3f(px - rightup0p, py - rightup1p, pz - rightup2p);
		GL11.glVertex3f(px + rightup0n, py + rightup1n, pz + rightup2n);
		GL11.glVertex3f(px + rightup0p, py + rightup1p, pz + rightup2p);
		GL11.glVertex3f(px - rightup0n, py - rightup1n, pz - rightup2n);
		GL11.glEnd();
	}


	public void registerNewRenderable(IWorldRenderer r) {
		if(!this.renderables.contains(r)) {
			this.renderablesToAdd.add(r);
		}
	}
	
	public void removeRenderable(IWorldRenderer r) {
		if(this.renderables.contains(r)) {
			this.renderablesToRemove.add(r);
		}
	}
	
	public void updateRenderables(float ptt, World world) {
		for(IWorldRenderer r : this.renderables) {
			r.renderWorld(ptt, world, this);
		}
		
		for (Iterator<IWorldRenderer> it = this.renderablesToAdd.iterator(); it
				.hasNext();) {
			this.renderables.add(it.next());
			it.remove();
		}
		
		for (Iterator<IWorldRenderer> it = this.renderablesToRemove.iterator(); it
				.hasNext();) {
			this.renderables.remove(it.next());
			it.remove();
		}
	}
}
