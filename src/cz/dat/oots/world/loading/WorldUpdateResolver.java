package cz.dat.oots.world.loading;

import java.io.File;
import java.io.IOException;

public class WorldUpdateResolver {
    private WorldInfo info;

    public WorldUpdateResolver(WorldInfo i) {
        this.info = i;
    }

    public boolean resolveData(File blockConfig, File itemConfig)
            throws IOException {
        if (this.isDifferent()) {
            if (this.info.getWorldVersion().equalsIgnoreCase("1")) {
                System.out
                        .println("Removing different Block and Item config files");

                if (blockConfig.exists()) {
                    blockConfig.delete();
                }

                if (itemConfig.exists()) {
                    itemConfig.delete();
                }

                return true;
            }
        }

        return false;
    }

    public WorldInfo getInfo() {
        return this.info;
    }

    public boolean isDifferent() {
        return !(this.info.getWorldVersion()
                .equalsIgnoreCase(WorldManager.CURRENT_VERSION));
    }
}
