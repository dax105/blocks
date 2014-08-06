package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.render.RenderPass;
import dax.blocks.world.IDRegister;

public class ApplierLeaves extends Applier {

	@Override
	public boolean apply(Object val) {
		boolean transp = (boolean) val;
		
		IDRegister.leaves.setOpaque(!transp);
		IDRegister.leaves.setRenderPass(transp ? RenderPass.TRANSPARENT : RenderPass.OPAQUE);
		IDRegister.leaves.setAllTextures(transp ? 10 : 19);
		
		if(Game.getInstance().ingame) {
			Game.getInstance().world.setAllChunksDirty();
		}
		
		return true;
	}

}
