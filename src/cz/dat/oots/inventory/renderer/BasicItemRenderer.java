package cz.dat.oots.inventory.renderer;

import cz.dat.oots.inventory.IObjectStack;
import cz.dat.oots.item.Item;
import cz.dat.oots.util.GLHelper;
import cz.dat.oots.world.World;
import org.newdawn.slick.opengl.Texture;

public class BasicItemRenderer implements IObjectStackRenderer {
    public static final int DEFAULT_SIZE = 32;
    private IObjectStack stack;
    private Texture texture;

    public BasicItemRenderer(IObjectStack stack, Item i) {
        this.stack = stack;
        this.texture = i.getTexture();
    }

    @Override
    public IObjectStack getObjectStack() {
        return this.stack;
    }

    @Override
    public void preRender(float ptt, World world) {
    }

    @Override
    public void render(float ptt, int x, int y, int width, int height,
                       World world) {
        GLHelper.drawTexture(this.texture, x, x + width, y, y + height);
    }

    @Override
    public void render(float ptt, int x, int y, World world) {
        this.render(ptt, x, y, BasicItemRenderer.DEFAULT_SIZE,
                BasicItemRenderer.DEFAULT_SIZE, world);

    }

}
