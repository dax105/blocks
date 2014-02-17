package dax.blocks.collisions;

public class AABB {
	   public Vector3D center;
	   public float size;
	   public float height;
	   
	   public AABB(final float size, final float height) {
		   center = new Vector3D();
		   this.size = size;
		   this.height = height;
	   }
	   
	   public void update(final Vector3D position) {
	      center.x = position.x;
	      center.y = position.y;
	      center.z = position.z;
	   }
	}
