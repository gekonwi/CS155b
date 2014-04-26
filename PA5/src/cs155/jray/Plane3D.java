package cs155.jray;

import java.awt.Color;

/**
 * class represents a 3D plane
 */

public class Plane3D extends Object3D {
	public Point3D center, normal;
	private Point3D up = new Point3D(0d, 1d, 0d);
	private Point3D right = new Point3D(1d, 0d, 0d);

	public Plane3D(Point3D center, Point3D normal, Material m) {
		super();
		this.center = center;
		this.normal = normal.normalize();
		this.insideMat = this.outsideMat = m;
		Point3D tmp = project(up);
		if (Math.abs(tmp.dot(tmp)) > 0.1) {
			up = up.normalize();
			right = up.cross(normal);
		} else {
			right = project(right).normalize();
			up = right.cross(normal);
		}

	}

	public Plane3D(Point3D center, Point3D normal) {
		this(center, normal, Material.defaultMat);
	}

	private Point3D project(Point3D q) {
		return q.subtract(normal.scale(normal.dot(q)));
	}

	public RayHit rayIntersect(Ray3D r) {
		Point3D P = r.p, D = r.d;
		Point3D PC = P.subtract(center);
		double dn = normal.dot(D), cpn = normal.dot(PC), t;
		if (dn == 0.0)
			return RayHit.NO_HIT;
		double t0 = -cpn / dn;
		if (t0 < epsilon)
			return RayHit.NO_HIT;
		else {
			Point3D hitPoint = r.atTime(t0);

			double texX = hitPoint.subtract(center).dot(right);
			double texY = hitPoint.subtract(center).dot(up);
			
			TextureCoordinate tc = new TextureCoordinate(texX, texY);
			return new RayHit(hitPoint, t0, normal, this, tc);
		}
	}

}
