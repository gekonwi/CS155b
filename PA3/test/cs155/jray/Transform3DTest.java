package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Test;

import cs155.jray.MatrixIterable.Entry;

public class Transform3DTest {

    private static final double PRECISION = 0.000001;

    @Test
    public void testTransform3D() {
	/*
	 * test that the default transform is a 4x4 identity matrix
	 */
	Transform3D t = new Transform3D();
	for (Entry e : new MatrixIterable(new double[4][4])) {
	    if (e.row == e.col)
		assertEquals(e.toString(), 1, t.entry(e.row, e.col), PRECISION);
	    else
		assertEquals(e.toString(), 0, t.entry(e.row, e.col), PRECISION);
	}
    }

    @Test
    public void testTranslationDoubleDoubleDouble() {
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 1 };
	expected[1] = new double[] { 0, 1, 0, 2 };
	expected[2] = new double[] { 0, 0, 1, 2 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.translation(1, 2, 2));
    }

    @Test
    public void testTranslationPoint3D() {
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 5 };
	expected[1] = new double[] { 0, 1, 0, 6 };
	expected[2] = new double[] { 0, 0, 1, 4 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.translation(new Point3D(5, 6, 4)));
    }

    @Test
    public void testRotationZ() {
	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 0, -1, 0, 0 };
	expected[1] = new double[] { 1, 0, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationZ(90));

	// sin(-90) = -1, cos(-90) = 0
	expected = new double[4][4];
	expected[0] = new double[] { 0, 1, 0, 0 };
	expected[1] = new double[] { -1, 0, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationZ(-90));

	// rotation of 0 should cause identity matrix
	expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 1, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationZ(0));
    }

    @Test
    public void testRotationY() {
	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 0, 0, 1, 0 };
	expected[1] = new double[] { 0, 1, 0, 0 };
	expected[2] = new double[] { -1, 0, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationY(90));

	// sin(-90) = -1, cos(-90) = 0
	expected = new double[4][4];
	expected[0] = new double[] { 0, 0, -1, 0 };
	expected[1] = new double[] { 0, 1, 0, 0 };
	expected[2] = new double[] { 1, 0, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationY(-90));

	// rotation of 0 should cause identity matrix
	expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 1, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationY(0));
    }

    @Test
    public void testRotationX() {
	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 0, -1, 0 };
	expected[2] = new double[] { 0, 1, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationX(90));

	// sin(-90) = -1, cos(-90) = 0
	expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 0, 1, 0 };
	expected[2] = new double[] { 0, -1, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationX(-90));

	// rotation of 0 should cause identity matrix
	expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 1, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.rotationX(0));
    }

    @Test
    public void testComposeRotationXRotationX() {
	Transform3D rotateX45 = Transform3D.rotationX(45);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 0, -1, 0 };
	expected[2] = new double[] { 0, 1, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.compose(rotateX45, rotateX45));
    }

    @Test
    public void testComposeRotationXTranslation() {
	Transform3D rotateX90 = Transform3D.rotationX(90);
	Transform3D translate1_2_4 = Transform3D.translation(1, 2, 4);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 1 };
	expected[1] = new double[] { 0, 0, -1, -4 };
	expected[2] = new double[] { 0, 1, 0, 2 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, Transform3D.compose(rotateX90, translate1_2_4));
    }

    @Test
    public void testApplyRotateX90() {
	Transform3D rotateX90 = Transform3D.rotationX(90);
	Point3D p2_0_10 = new Point3D(2, 0, 10);

	/*
	 * Taking the right hand with the thumb pointing in the direction of the
	 * x-axis and the other four fingers together and bend, the rotation is
	 * applied in the direction in which the four fingers are pointing. In
	 * other words by looking in the direction of the x-axis (1, 0, 0) the
	 * rotation is applied clockwise.
	 */
	Point3D p2_0_10RotateX90 = rotateX90.applyTo(p2_0_10);

	assertEquals(2, p2_0_10RotateX90.x, PRECISION);
	assertEquals(-10, p2_0_10RotateX90.y, PRECISION);
	assertEquals(0, p2_0_10RotateX90.z, PRECISION);
    }

    @Test
    public void testApplyTranslate() {
	Transform3D translate1_2_4 = Transform3D.translation(1, 2, 4);
	Point3D p2_0_10 = new Point3D(2, 0, 10);
	Point3D p2_0_10Translate1_2_4 = translate1_2_4.applyTo(p2_0_10);

	assertEquals(3, p2_0_10Translate1_2_4.x, PRECISION);
	assertEquals(2, p2_0_10Translate1_2_4.y, PRECISION);
	assertEquals(14, p2_0_10Translate1_2_4.z, PRECISION);
    }

    @Test
    public void testTranslatePoint3D() {
	Transform3D rotateX90 = Transform3D.rotationX(90);
	Point3D p2_0_10 = new Point3D(2, 0, 10);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 2 };
	expected[1] = new double[] { 0, 0, -1, -10 };
	expected[2] = new double[] { 0, 1, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, rotateX90.translate(p2_0_10));
    }

    @Test
    public void testTranslateDoubleDoubleDouble() {
	Transform3D rotateX90 = Transform3D.rotationX(90);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, -5 };
	expected[1] = new double[] { 0, 0, -1, -3 };
	expected[2] = new double[] { 0, 1, 0, 8 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, rotateX90.translate(-5, 8, 3));
    }

    @Test
    public void testRotateX() {
	Transform3D rotateX45 = Transform3D.rotationX(45);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 1, 0, 0, 0 };
	expected[1] = new double[] { 0, 0, -1, 0 };
	expected[2] = new double[] { 0, 1, 0, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, rotateX45.rotateX(45));
    }

    @Test
    public void testRotateY() {
	Transform3D translate1_2_4 = Transform3D.translation(1, 2, 4);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 0, 0, 1, 1 };
	expected[1] = new double[] { 0, 1, 0, 2 };
	expected[2] = new double[] { -1, 0, 0, 4 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, translate1_2_4.rotateY(90));
    }

    @Test
    public void testRotateZ() {
	Transform3D rotateZ180 = Transform3D.rotationZ(180);

	// sin(90) = 1, cos(90) = 0
	double[][] expected = new double[4][4];
	expected[0] = new double[] { 0, -1, 0, 0 };
	expected[1] = new double[] { 1, 0, 0, 0 };
	expected[2] = new double[] { 0, 0, 1, 0 };
	expected[3] = new double[] { 0, 0, 0, 1 };

	test(expected, rotateZ180.rotateZ(-90)); // == rotateZ90
    }

    private void test(double[][] expected, Transform3D t) {
	for (Entry e : new MatrixIterable(expected))
	    assertEquals(e.toString(), e.value(), t.entry(e.row, e.col),
		    PRECISION);
    }
}
