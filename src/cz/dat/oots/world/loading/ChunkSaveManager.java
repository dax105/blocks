package cz.dat.oots.world.loading;

import cz.dat.oots.Game;
import cz.dat.oots.util.Coord2D;
import cz.dat.oots.world.World;
import cz.dat.oots.world.chunk.Chunk;
import cz.dat.oots.world.chunk.ChunkProvider;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ChunkSaveManager {

    private World world;
    private ChunkProvider provider;
    private String name;

    public ChunkSaveManager(ChunkProvider provider, String saveName) {
        this.provider = provider;
        this.world = provider.world;
        this.name = saveName;
    }

    public void tryToLoadWorld(Game game) {
        File dir = new File(WorldManager.SAVES_DIR, this.name);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File bdf = new File(dir, "bdf");
        File idf = new File(dir, "idf");
        this.world.createDataManagers(bdf, idf);

        File file = new File(dir, "world" + ".txt");

        if (!file.exists()) {
            game.getConsole().println("World save not found!");
            return;
        }

        WorldInfo i = game.getWorldsManager().getWorld(name);
        WorldUpdateResolver r = new WorldUpdateResolver(i);

        this.world.getPlayer().setPosition(i.getPlayerX(), i.getPlayerY(),
                i.getPlayerZ());
        this.world.getPlayer().setTilt(i.getPlayerTilt());
        this.world.getPlayer().setHeading(i.getPlayerHeading());
        this.provider.seed = i.getWorldSeed();

        try {
            if (r.resolveData(bdf, idf)) {
                this.world.createDataManagers(bdf, idf);
            }
        } catch (IOException e) {
            game.getConsole().println("Could not resolve data file problems.");
        }

        game.getConsole().println("World info sucessfully loaded!");
    }

    public boolean isChunkSaved(int cx, int cz) {
        File dir = new File(WorldManager.SAVES_DIR, this.name);

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, "x" + cx + "z" + cz + ".ccf");

        return file.exists();
    }

    public void saveAll(Game game) {
        game.displayLoadingScreen("Saving...");
        Iterator<Entry<Coord2D, Chunk>> it = provider.loadedChunks.entrySet()
                .iterator();
        while (it.hasNext()) {
            Entry<Coord2D, Chunk> pairs = it.next();
            Chunk c = (Chunk) pairs.getValue();

            c.deleteAllRenderChunks();

            this.saveChunk(c);
        }

        WorldInfo i = game.getWorldsManager().getWorld(this.name);
        if (i == null)
            i = new WorldInfo(this.name);

        i.setPlayerX(this.world.getPlayer().getPosX());
        i.setPlayerY(this.world.getPlayer().getPosY());
        i.setPlayerZ(this.world.getPlayer().getPosZ());
        i.setPlayerTilt(this.world.getPlayer().getTilt());
        i.setPlayerHeading(this.world.getPlayer().getHeading());
        i.setWorldSeed(this.provider.seed);
        i.setWorldVersion("" + WorldManager.CURRENT_VERSION);

        i.saveWorldInfo();

        try {
            this.world.getBlockDataManager().save();
        } catch (IOException e) {
            Logger.getGlobal().warning("Can't save data file!");
        }

        game.closeGuiScreen();
    }

    public Chunk loadChunk(int cx, int cz) {
        File dir = new File(WorldManager.SAVES_DIR, this.name);
        File file = new File(dir, "x" + cx + "z" + cz + ".ccf");
        byte[] fileData = new byte[(int) file.length()];
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Chunk c = new Chunk(cx, cz, this.world);
        try {
            c.blocksBuffer.put(Snappy.uncompressShortArray(fileData));
        } catch (IOException e) {
            e.printStackTrace();
        }

        c.changed = true;

        return c;
    }

    public void saveChunk(Chunk c) {
        if (!c.changed) {
            return;
        }

        try {
            File dir = new File(WorldManager.SAVES_DIR, this.name);

            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dir, "x" + c.x + "z" + c.z + ".ccf");
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(Snappy.compress(c.blocksBuffer.array()));
            } finally {
                stream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
