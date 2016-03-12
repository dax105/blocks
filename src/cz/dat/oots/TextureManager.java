package cz.dat.oots;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextureManager {

    public static final int ATLAS_SIZE = 8;
    public static float textureSizeCoord;
    public static float texSize;
    public static Texture atlas;
    public static Texture logo;
    public static Texture menuBg;
    public static Texture clouds;
    public static Texture skybox_bottom;
    public static Texture skybox_top;
    public static Texture skybox_side;
    public static Texture life_full;
    public static Texture life_zero;
    public static Texture imaginary_chocolate;
    private static Game game;

    public static Texture loadTex(String path) {
        try {
            Texture tex = TextureLoader.getTexture("PNG",
                    ResourceLoader.getResourceAsStream(path));
            if (TextureManager.game != null)
                TextureManager.game.getConsole().println(
                        "Successfully loaded texture from " + path);
            return tex;
        } catch (IOException e) {
            System.err.println("Can't load texture from " + path
                    + ", perhaps the file doesn't exist?");
            System.exit(1);
        }
        return null;
    }

    public static BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(ResourceLoader
                    .getResourceAsStream(path));
            TextureManager.game.getConsole().println(
                    "Successfully loaded texture as image from " + path);
            return image;
        } catch (IOException e) {
            System.err.println("Can't load texture as image from " + path
                    + ", perhaps the file doesn't exist?");
            System.exit(1);
        }
        return null;
    }

    public static void load(Game game) {
        TextureManager.game = game;
        TextureManager.logo = TextureManager.loadTex("cz/dat/oots/res/textures/logo.png");
        TextureManager.menuBg = TextureManager.loadTex("cz/dat/oots/res/textures/menubg.png");
        TextureManager.clouds = TextureManager.loadTex("cz/dat/oots/res/textures/clouds.png");

        TextureManager.skybox_bottom = TextureManager.loadTex("cz/dat/oots/res/textures/skybox_bottom.png");
        TextureManager.skybox_top = TextureManager.loadTex("cz/dat/oots/res/textures/skybox_top.png");
        TextureManager.skybox_side = TextureManager.loadTex("cz/dat/oots/res/textures/skybox_side.png");

        TextureManager.life_full = TextureManager.loadTex("cz/dat/oots/res/textures/life_full.png");
        TextureManager.life_zero = TextureManager.loadTex("cz/dat/oots/res/textures/life_null.png");
        TextureManager.imaginary_chocolate = TextureManager.loadTex("cz/dat/oots/res/textures/items/im_choc.png");

        TextureManager.skybox_bottom.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        TextureManager.skybox_top.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                GL12.GL_CLAMP_TO_EDGE);
        TextureManager.skybox_side.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                GL12.GL_CLAMP_TO_EDGE);

        TextureManager.clouds.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);

        AtlasBuilder ab = new AtlasBuilder(TextureManager.game);
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/stone.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/cobblestone_mossy.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/planks_oak.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/dirt.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/grass_top.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/grass_side.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/sand.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/log_oak.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/brick.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/glass.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/leaves.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/log_oak_top.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/bedrock.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/water.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/ice.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/tallgrass.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/flower_y.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/flower_r.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/flower_w.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/leaves_opaque.png"));
        ab.addTexture(TextureManager
                .loadImage("cz/dat/oots/res/textures/blocks/lava.png"));

        TextureManager.atlas = ab.buildAtlas(true);

        TextureManager.atlas.bind();

        TextureManager.textureSizeCoord = TextureManager.atlas.getWidth()
                / TextureManager.ATLAS_SIZE
                / (AtlasBuilder.EXPAND_RATIO * 2 + 1);
        TextureManager.texSize = TextureManager.atlas.getWidth();
    }

    public static float getX1(int texid) {
        return (texid % TextureManager.ATLAS_SIZE)
                * (TextureManager.texSize / TextureManager.ATLAS_SIZE)
                + TextureManager.textureSizeCoord
                / (1 / AtlasBuilder.EXPAND_RATIO);
    }

    public static float getX2(int texid) {
        return (texid % TextureManager.ATLAS_SIZE)
                * (TextureManager.texSize / TextureManager.ATLAS_SIZE)
                + TextureManager.textureSizeCoord
                + TextureManager.textureSizeCoord
                / (1 / AtlasBuilder.EXPAND_RATIO);
    }

    public static float getY1(int texid) {
        return (texid / TextureManager.ATLAS_SIZE)
                * (TextureManager.texSize / TextureManager.ATLAS_SIZE)
                + TextureManager.textureSizeCoord
                / (1 / AtlasBuilder.EXPAND_RATIO);
    }

    public static float getY2(int texid) {
        return (texid / TextureManager.ATLAS_SIZE)
                * (TextureManager.texSize / TextureManager.ATLAS_SIZE)
                + TextureManager.textureSizeCoord
                + TextureManager.textureSizeCoord
                / (1 / AtlasBuilder.EXPAND_RATIO);
    }
}
