package cz.dat.oots.util.gl;

import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture2D {

    private int id;
    private int width;
    private int height;

    /**
     * Creates empty texture (useful for FBO's)
     *
     * @param fbo parent FBO
     */
    public Texture2D(FramebufferObject fbo, int internalFormat, int format, int dataType) {
        this(fbo.getWidth(), fbo.getHeight(), internalFormat, format, dataType, (ByteBuffer) null);

        setWrapST(GL12.GL_CLAMP_TO_EDGE);
        setMinMagFilter(GL_LINEAR);
    }

    /**
     * Creates empty texture (useful for FBO's)
     *
     * @param width
     * @param height
     */
    public Texture2D(int width, int height, int internalFormat, int format, int dataType) {
        this(width, height, internalFormat, format, dataType, (ByteBuffer) null);
    }

    /**
     * Creates texture with specified data
     *
     * @param width
     * @param height
     */
    public Texture2D(int width, int height, int internalFormat, int format, int dataType, ByteBuffer data) {
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, dataType, data);
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public static void bindNone() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void setWrapST(int glCapWrap) {
        if (glGetInteger(GL_TEXTURE_BINDING_2D) != this.id) {
            glBindTexture(GL_TEXTURE_2D, this.id);
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, glCapWrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, glCapWrap);
    }

    public void setMinMagFilter(int glCapFilter) {
        if (glGetInteger(GL_TEXTURE_BINDING_2D) != this.id) {
            glBindTexture(GL_TEXTURE_2D, this.id);
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, glCapFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, glCapFilter);
    }

    public void dispose() {
        glDeleteTextures(this.id);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
