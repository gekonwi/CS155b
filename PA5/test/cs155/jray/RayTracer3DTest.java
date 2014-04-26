package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RayTracer3DTest {

	private Plane3D xyPlane;
	private Point3D origin;
	private Point3D yVec;

	@Before
	public void before() {
		origin = new Point3D(0, 0, 0);
		yVec = new Point3D(0, 1, 0);
		yVec.w = 0;
		xyPlane = new Plane3D(origin, yVec);
	}

	@Test
	public void calcStraightReflectionRayTest() {
		Point3D rayStart = new Point3D(0, 5, 0);
		Point3D rayDir = yVec.scale(-1);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		RayHit hit = new RayHit(origin, 5, yVec, xyPlane, null);

		Ray3D reflectedRay = RayTracer3D.calcReflectionRay(ray, hit);

		Point3D expReflRayStart = new Point3D(0, RayTracer3D.RAY_START_OFFSET,
				0);

		TestHelp.assertEquals(expReflRayStart, reflectedRay.p);
		TestHelp.assertEquals(yVec, reflectedRay.d);
	}

	@Test
	public void calcAngleReflectionRayTest() {
		Point3D rayStart = new Point3D(-3, 4, 0);
		Point3D rayDir = new Point3D(3, -4, 0);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		RayHit hit = new RayHit(origin, 5, yVec, xyPlane, null);

		Ray3D reflectedRay = RayTracer3D.calcReflectionRay(ray, hit);

		Point3D expReflRayDir = new Point3D(3, 4, 0).normalize();
		expReflRayDir.w = 0;
		Point3D expReflRayStart = new Point3D(0, 0, 0).add(expReflRayDir
				.scale(RayTracer3D.RAY_START_OFFSET));

		TestHelp.assertEquals(expReflRayStart, reflectedRay.p);
		TestHelp.assertEquals(expReflRayDir, reflectedRay.d);
	}

}
