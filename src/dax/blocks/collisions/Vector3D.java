package dax.blocks.collisions;

public class Vector3D {
	   public float x;
	   public float y;
	   public float z;
	   
	   public Vector3D() {
	      x = 0.0f;
	      y = 0.0f;
	      z = 0.0f;
	   }
	   
	   public Vector3D(float x, float y, float z) {
		  this.x = x;
		  this.y = y;
		  this.z = z;
	   }
	        
	   public float distSQ(final Vector3D vec) {
	      float distX = x - vec.x;
	      float distY = y - vec.y;
	      float distZ = y - vec.y;
	      
	      return distX * distX + distY * distY + distZ * distZ;
	   }
	}
