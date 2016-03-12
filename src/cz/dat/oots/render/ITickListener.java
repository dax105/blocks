package cz.dat.oots.render;

public interface ITickListener {
    public void onTick();

    public void onRenderTick(float partialTickTime);
}
