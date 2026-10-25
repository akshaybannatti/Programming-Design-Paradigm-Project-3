package mat;

/**
 * This class has matrix in 2D square.
 */
public class ArrayMatrix implements SquareMatrix {
  private float[][] squarematrix;


  /**
   * Constructor for 2D Array Square Matrix to store all elements.
   * nxn array to zero.
   *
   * @param size of SquareMatrix.
   */
  public ArrayMatrix(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0");
    }
    squarematrix = new float[size][size];
  }

  @Override
  public void setIdentity() {
    for (int i = 0; i < size(); i++) {
      for (int j = 0; j < size(); j++) {
        squarematrix[i][j] = (i == j) ? 1.0f : 0.0f;
      }
    }
  }

  @Override
  public void set(int i, int j, float value) throws IllegalArgumentException {
    if ((i < 0 || i >= size()) || (j < 0 || j >= size())) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    squarematrix[i][j] = value;
  }

  @Override
  public float get(int i, int j) throws IllegalArgumentException {
    if ((i < 0 || i >= size()) || (j < 0 || j >= size())) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    return squarematrix[i][j];
  }

  @Override
  public SquareMatrix add(SquareMatrix other) throws IllegalArgumentException {
    ArrayMatrix result = new ArrayMatrix(this.size());
    if (other.size() != this.size()) {
      throw new IllegalArgumentException("Size of the matrix is not matching");
    } else {
      for (int i = 0; i < size(); i++) {
        for (int j = 0; j < size(); j++) {
          result.squarematrix[i][j] = squarematrix[i][j] + other.get(i, j);
        }
      }
    }
    return result;
  }

  @Override
  public SquareMatrix premul(SquareMatrix other) throws IllegalArgumentException {
    ArrayMatrix result = new ArrayMatrix(size());
    if (other.size() != size()) {
      throw new IllegalArgumentException("Size does not match for the Matrix");
    } else {
      for (int i = 0; i < size(); i++) {
        for (int j = 0; j < size(); j++) {
          for (int k = 0; k < size(); k++) {
            result.squarematrix[i][j] += other.get(i, k) * squarematrix[k][j];
          }
        }
      }
    }
    return result;
  }


  @Override
  public SquareMatrix postmul(SquareMatrix other) throws IllegalArgumentException {
    ArrayMatrix result = new ArrayMatrix(this.size());
    if ((other.size() != this.size())) {
      throw new IllegalArgumentException("Size does not match for the Matrix");
    } else {
      for (int i = 0; i < size(); i++) {
        for (int j = 0; j < size(); j++) {
          for (int k = 0; k < size(); k++) {
            result.squarematrix[i][j] += squarematrix[i][k] * other.get(k, j);
          }
        }
      }
    }
    return result;
  }

  /**
   * To find out the length of the squarematrix.
   *
   * @return size of the squaremamtrix.
   */
  @Override
  public int size() {
    return squarematrix.length;
  }
}
