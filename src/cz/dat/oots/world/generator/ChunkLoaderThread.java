package cz.dat.oots.world.generator;

import cz.dat.oots.util.Coord2D;
import cz.dat.oots.world.chunk.Chunk;
import cz.dat.oots.world.chunk.ChunkProvider;

public class ChunkLoaderThread implements Runnable {

    public boolean waiting;

    private ChunkProvider provider;
    private boolean running = true;

    public ChunkLoaderThread(ChunkProvider provider) {
        this.provider = provider;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (this.running) {
            Coord2D coord = provider.loadNeeded.poll();
            if (coord != null) {
                provider.loadingChunks.add(coord);
                Chunk c = provider.getChunk(coord.x, coord.y);
                if (c != null) {
                    provider.loaded.putIfAbsent(coord, c);
                }
                provider.loadingChunks.remove(coord);
            } else {
                this.pause();
            }
        }
    }

    public void pause() {
        if (!this.waiting) {
            this.waiting = true;
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resume() {
        if (this.waiting) {
            this.waiting = false;
            synchronized (this) {
                this.notify();
            }
        }
    }

}
