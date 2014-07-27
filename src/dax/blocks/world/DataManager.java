package dax.blocks.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.UnsupportedDataTypeException;

import dax.blocks.Coord3D;

public class DataManager {
	File data;
	private Map<Coord3D, Map<Integer, DataValue>> values;

	public DataManager(File dataFile) throws IOException {
		this.data = dataFile;
		values = new HashMap<>();

		if (dataFile.exists()) {
			load();
		} else {
			dataFile.createNewFile();
		}
	}
	
	public boolean containsData(Coord3D position) {
		return (values.get(position) != null);
	}

	public boolean containsData(int x, int y, int z) {
		return (values.get(new Coord3D(x, y, z)) != null);
	}
	
	public Map<Integer, DataValue> getValuesForCoord(Coord3D position) {
		if (values.get(position) != null) {
			return values.get(position);
		} else {
			Map<Integer, DataValue> dataMap = new HashMap<Integer, DataValue>();
			values.put(position, dataMap);
			return dataMap;
		}
	}

	public Map<Integer, DataValue> getValuesForCoord(int x, int y, int z) {
		return getValuesForCoord(new Coord3D(x, y, z));
	}

	public void load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(data));

		String line;
		boolean started = false;
		Coord3D currentCoords = null;
		Map<Integer, DataValue> currentMap = new HashMap<>();

		while ((line = br.readLine()) != null) {
			if (!started && line.charAt(0) != 's') {
				br.close();
				throw new UnsupportedDataTypeException("Invalid file start");
			}

			if (!started && line.charAt(0) == 's') {
				started = true;
				continue;
			}

			if (started && line.charAt(0) == 'e') {
				if (currentCoords != null && !currentMap.isEmpty())
					values.put(currentCoords, currentMap);
				break;
			}

			String[] parts = line.split(";");

			switch (parts[0]) {
			case "c":
				if (currentCoords == null && !currentMap.isEmpty()) {
					br.close();
					throw new UnsupportedDataTypeException(
							"Invalid file contents");
				}

				if (!currentMap.isEmpty()) {
					values.put(currentCoords, currentMap);
					currentMap = new HashMap<>();
				}
				currentCoords = new Coord3D(Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				break;
			case "d":
				currentMap.put(Integer.parseInt(parts[1]), new DataValue(parts[2]));
				break;
			}

		}

		br.close();
	}

	public void save() throws IOException {
		if (data.exists()) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(data));

			bw.write("s");
			bw.newLine();
			for (Entry<Coord3D, Map<Integer, DataValue>> entry : this.values
					.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					bw.write(String.format("c;%1$d;%2$d;%3$d",
							(int) entry.getKey().x, (int) entry.getKey().y,
							(int) entry.getKey().z));
					bw.newLine();

					for (Entry<Integer, DataValue> dataEntry : entry.getValue()
							.entrySet()) {
						bw.write(String.format("d;%1$s;%2$s", dataEntry
								.getKey(), dataEntry.getValue()
								.getDataAsString()));
						bw.newLine();
					}
				}
			}
			bw.write("e");
			bw.newLine();

			bw.close();
		}
	}
}
