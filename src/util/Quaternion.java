package util;

public class Quaternion {
	// COOLbeans... this is a spherical linear interpolation between two
	// Quaternions
	public static Quaternion slerp(Quaternion a, Quaternion b, double t) {
		final double epsilon = 0.00001f;
		double flip = 1;

		double cosine = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;

		if (cosine < 0) {
			cosine = -cosine;
			flip = -1;
		}

		if ((1 - cosine) < epsilon)
			return a.scale(1 - t).add(b.scale(t * flip));
		else {
			double theta = (double) Math.acos(cosine);
			double sine = (double) Math.sin(theta);
			double beta = (double) Math.sin((1 - t) * theta) / sine;
			double alpha = (double) Math.sin(t * theta) / sine * flip;

			return a.scale(beta).add(b.scale(alpha));
		}
	}

	public double w, x, y, z;

	public Quaternion() {
	}

	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion add(Quaternion o) {
		return new Quaternion(w + o.w, x + o.x, y + o.y, z + o.z);
	}

	public double length() {
		return (double) Math.sqrt(w * w + x * x + y * y + z * z);
	}

	public Quaternion multiply(Quaternion o) {
		return new Quaternion(
				w * o.w - x * o.x - y * o.y - z * o.z, 
				w * o.x	+ x * o.w + y * o.z - z * o.y, 
				w * o.y - x * o.z + y * o.w + z * o.x, 
				w * o.z + x * o.y - y * o.x + z * o.w);
	}

	public Quaternion normalize() {
		double length = this.length();
		Quaternion quaternion = new Quaternion();
		quaternion.w = w / length;
		quaternion.x = x / length;
		quaternion.y = y / length;
		quaternion.z = z / length;
		return quaternion;
	}

	public Quaternion scale(double o) {
		return new Quaternion(o * w, o * x, o * y, o * z);
	}

	public Matrix toMatrix() {
		double fTx = 2.0f * x;
		double fTy = 2.0f * y;
		double fTz = 2.0f * z;
		double fTwx = fTx * w;
		double fTwy = fTy * w;
		double fTwz = fTz * w;
		double fTxx = fTx * x;
		double fTxy = fTy * x;
		double fTxz = fTz * x;
		double fTyy = fTy * y;
		double fTyz = fTz * y;
		double fTzz = fTz * z;

		return new Matrix(
				1.0f - (fTyy + fTzz), 
				fTxy - fTwz, fTxz + fTwy, 
				fTxy + fTwz, 1.0f - (fTxx + fTzz), 
				fTyz - fTwx, fTxz - fTwy,
				fTyz + fTwx, 1.0f - (fTxx + fTyy));
	}
}
