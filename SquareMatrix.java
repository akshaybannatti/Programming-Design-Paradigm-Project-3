package mat;

/**
 * This interface represents a 2D matrix of floating point numbers.
 */
public interface SquareMatrix {
  /**
   * Set this matrix to the identity matrix.
   */
  void setIdentity();

  /**
   * Set the specific cell to the specific value. Cell rows and columns begin with 0.
   *
   * @param i     the row of the cell
   * @param j     the column of the cell
   * @param value the floating point value
   * @throws IllegalArgumentException if cell does not exist within the bounds of this matrix.
   */
  void set(int i, int j, float value) throws IllegalArgumentException;

  /**
   * Retrieves and returns the value at the specified cell in this matrix.
   *
   * @param i the row of the cell
   * @param j the column of the cell
   * @return the value at the given row and column
   * @throws IllegalArgumentException if the row or column is not within bounds of this matrix.
   */
  float get(int i, int j) throws IllegalArgumentException;

  /**
   * Add this matrix with the other matrix and return the result.
   * Neither this nor the other matrix should be changed as a result of this operation.
   *
   * @param other the other matrix to be added to this.
   * @return the sum of this and other matrix.
   * @throws IllegalArgumentException if number of rows and number of columns of
   *                                  two matrices do not match respectively.
   */
  SquareMatrix add(SquareMatrix other) throws IllegalArgumentException;

  /**
   * Pre multiply this matrix with the other matrix (result = other * this) and return the result.
   * Neither this nor the other matrix should be changed as a result of this operation.
   *
   * @param other the other matrix
   * @return the product of the post-multiplication of this with other
   * @throws IllegalArgumentException if the number of columns of this matrix are not equal
   *                                  to the number of rows of the other matrix.
   */
  SquareMatrix premul(SquareMatrix other) throws IllegalArgumentException;

  /**
   * Post multiply this matrix with the other matrix (result = this * other) and return the result.
   * Neither this nor the other matrix should be changed as a result of this operation.
   *
   * @param other the other matrix
   * @return the product of the post-multiplication of this with other
   * @throws IllegalArgumentException if the number of columns of this matrix are not equal
   *                                  to the number of rows of the other matrix.
   */
  SquareMatrix postmul(SquareMatrix other) throws IllegalArgumentException;

  /**
   * Get the size of this matrix (number of rows or number of columns).
   *
   * @return the size of this matrix
   */
  int size();

}
