package cs155.jray;

import org.junit.Assert;

public class TestHelp {
	private static final double PRECISION = 0.0001;

	public static void assertEquals(Point3D expected, Point3D actual) {
		if (expected == null && actual == null)
			return;

		if (expected == null && actual != null)
			Assert.fail("expected null but was " + actual);

		if (expected != null && actual == null)
			Assert.fail("expected " + expected + " but was null");

		Assert.assertEquals(expected.x, actual.x, PRECISION);
		Assert.assertEquals(expected.y, actual.y, PRECISION);
		Assert.assertEquals(expected.z, actual.z, PRECISION);
		Assert.assertEquals(expected.w, actual.w, PRECISION);
	}

	public static void assertEquals(double expected, double actual) {
		Assert.assertEquals(expected, actual, PRECISION);
	}
}
