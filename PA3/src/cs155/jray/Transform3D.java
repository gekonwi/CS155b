package cs155.jray;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

import cs155.jray.MatrixIterable.Entry;

/**
 * this class represented 3d affine transforms
 * 
 * @author tim
 * @author Georg Konwisser (gekonwi@brandeis.edu)
 * 
 */

public class Transform3D {
    public final static Transform3D IDENTITY = new Transform3D();

    /* the transform is represented by this 4x4 matrix */
    private double matrix[][] = new double[4][4];

    /**
     * create the default transform, which is the identity transform
     */
    public Transform3D() {
	for (Entry e : new MatrixIterable(new double[4][4]))
	    this.matrix[e.row][e.col] = (e.row == e.col) ? 1 : 0;
    }

    /**
     * create a translation transform
     * 
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Transform3D translation(double x, double y, double z) {
	Transform3D t = new Transform3D();
	t.matrix[0][3] = x;
	t.matrix[1][3] = y;
	t.matrix[2][3] = z;
	return t;
    }

    /**
     * generate the Transform3D corresponding to translation by a point p
     */
    public static Transform3D translation(Point3D p) {
	return translation(p.x, p.y, p.z);
    }

    /**
     * generate the Transform3D corresponding to rotation theta degrees around
     * the x-axis
     * 
     * @param theta
     *            angle in degrees
     * @return
     */
    public static Transform3D rotationZ(double theta) {
	Transform3D t = new Transform3D();

	theta = theta / 180 * Math.PI; // convert from degrees to radians
	double c = Math.cos(theta);
	double s = Math.sin(theta);

	t.matrix[0][0] = c;
	t.matrix[0][1] = -s;
	t.matrix[1][0] = s;
	t.matrix[1][1] = c;

	return t;
    }

    /**
     * generate the Transform3D corresponding to rotation theta degrees around
     * the y-axis
     * 
     * @param theta
     *            angle in degrees
     * @return
     */
    public static Transform3D rotationY(double theta) {
	Transform3D t = new Transform3D();

	theta = theta / 180 * Math.PI; // convert from degrees to radians
	double c = Math.cos(theta);
	double s = Math.sin(theta);

	t.matrix[0][0] = c;
	t.matrix[0][2] = s;
	t.matrix[2][0] = -s;
	t.matrix[2][2] = c;

	return t;
    }

    /**
     * generate the Transform3D corresponding to rotation theta degrees around
     * the z-axis
     * 
     * @param theta
     *            angle in degrees
     * @return
     */
    public static Transform3D rotationX(double theta) {
	Transform3D t = new Transform3D();

	theta = theta / 180 * Math.PI; // convert from degrees to radians
	double c = Math.cos(theta);
	double s = Math.sin(theta);

	t.matrix[1][1] = c;
	t.matrix[1][2] = -s;
	t.matrix[2][1] = s;
	t.matrix[2][2] = c;

	return t;
    }

    /**
     * Compose the two transformations by multiplying their correspond matrices
     * 
     * @param a
     * @param b
     * @return
     */
    public static Transform3D compose(Transform3D a, Transform3D b) {
	Transform3D result = new Transform3D();

	result.matrix = Transform3D.multiply(a.matrix, b.matrix);

	return result;
    }

    /**
     * multiply the two matrices together and return their product
     */
    private static double[][] multiply(double[][] a, double[][] b) {
	int rows = a.length;
	int cols = b[0].length;
	double[][] result = new double[rows][cols];

	for (Entry e : new MatrixIterable(result))
	    for (int i = 0; i < 4; i++)
		result[e.row][e.col] += a[e.row][i] * b[i][e.col];

	return result;
    }

    /* ******************************
     * Below are the instance methods ...
     */

    /**
     * Apply this transform to a point. Pays attention to the point's fourth
     * coordinate <code>w</code>.
     * 
     * @param p
     *            (stays unchanged)
     * @return a new point, resulting from <code>p</code>, transformed by this
     *         transform
     */
    public Point3D applyTo(Point3D p) {
	double[][] pointVec = new double[4][1];
	pointVec[0][0] = p.x;
	pointVec[1][0] = p.y;
	pointVec[2][0] = p.z;
	pointVec[3][0] = p.w;

	double[][] r = Transform3D.multiply(matrix, pointVec);

	return new Point3D(r[0][0], r[1][0], r[2][0], r[3][0]);
    }

    /**
     * return the transform obtained by composing with a translation by a point
     * p
     * 
     * @param p
     * @return
     */
    public Transform3D translate(Point3D p) {
	return Transform3D.compose(this, Transform3D.translation(p));
    }

    /**
     * return the transform obtained by composing with a translation by a
     * (x,y,z)
     * 
     * @param p
     * @return
     */
    public Transform3D translate(double x, double y, double z) {
	return Transform3D.compose(this, Transform3D.translation(x, y, z));
    }

    /**
     * return the transform obtained by composing with a x-rotation of d degrees
     * 
     * @param d
     * @return
     */
    public Transform3D rotateX(double d) {
	return Transform3D.compose(this, Transform3D.rotationX(d));
    }

    /**
     * return the transform obtained by composing with a y-rotation of d degrees
     * 
     * @param d
     * @return
     */
    public Transform3D rotateY(double d) {
	return Transform3D.compose(this, Transform3D.rotationY(d));
    }

    /**
     * return the transform obtained by composing with a z-rotation of d degrees
     * 
     * @param d
     * @return
     */
    public Transform3D rotateZ(double d) {
	return Transform3D.compose(this, Transform3D.rotationZ(d));
    }

    /**
     * gives a one-line representation of the matrix with values rounded to two
     * decimal places
     */
    public String toString() {
	DecimalFormat df = new DecimalFormat("#.##");
	df.setRoundingMode(RoundingMode.HALF_UP);

	String[][] rounded = new String[4][4];
	for (Entry e : new MatrixIterable(matrix))
	    rounded[e.row][e.col] = df.format(e.value());

	String[] rows = new String[4];

	for (int row = 0; row < 4; row++)
	    rows[row] = Arrays.toString(rounded[row]);

	return "transform " + Arrays.toString(rows);
    }

    protected double entry(int row, int column) {
	return matrix[row][column];
    }
}
