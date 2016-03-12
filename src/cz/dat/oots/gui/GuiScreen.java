package cz.dat.oots.gui;

import cz.dat.oots.FontManager;
import cz.dat.oots.Game;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Font;

import java.util.ArrayList;

public abstract class GuiScreen {

    protected Game game;
    protected GuiScreen parent;
    protected Font f;
    protected ArrayList<GuiObject> objects = new ArrayList<GuiObject>();

    public GuiScreen(Game game) {
        if (game.getWorldsManager().isInGame()) {
            this.objects.add(new GuiObjectRectangle(0, 0, Display.getWidth(),
                    Display.getHeight(), 0xF0000000));
        }
        this.game = game;
        this.parent = null;
        this.f = FontManager.getFont();
    }

    public GuiScreen(GuiScreen parent) {
        this(parent.game);
        this.parent = parent;
    }

    public void render() {
        for (GuiObject object : this.objects) {
            object.render();
        }
    }

    public void update() {
        for (GuiObject object : this.objects) {
            object.update();
        }

        while (Mouse.next()) {
            ;
        }
    }

    public void close() {
        if (this.parent != null) {
            this.game.openGuiScreen(this.parent);
        } else {
            this.game.closeGuiScreen();
        }
    }

    public abstract void onOpening();

    public abstract void onClosing();

    public abstract void buttonPress(GuiObjectButton button);

    public abstract void sliderUpdate(GuiObjectSlider slider);

    public abstract void buttonChanged(GuiObjectChangingButton button, int line);
}
