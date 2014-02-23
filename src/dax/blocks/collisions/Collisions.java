package dax.blocks.collisions;

public class Collisions {

	public static boolean testAABBAABB(final AABB box1, final AABB box2) {
		if (Math.abs(box1.center.x - box2.center.x) > (box1.size + box2.size)) {
			return false;
		}
		if (Math.abs(box1.center.y - box2.center.y) > (box1.height + box2.height)) {
			return false;
		}
		if (Math.abs(box1.center.z - box2.center.z) > (box1.size + box2.size)) {
			return false;
		}
		return true;
	}
}
