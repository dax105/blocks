package dax.blocks.data.block;

import java.util.ArrayList;
import java.util.List;

import dax.blocks.block.BlockPlant;
import dax.blocks.data.IDataObject;
import dax.blocks.settings.ObjectType;
import dax.blocks.util.GameUtil;

public class PlantDataObject implements IDataObject {

	private int blockID;
	private boolean hasSpecialTexture;
	
	public PlantDataObject(BlockPlant plant) {
		this.blockID = plant.getID();
	}
	
	@Override
	public void load(List<String> values) {
		for(String line : values) {
			if(line.startsWith("d;" + GameUtil.objectTypeAsString(ObjectType.BOOLEAN) + ";")) {
				this.hasSpecialTexture = Boolean.parseBoolean(line.substring(8));
			}
		}
	}

	@Override
	public List<String> save() {
		List<String> r = new ArrayList<String>();
		r.add("d;" + GameUtil.objectTypeAsString(ObjectType.BOOLEAN) + ";" + this.hasSpecialTexture);
		return r;
	}

	@Override
	public int getObjectID() {
		return this.blockID;
	}
	
	public boolean isTextured() {
		return this.hasSpecialTexture;
	}
	
	public void setTextured(boolean b) {
		this.hasSpecialTexture = b;
	}

}
