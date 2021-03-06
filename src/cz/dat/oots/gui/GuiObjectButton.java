package cz.dat.oots.gui;

import cz.dat.oots.util.GLHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class GuiObjectButton extends GuiObject {

    public int id;
    public String text;
    protected GuiScreen parent;
    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;
    protected boolean hover = false;
    Color c1 = new Color(0x40202020);
    Color c2 = new Color(0x20FFFFFF);
    private Font font;
    //Color c3 = new Color(0xA0FFFFFF);

    public GuiObjectButton(int x1, int y1, int x2, int y2, Font font,
                           String text, int id, GuiScreen parent) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.text = text;
        this.id = id;
        this.font = font;
        this.parent = parent;
    }

    @Override
    public void render() {
        GLHelper.drawRectangle(c1, x1, x2, y1, y2);
        //GLHelper.drawRectangle(c2, x1 + 2, x2 - 2, y1 + 2, y2 - 2);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.font.drawString(x1 + (x2 - x1) / 2 - this.font.getWidth(text) / 2,
                y1 + (y2 - y1) / 2 - this.font.getHeight(text) / 2, this.text);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        if (this.hover) {
            GLHelper.drawRectangle(c2, x1, x2, y1, y2);
        }
    }

    @Override
    public void update() {
        if (Mouse.getX() >= x1 && Mouse.getX() <= x2
                && Display.getHeight() - Mouse.getY() >= y1
                && Display.getHeight() - Mouse.getY() <= y2) {
            this.hover = !Mouse.isButtonDown(0);
            while (Mouse.next()) {
                if (Mouse.getEventButtonState()) {
                    if (Mouse.getEventButton() == 0) {
                        this.onClick();
                    }
                }
            }
        } else {
            this.hover = false;
        }
    }

    protected void onClick() {
        this.parent.buttonPress(this);
    }
}
