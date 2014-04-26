package cs155.jray;

import java.util.Iterator;

/**
 * This class facilitates the iteration over matrices. It supports the for-each
 * iteration returning an {@link Entry} object which contains the current matrix
 * row, column, and value.
 * 
 * @author Georg Konwisser (gekonwi@brandeis.edu)
 * 
 */
public class MatrixIterable implements Iterable<MatrixIterable.Entry> {

    public class Entry {
	public final int row, col;

	public Entry(int row, int col) {
	    this.row = row;
	    this.col = col;
	}

	public double value() {
	    return matrix[row][col];
	}

	@Override
	public String toString() {
	    return "entry [" + row + ", " + col + "] = " + value();
	}
    }

    private final int rows, cols;
    private final double[][] matrix;

    public MatrixIterable(double[][] matrix) {
	this.rows = matrix.length;
	this.cols = (rows == 0 ? 0 : matrix[0].length);
	this.matrix = matrix;
    }

    @Override
    public Iterator<Entry> iterator() {
	return new Iterator<Entry>() {

	    private int row = 0, col = -1;
	    boolean endReached = (rows == 0);

	    @Override
	    public boolean hasNext() {
		return !endReached;
	    }

	    @Override
	    public Entry next() {
		row = (col == cols - 1 ? row + 1 : row);
		col = (col == cols - 1 ? 0 : col + 1);
		endReached = (row == rows - 1 && col == cols - 1);
		return new Entry(row, col);
	    }

	    @Override
	    public void remove() {
		throw new UnsupportedOperationException();
	    }
	};
    }
}
