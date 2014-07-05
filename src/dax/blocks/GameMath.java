package dax.blocks;

public class GameMath {

	public static double biLerp(double x, double y, double q11, double q12, double q21, double q22, double x1, double x2, double y1, double y2) {
		double r1 = lerp(x, x1, x2, q11, q21);
		double r2 = lerp(x, x1, x2, q12, q22);
		return lerp(y, y1, y2, r1, r2);
	}

	public static double lerp(double x, double x1, double x2, double q00, double q01) {
		return ((x2 - x) / (x2 - x1)) * q00 + ((x - x1) / (x2 - x1)) * q01;
	}

	public static double lerp(double x1, double x2, double p) {
		return x1 * (1.0 - p) + x2 * p;
	}

	public static double triLerp(double x, double y, double z, double q000, double q001, double q010, double q011, double q100, double q101, double q110, double q111, double x1, double x2, double y1, double y2, double z1, double z2) {
		double x00 = lerp(x, x1, x2, q000, q100);
		double x10 = lerp(x, x1, x2, q010, q110);
		double x01 = lerp(x, x1, x2, q001, q101);
		double x11 = lerp(x, x1, x2, q011, q111);
		double r0 = lerp(y, y1, y2, x00, x01);
		double r1 = lerp(y, y1, y2, x10, x11);
		return lerp(z, z1, z2, r0, r1);
	}

}
