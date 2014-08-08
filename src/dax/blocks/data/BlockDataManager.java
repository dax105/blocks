package dax.blocks.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.UnsupportedDataTypeException;

import dax.blocks.block.Block;
import dax.blocks.util.Coord3D;
import dax.blocks.world.World;

public class BlockDataManager implements IBlockDataManager {

	private File data;
	private Map<Coord3D, IDataObject> values;
	private World world;

	public BlockDataManager(File dataFile, World world) throws IOException {
		this.data = dataFile;
		this.values = new HashMap<>();
		this.world = world;

		if(dataFile.exists()) {
			this.load();
		} else {
			dataFile.createNewFile();
		}
	}

	public boolean containsData(Coord3D position) {
		return (this.values.get(position) != null);
	}

	public boolean containsData(int x, int y, int z) {
		return (this.values.get(new Coord3D(x, y, z)) != null);
	}

	public IDataObject getDataForCoord(Coord3D position) {
		return this.values.get(position);
	}

	public IDataObject getDataForCoord(int x, int y, int z) {
		return this.getDataForCoord(new Coord3D(x, y, z));
	}

	/*
	 * s b;[BLOCKID] c;x;y;z d;[str/int/float/bool];[DATA] d;..... d;.....
	 * c;x;y;z ..... b;[BLOCKID] .... e
	 */
	public void load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(this.data));

		String line;
		boolean started = false;

		Coord3D coord = null;
		Block currentBlock = null;
		IDataObject currentObject = null;
		List<String> passData = null;

		while((line = br.readLine()) != null) {
			if(!started && line.charAt(0) != 's') {
				br.close();
				throw new UnsupportedDataTypeException(
						"Invalid file start (x-S-.-E)");
			}

			if(!started && line.charAt(0) == 's') {
				started = true;
				continue;
			}

			if(started && line.charAt(0) == 'e') {
				br.close();
				return;
			}

			if(started && line.charAt(0) == 'b') {
				currentBlock = world.getRegister().getBlock(
						Integer.parseInt(line.substring(2)));
				continue;
			}

			if(started && line.charAt(0) == 'c' && passData == null) {
				if(currentBlock == null) {
					br.close();
					throw new UnsupportedDataTypeException(
							"Invalid file contents (B-C-d-d)!");
				}
				
				String[] parts = line.split(";");
				coord = new Coord3D(Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				passData = new ArrayList<String>();
				currentObject = currentBlock.createDataObject();
				continue;
			}

			if(started && line.charAt(0) == 'c' && passData != null) {
				currentObject.load(new ArrayList<String>(passData));
				this.values.put(coord, currentObject);

				passData = null;
				coord = null;
			}

			if(started && coord == null && line.charAt(0) == 'd') {
				br.close();
				throw new UnsupportedDataTypeException(
						"Invalid file contents (b-C-D-D)!");
			}

			if(started && line.charAt(0) == 'd') {
				passData.add(line);
			}
		}

		br.close();
	}

	public void save() throws IOException {
		if(this.data.exists()) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.data));

			bw.write("s");
			bw.newLine();

			int currentID = 0;

			for(Entry<Coord3D, IDataObject> entry : this.values.entrySet()) {
				if(entry.getValue() != null) {

					if(currentID != entry.getValue().getObjectID()) {
						currentID = entry.getValue().getObjectID();
						bw.write("b;" + currentID);
						bw.newLine();
					}

					bw.write(String.format("c;%1$d;%2$d;%3$d",
							(int) entry.getKey().x, (int) entry.getKey().y,
							(int) entry.getKey().z));
					bw.newLine();

					for(String s : entry.getValue().save()) {
						bw.write(s);
						bw.newLine();
					}
				}
			}

			bw.write("e");
			bw.newLine();

			bw.close();
		}
	}

	@Override
	public void addDataForCoord(Coord3D position, IDataObject data) {
		this.values.put(position, data);
	}

	@Override
	public void addDataForCoord(int x, int y, int z, IDataObject data) {
		this.addDataForCoord(new Coord3D(x, y, z), data);
	}

	@Override
	public void removeDataForCoord(Coord3D position) {
		this.values.remove(position);
	}

	@Override
	public void removeDataForCoord(int x, int y, int z) {
		this.removeDataForCoord(new Coord3D(x, y, z));
	}
}
