package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Test;

public class Cylinder3DTest {

	@Test
	public void noHitThroughBothOpenEnds() {
		Point3D rayRoot = new Point3D(1, -1, 0);
		Point3D rayDir = new Point3D(0, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, cyl(2, 3).rayIntersect(r).hitPoint);
	}

	@Test
	public void angleHitFromInside() {
		Point3D rayRoot = new Point3D(0, -1, 0);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = new Point3D(3, 2, 0);
		TestHelp.assertEquals(expHit, cyl(3, 3).rayIntersect(r).hitPoint);
	}

	@Test
	public void noHitThroughBothOpenEndsDiagonally() {
		Point3D rayRoot = new Point3D(-1, -1, 0);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, cyl(3, 2).rayIntersect(r).hitPoint);
	}

	@Test
	public void straightHitFromOutside() {
		Point3D rayRoot = new Point3D(5, 1, 0);
		Point3D rayDir = new Point3D(-1, 0, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = new Point3D(3, 1, 0);
		TestHelp.assertEquals(expHit, cyl(3, 3).rayIntersect(r).hitPoint);
	}

	@Test
	public void angleHitFromOutside() {
		Point3D rayRoot = new Point3D(6, 1, 0);
		Point3D rayDir = new Point3D(-2, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = new Point3D(2, 3, 0);
		TestHelp.assertEquals(expHit, cyl(2, 3.5).rayIntersect(r).hitPoint);
	}

	@Test
	public void rayStartsOnCylinderSurface() {
		Point3D rayRoot = new Point3D(3, 1, 0);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = new Point3D(3, 1, 0);
		TestHelp.assertEquals(expHit, cyl(3, 2).rayIntersect(r).hitPoint);
	}

	
	@Test
	public void infiniteHitsMeansNoHit() {
		Point3D rayRoot = new Point3D(3, -1, 0);
		Point3D rayDir = new Point3D(0, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, cyl(3, 5).rayIntersect(r).hitPoint);
	}

	@Test
	public void passesOutside() {
		Point3D rayRoot = new Point3D(3.1, -1, 0);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, cyl(3, 5).rayIntersect(r).hitPoint);
	}

	@Test
	public void distanceFromInside() {
		Point3D rayRoot = new Point3D(-1, 1, 0);
		Point3D rayDir = new Point3D(1, 0, 0);
		Ray3D r = new Ray3D(rayRoot, rayDir);

		TestHelp.assertEquals(4, cyl(3, 3).rayIntersect(r).distance);
	}

	private Cylinder3D cyl(double radius, double height) {
		Point3D center = new Point3D(0, 0, 0);
		Point3D direction = new Point3D(0, 1, 0);
		return new Cylinder3D(center, direction, radius, height);
	}

}
