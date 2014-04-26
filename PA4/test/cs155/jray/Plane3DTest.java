package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Test;

public class Plane3DTest {

	@Test
	public void straightHit() {
		Point3D planeCenter = new Point3D(0, 0, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, 10, 11);
		Point3D rayDir = new Point3D(0, -1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(9, 0, 11);
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void straightHitDistance() {
		Point3D planeCenter = new Point3D(0, 0, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, 10, 11);
		Point3D rayDir = new Point3D(0, -1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(9, 0, 11);
		TestHelp.assertEquals(10, plane.rayIntersect(ray).distance);
	}

	@Test
	public void angleHit() {
		Point3D planeCenter = new Point3D(0, 0, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, 10, 11);
		Point3D rayDir = new Point3D(1, -1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(19, 0, 11);
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void angleHitLiftedPlane() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, 10, 11);
		Point3D rayDir = new Point3D(1, -1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(14, 5, 11);
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void infiniteHitsMeansNoHits() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(1, 0, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(0, 0, 7);
		Point3D rayDir = new Point3D(0, -1, 1);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void parallelToPlaneWithoutTouching() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(1, 0, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(0.1, 0, 7);
		Point3D rayDir = new Point3D(0, -1, 1);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void angleHitFromBelow() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, -3, 11);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(17, 5, 11);
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void rayInWrongDirection() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, -3, 11);
		Point3D rayDir = new Point3D(1, -1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = RayHit.NO_HIT.hitPoint;
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

	@Test
	public void rayStartsInPlane() {
		Point3D planeCenter = new Point3D(0, 5, 0);
		Point3D planeNormal = new Point3D(0, 1, 0);
		Plane3D plane = new Plane3D(planeCenter, planeNormal);

		Point3D rayCenter = new Point3D(9, 5, 11);
		Point3D rayDir = new Point3D(1, 1, 0);
		Ray3D ray = new Ray3D(rayCenter, rayDir);

		Point3D expHit = new Point3D(9, 5, 11);
		TestHelp.assertEquals(expHit, plane.rayIntersect(ray).hitPoint);
	}

}
