package cz.dat.oots.render;

public interface IChunkRenderer {

    public void setAOIntensity(float value);

    public void begin();

    public void end();

    public int getVertexCount();

    public int getHandle();

    public void delete(int handle);

    public void render(int handle);

    public void vertex(float x, float y, float z);

    public void vertexWithAO(float x, float y, float z, boolean s1, boolean s2,
                             boolean c);

    public void vertexWithColoredAO(float x, float y, float z, float r,
                                    float g, float b, boolean s1, boolean s2, boolean c);

    public void color(float r, float b, float g);

    public void color(float r, float b, float g, float a);

    public void normal(float nx, float ny, float nz);

    public void texCoord(float s, float t);

    public void beforeRendering();

    public void afterRendering();

    public void beforeBuilding();

    public void afterBuilding();
}
