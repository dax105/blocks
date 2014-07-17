package dax.blocks.settings;

import dax.blocks.GLHelper;
import dax.blocks.Game;

public class ApplierResolution extends Applier {

	@Override
	public boolean apply(Object val) {
		String[] res = val.toString().split("x");
		if (res.length < 2)
			return false;

		try {
			int width = Integer.parseInt(res[0]);
			int height = Integer.parseInt(res[1]);
			
			if(width < 100 || height < 100)
				return false;
			
			Game.getInstance().width = width;
			Game.getInstance().height = height;
			GLHelper.setDisplayMode(width, height, Game.getInstance().isFullscreen);
			
			return true;
			
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
