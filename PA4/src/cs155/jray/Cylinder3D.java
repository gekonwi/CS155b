package cs155.jray;

import java.awt.Color;

/**
 * this class represents a 3D cylinder with open ends
 */

public class Cylinder3D extends Object3D {
	public Point3D center;
	public Point3D direction;

	public double radius = 0.0;
	public double height = 0.0;

	/**
	 * create a cylinder with the specified parameters and the default Material
	 * 
	 * @param center
	 * @param direction
	 * @param radius
	 * @param height
	 */
	public Cylinder3D(Point3D center, Point3D direction, double radius,
			double height) {
		this(center, direction, radius, height, Material.defaultMat);
	}

	/**
	 * create a Cylinder with the specified parameters
	 * 
	 * @param center
	 * @param direction
	 * @param radius
	 * @param height
	 * @param m
	 */
	public Cylinder3D(Point3D center, Point3D direction, double radius,
			double height, Material m) {
		this.center = center;
		this.direction = direction.normalize();
		this.radius = radius;
		this.height = height;
		this.insideMat = this.outsideMat = m;
	}

	/**
	 * return the Rayhit object corresponding to intersecting the ray r with
	 * this cylinder. If there is no intersection, then return RayHit.NO_HIT
	 * 
	 */
	public RayHit rayIntersect(Ray3D r) {
		Point3D w = r.p.subtract(center);
		Point3D alpha = r.d.subtract(direction.scale(r.d.dot(direction)));
		Point3D beta = w.subtract(direction.scale(w.dot(direction)));

		// we get the equation A^2 * t + B * t + C = 0
		double A = alpha.dot(alpha); // |alpha|^2
		double B = alpha.scale(2).dot(beta); // 2 * alpha * beta
		double C = beta.dot(beta) - Math.pow(radius, 2); // |beta|^2 - radius^2
		double D = Math.pow(B, 2) - 4 * A * C; // the discriminant

		if (D < 0 || A == 0)
			return RayHit.NO_HIT;

		double t1 = (-B - Math.sqrt(D)) / (2 * A);
		double t2 = (-B + Math.sqrt(D)) / (2 * A);

		return rayIntersect(r, t1, t2);
	}

	/**
	 * Returns the intersection point given the ray and the two possible times
	 * of intersection.
	 * 
	 * @param r
	 *            the intersecting ray
	 * @param t1
	 *            the first possible time of intersection
	 * @param t2
	 *            the second possible time of intersection
	 * @return the intersection point or RayHit.NO_HIT if there is no
	 *         intersection for both given times
	 */
	private RayHit rayIntersect(Ray3D r, double t1, double t2) {
		if (t1 < 0 && t2 < 0)
			return RayHit.NO_HIT;

		if (t1 < 0 && t2 >= 0)
			return rayIntersect(r, t2);

		if (t1 >= 0 && t2 < 0)
			return rayIntersect(r, t1);

		// we know t1, t2 >= 0

		RayHit hit1 = rayIntersect(r, t1);
		RayHit hit2 = rayIntersect(r, t2);

		if (hit1 == RayHit.NO_HIT && hit2 == RayHit.NO_HIT)
			return RayHit.NO_HIT;

		if (hit1 == RayHit.NO_HIT && hit2 != RayHit.NO_HIT)
			return hit2;

		if (hit1 != RayHit.NO_HIT && hit2 == RayHit.NO_HIT)
			return hit1;

		// we know that both are hits

		return (t1 < t2 ? hit1 : hit2);
	}

	/**
	 * Returns the intersection point given the ray and the times of
	 * intersection.
	 * 
	 * @param r
	 *            the intersecting ray
	 * @param t
	 *            the time of intersection
	 * @return the intersection point or RayHit.NO_HIT if there is no
	 *         intersection at the given time
	 */
	private RayHit rayIntersect(Ray3D r, double t) {
		Point3D hit = r.atTime(t);

		double hitHeight = hit.subtract(center).dot(direction);
		if (hitHeight < 0 || hitHeight > height)
			return RayHit.NO_HIT;

		Point3D hit1 = hit.subtract(center);
		Point3D normal = hit1.subtract(direction.scale(hit1.dot(direction)));

		return new RayHit(hit, t, normal, this);
	}
}
