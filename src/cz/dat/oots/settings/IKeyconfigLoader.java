package cz.dat.oots.settings;

import java.io.File;

public interface IKeyconfigLoader {
	public void load(File configFile);

	public void save(File configFile);
}
