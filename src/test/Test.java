package test;

import cz.dat.oots.render.shader.ShaderProgram;
import cz.dat.oots.util.gl.FramebufferObject;
import cz.dat.oots.util.gl.Texture2D;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class Test {

    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        FramebufferObject fbo = new FramebufferObject(640, 480);

        Texture2D texComposite = new Texture2D(fbo, GL11.GL_RGBA8, GL11.GL_RGBA, GL11.GL_INT);
        Texture2D texLayer = new Texture2D(fbo, GL11.GL_RGBA8, GL11.GL_RGBA, GL11.GL_INT);
        Texture2D texDepth = new Texture2D(fbo, GL14.GL_DEPTH_COMPONENT24, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT);

        fbo.attachTexture(texComposite, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
        fbo.attachTexture(texLayer, EXTFramebufferObject.GL_COLOR_ATTACHMENT1_EXT);
        fbo.attachTexture(texDepth, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT);

        ShaderProgram sp = new ShaderProgram("cz/dat/oots/shaders/glass");

        //Clear - render - swap
        while (!Display.isCloseRequested()) {

            // <<< TO FBO >>>

            fbo.checkBind();
            GL11.glDrawBuffer(EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
            GL11.glClearColor(0, 0, 0, 1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

            double aspect = 640.0 / 480.0;
            double zNear = 0.05;
            double zFar = 200;
            double fH = Math.tan(90.0 / 360.0 * Math.PI) * zNear;
            double fW = fH * aspect;
            GL11.glFrustum(-fW, fW, -fH, fH, zNear, zFar);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            GL11.glTranslatef(0, 0, -10);

            GL11.glDisable(GL11.GL_TEXTURE_2D);

            // first quad

            GL11.glRotatef((float) (Math.sin(System.nanoTime() / 3000000000D) * 25f), 1, 0, 0);
            GL11.glRotatef((float) (Math.cos(System.nanoTime() / 6000000000D) * 25f), 0, 1, 0);

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(1, 0, 0);
            GL11.glVertex2f(-8, -8);
            GL11.glColor3f(0, 1, 0);
            GL11.glVertex2f(8, -8);
            GL11.glColor3f(0, 0, 1);
            GL11.glVertex2f(8, 8);
            GL11.glColor3f(1, 1, 1);
            GL11.glVertex2f(-8, 8);
            GL11.glEnd();

            GL11.glTranslatef(0, 0, 6);


            // second quad (with shader effects)
            GL11.glDrawBuffer(EXTFramebufferObject.GL_COLOR_ATTACHMENT1_EXT);
            GL11.glClearColor(0, 0, 0, 0);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL20.glUseProgram(sp.getProgramID());

            sp.setUniform1f("time", System.nanoTime() / 1000000000f);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texComposite.getId());

            float x = (320 - Mouse.getX()) / -50f;
            float y = (240 - Mouse.getY()) / -50f;

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x - 1, y - 1);
            GL11.glVertex2f(x + 1, y - 1);
            GL11.glVertex2f(x + 1, y + 1);
            GL11.glVertex2f(x - 1, y + 1);
            GL11.glEnd();

            GL20.glUseProgram(0);

            // <<< BACK TO NORMAL BACK BUFFER >>>

            FramebufferObject.bindNone();
            GL11.glClearColor(0, 0, 0, 1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();

            GL11.glOrtho(0, 1, 0, 1, -1, 1);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texComposite.getId());
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(1, 0);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(1, 1);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 1);
            GL11.glEnd();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texLayer.getId());
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(1, 0);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(1, 1);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 1);
            GL11.glEnd();

            Display.update();
        }
    }

}