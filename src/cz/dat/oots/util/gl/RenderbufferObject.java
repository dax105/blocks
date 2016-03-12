package cz.dat.oots.util.gl;

import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class RenderbufferObject {

    private int id;
    private int width;
    private int height;

    public RenderbufferObject(int width, int height, int glCapStorageFormat) {
        int id = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, id);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, glCapStorageFormat, width, height);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);

        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void dispose() {
        glDeleteRenderbuffersEXT(this.id);
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
