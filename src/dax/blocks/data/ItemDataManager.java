package dax.blocks.data;

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


public class ItemDataManager implements IItemDataManager {

	File data;
	private Map<Integer, Map<Integer, DataValue>> values;

	public ItemDataManager(File dataFile) throws IOException {
		this.data = dataFile;
		this.values = new HashMap<>();

		if (dataFile.exists()) {
			this.load();
		} else {
			dataFile.createNewFile();
		}
	}
	
	@Override
	public boolean containsData(int identificator) {
		return (values.get(identificator) != null);
	}

	@Override
	public Map<Integer, DataValue> getValuesForIdentificator(int ident) {
		if (this.containsData(ident)) {
			return values.get(ident);
		} else {
			Map<Integer, DataValue> dataMap = new HashMap<Integer, DataValue>();
			values.put(ident, dataMap);
			return dataMap;
		}
	}

	public void load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(data));

		String line;
		boolean started = false;
		Integer currentKey = null;
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
				if (currentKey != null && !currentMap.isEmpty())
					values.put(currentKey, currentMap);
				break;
			}

			String[] parts = line.split(";");

			switch (parts[0]) {
			case "c":
				if (currentKey == null && !currentMap.isEmpty()) {
					br.close();
					throw new UnsupportedDataTypeException(
							"Invalid file contents");
				}

				if (!currentMap.isEmpty()) {
					values.put(currentKey, currentMap);
					currentMap = new HashMap<>();
				}
				currentKey = Integer.parseInt(parts[1]);
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
			for (Entry<Integer, Map<Integer, DataValue>> entry : this.values.entrySet()) {
				if (!entry.getValue().isEmpty()) {
					bw.write(String.format("i;%s", entry.getKey()));
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
