package cz.dat.oots.inventory.renderer;

import cz.dat.oots.FontManager;
import cz.dat.oots.block.Block;
import cz.dat.oots.inventory.IObjectStack;
import cz.dat.oots.util.GLHelper;
import cz.dat.oots.world.World;

public class BasicBlockRenderer implements IObjectStackRenderer {

    public static final int DEFAULT_SIZE = 32;
    private IObjectStack stack;
    private int textureID;
    private String renderString;

    public BasicBlockRenderer(IObjectStack stack, Block i) {
        this.stack = stack;
        this.textureID = i.getSideTexture();
    }

    public void updateRenderString(int c) {
        this.renderString = "" + c;
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
        GLHelper.drawFromAtlas(this.textureID, x, x + width, y, y + height);
        FontManager.getFont().drawString(
                x + width - FontManager.getFont().getWidth(this.renderString)
                        - 2, y + height - FontManager.getFont().getHeight(),
                this.renderString);
    }

    @Override
    public void render(float ptt, int x, int y, World world) {
        this.render(ptt, x, y, BasicItemRenderer.DEFAULT_SIZE,
                BasicItemRenderer.DEFAULT_SIZE, world);
    }
}
