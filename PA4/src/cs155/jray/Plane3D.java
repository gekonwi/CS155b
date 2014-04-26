package cs155.jray;

import java.awt.Color;

/**
 * class represents a 3D plane
 */

public class Plane3D extends Object3D {
	public Point3D center, normal;

	/**
	 * create the plane with the specified parameters
	 * 
	 * @param center
	 * @param normal
	 * @param m
	 */
	public Plane3D(Point3D center, Point3D normal, Material m) {
		super();
		this.center = center;
		this.normal = normal;
		this.insideMat = this.outsideMat = m;
	}

	/**
	 * create the plane with the specified parameter and the default material
	 * 
	 * @param center
	 * @param normal
	 */
	public Plane3D(Point3D center, Point3D normal) {
		this(center, normal, Material.defaultMat);
	}

	/**
	 * intersect the ray r with this plane and return RayHit.NO_HIT if there is
	 * no intersection
	 */
	public RayHit rayIntersect(Ray3D r) {
		// no hit if ray is parallel to the plane
		double divisor = r.d.dot(normal);
		if (divisor == 0)
			return RayHit.NO_HIT;

		double dividend = center.subtract(r.p).dot(normal);
		double t = dividend / divisor;

		if (t < 0)
			return RayHit.NO_HIT;

		return new RayHit(r.atTime(t), t, normal, this);
	}
}
