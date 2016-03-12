package cz.dat.oots.util.gl;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGetInteger;

public class FramebufferObject {

    private int id;
    private int width;
    private int height;

    public FramebufferObject(int width, int height) {
        int id = glGenFramebuffersEXT();

        this.id = id;
        this.width = width;
        this.height = height;
    }

    public static void bindNone() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void attachTexture(Texture2D tex, int attachment) {
        checkBind();
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, attachment, GL_TEXTURE_2D, tex.getId(), 0);
    }

    public void attachRenderbufferObject(RenderbufferObject rbo, int attachment) {
        checkBind();
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, attachment, GL_RENDERBUFFER_EXT, rbo.getId());
    }

    public int checkStatus() {
        checkBind();
        return glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
    }

    public boolean isComplete() {
        return this.checkStatus() == GL_FRAMEBUFFER_COMPLETE_EXT;
    }

    public void checkBind() {
        if (glGetInteger(GL_FRAMEBUFFER_BINDING_EXT) != this.id) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.id);
        }
    }

    public void dispose() {
        glDeleteFramebuffersEXT(this.id);
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
