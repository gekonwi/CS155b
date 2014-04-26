package cs155.jray;

import org.junit.Assert;

/**
 * Provides 3D geometry specific assertion methods used in many unit tests.
 * 
 * @author Georg Konwisser, gekonwi@brandeis.edu
 * 
 */
public class TestHelp {
	private static final double PRECISION = 0.01;

	public static void assertEquals(Point3D expected, Point3D actual) {
		checkForNull(expected, actual);

		Assert.assertEquals(expected.x, actual.x, PRECISION);
		Assert.assertEquals(expected.y, actual.y, PRECISION);
		Assert.assertEquals(expected.z, actual.z, PRECISION);
		Assert.assertEquals(expected.w, actual.w, PRECISION);
	}

	private static void checkForNull(Object expected, Object actual) {
		if (expected == null && actual == null)
			return;

		if (expected == null && actual != null)
			Assert.fail("expected null but was " + actual);

		if (expected != null && actual == null)
			Assert.fail("expected " + expected + " but was null");
	}

	public static void assertEquals(double expected, double actual) {
		Assert.assertEquals(expected, actual, PRECISION);
	}

	public static void assertEquals(Color3D expected, Color3D actual) {
		checkForNull(expected, actual);

		Assert.assertEquals(expected.red, actual.red, PRECISION);
		Assert.assertEquals(expected.green, actual.green, PRECISION);
		Assert.assertEquals(expected.blue, actual.blue, PRECISION);
	}
}
