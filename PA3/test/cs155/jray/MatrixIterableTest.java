package cs155.jray;

import static org.junit.Assert.*;

import org.junit.Test;

import cs155.jray.MatrixIterable.Entry;

public class MatrixIterableTest {

    @Test
    public void test_2_3() {
	double[][] matrix = new double[2][3];
	matrix[0] = new double[] { 1, 2, 3 };
	matrix[1] = new double[] { 4, 4, 4 };

	int[] expRows = { 0, 0, 0, 1, 1, 1 };
	int[] expCols = { 0, 1, 2, 0, 1, 2 };
	int[] expValues = { 1, 2, 3, 4, 4, 4 };

	int i = 0;
	for (Entry e : new MatrixIterable(matrix)) {
	    assertEquals("i = " + i, expRows[i], e.row);
	    assertEquals("i = " + i, expCols[i], e.col);
	    assertEquals("i = " + i, expValues[i], e.value(), 0.0000001);
	    i++;
	}

	assertEquals(6, i);
    }

    @Test
    public void test_1_3() {
	double[][] matrix = new double[1][3];
	matrix[0] = new double[] { 1, 2, 3 };

	int[] expRows = { 0, 0, 0 };
	int[] expCols = { 0, 1, 2 };
	int[] expValues = { 1, 2, 3 };

	int i = 0;
	for (Entry e : new MatrixIterable(matrix)) {
	    assertEquals("i = " + i, expRows[i], e.row);
	    assertEquals("i = " + i, expCols[i], e.col);
	    assertEquals("i = " + i, expValues[i], e.value(), 0.0000001);
	    i++;
	}

	assertEquals(3, i);
    }

    @Test
    public void test_0_0() {
	double[][] matrix = new double[0][0];

	for (@SuppressWarnings("unused")
	Entry e : new MatrixIterable(matrix))
	    fail("not detected that matrix is empty");
    }
}
