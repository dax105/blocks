package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.block.Block;
import dax.blocks.render.RenderPass;

public class ApplierLeaves extends Applier {

	@Override
	public void apply() {
		boolean transp = Game.settings.transparent_leaves.getValue();
		
		Block.leaves.setOpaque(!transp);
		Block.leaves.setRenderPass(transp ? RenderPass.PASS_TRANSPARENT : RenderPass.PASS_OPAQUE);
		Block.leaves.setAllTextures(transp ? 10 : 19);
		
		if (Game.getInstance().ingame) {
			Game.getInstance().world.setAllChunksDirty();
		}
		
	}

}
