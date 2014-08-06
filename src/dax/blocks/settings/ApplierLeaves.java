package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.block.Block;
import dax.blocks.render.RenderPass;

public class ApplierLeaves extends Applier {

	@Override
	public boolean apply(Object val) {
		boolean transp = (boolean) val;
		
		Block.leaves.setOpaque(!transp);
		Block.leaves.setRenderPass(transp ? RenderPass.TRANSPARENT : RenderPass.OPAQUE);
		Block.leaves.setAllTextures(transp ? 10 : 19);
		
		if(Game.getInstance().ingame) {
			Game.getInstance().world.setAllChunksDirty();
		}
		
		return true;
	}

}
