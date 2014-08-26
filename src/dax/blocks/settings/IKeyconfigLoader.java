package dax.blocks.settings;

import java.io.File;

public interface IKeyconfigLoader {
	public void load(File configFile);
	public void save(File configFile);
}
