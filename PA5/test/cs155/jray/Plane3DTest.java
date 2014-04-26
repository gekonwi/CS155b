package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Plane3DTest {

	private Point3D origin;
	private Point3D yVec;
	private Plane3D xyPlane;
	private Plane3D yzPlane;

	@Before
	public void before() {
		origin = new Point3D(0, 0, 0);
		yVec = new Point3D(0, 1, 0);
		xyPlane = new Plane3D(origin, yVec);
		xyPlane.outsideMat = new Material();

		// fields have size 100:100
		xyPlane.outsideMat.texture = new CheckerTexture();

		yzPlane = new Plane3D(origin, new Point3D(1, 0, 0), new Material());
		yzPlane.outsideMat.texture = new CheckerTexture();
	}

	@Test
	public void testZeroPlaneTextureWhite() {
		Point3D rayStart = new Point3D(5, 5, 5);
		Point3D rayDir = yVec.scale(-1);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		checkHitColor(xyPlane, ray, Color3D.WHITE);
	}

	@Test
	public void testZeroPlaneTextureBlack() {
		Point3D rayStart = new Point3D(105, 5, 5);
		Point3D rayDir = yVec.scale(-1);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		checkHitColor(xyPlane, ray, Color3D.BLACK);
	}

	@Test
	public void testRotatedPlaneTextureWhite() {
		Point3D rayStart = new Point3D(5, -5, 5);
		Point3D rayDir = new Point3D(-1, 0, 0);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		checkHitColor(yzPlane, ray, Color3D.WHITE);
	}

	@Test
	public void testRotatedPlaneTextureBlack() {
		Point3D rayStart = new Point3D(5, 5, 5);
		Point3D rayDir = new Point3D(-1, 0, 0);
		Ray3D ray = new Ray3D(rayStart, rayDir);

		checkHitColor(yzPlane, ray, Color3D.BLACK);
	}

	private void checkHitColor(Plane3D plane, Ray3D ray, Color3D expColor) {
		RayHit hit = plane.rayIntersect(ray);

		Color3D color = plane.outsideMat.texture.getColor(hit.tc);
		TestHelp.assertEquals(expColor, color);
	}

}
