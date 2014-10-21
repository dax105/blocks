package cz.dat.oots.overlay;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cz.dat.oots.render.IOverlayRenderer;

public class OverlayManager {
	private List<IOverlayRenderer> overlays;
	private List<IOverlayRenderer> addingOverlays;
	private List<IOverlayRenderer> removalOverlays;

	public OverlayManager() {
		this.overlays = new LinkedList<IOverlayRenderer>();
		this.addingOverlays = new LinkedList<IOverlayRenderer>();
		this.removalOverlays = new LinkedList<IOverlayRenderer>();
	}

	public void renderOverlays(float ptt) {
		for(IOverlayRenderer r : this.overlays) {
			r.renderOverlay(ptt);
		}

		for(Iterator<IOverlayRenderer> it = this.addingOverlays.iterator(); it
				.hasNext();) {
			this.overlays.add(it.next());
			it.remove();
		}

		for(Iterator<IOverlayRenderer> it = this.removalOverlays.iterator(); it
				.hasNext();) {
			this.overlays.remove(it.next());
			it.remove();
		}
	}

	public void addOverlay(IOverlayRenderer o) {
		if(!this.overlays.contains(o)) {
			this.addingOverlays.add(o);
		}
	}

	public void removeOverlay(IOverlayRenderer o) {
		if(this.overlays.contains(o)) {
			this.removalOverlays.add(o);
		}
	}

	public List<IOverlayRenderer> getOverlays() {
		return this.overlays;
	}
}
