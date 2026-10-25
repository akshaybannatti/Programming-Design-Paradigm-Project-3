import static org.junit.Assert.assertEquals;

import com.beust.jcommander.Parameterized;
import mat.ArrayMatrix;
import mat.SparseMatrix;
import mat.SquareMatrix;
import org.junit.Test;


/**
 * Tests cases for the MatrixTests.
 * Test cases for ArrayMatrixTests and test cases for the SparseMatrixTest.
 */
public class MatrixTests {

  @Test
  public void testAdd() {
    /*

    1 2 3 4
    1 5 2 1
    0 1 -1 2
    0 0 1 0

    +

    2 1 3 1
    2 1 5 4
    -1 2 1 1
    1 1 1 1

    =

    3 3 6 5
    3 6 7 5
    -1 3 0 3
    1 1 2 1

     */
    SquareMatrix one = new ArrayMatrix(4);
    SquareMatrix two = new ArrayMatrix(4);
    float[][] onevalues = {{1, 2, 3, 4}, {1, 5, 2, 1}, {0, 1, -1, 2}, {0, 0, 1, 0}};

    float[][] twovalues = {{2, 1, 3, 1}, {2, 1, 5, 4}, {-1, 2, 1, 1}, {1, 1, 1, 1}};

    float[][] answer = {{3, 3, 6, 5}, {3, 6, 7, 5}, {-1, 3, 0, 3}, {1, 1, 2, 1}};
    for (int i = 0; i < 4; i += 1) {
      for (int j = 0; j < 4; j += 1) {
        one.set(i, j, onevalues[i][j]);
        two.set(i, j, twovalues[i][j]);
      }
    }
    SquareMatrix result = one.add(two);
    for (int i = 0; i < 4; i += 1) {
      for (int j = 0; j < 4; j += 1) {
        assertEquals(answer[i][j], result.get(i, j), 0.001);
      }
    }
  }

  @Test
  public void testLargeIdentities() {
    int dim = 1000;
    SquareMatrix one = new ArrayMatrix(dim);
    SquareMatrix two = new ArrayMatrix(dim);
    one.setIdentity();
    two.setIdentity();
    SquareMatrix add = one.add(two);
    SquareMatrix premul = one.premul(two);
    SquareMatrix postmul = one.postmul(two);

    for (int i = 0; i < dim; i += 1) {
      for (int j = 0; j < dim; j += 1) {
        if (i == j) {
          assertEquals(2, add.get(i, j), 0.001);
          assertEquals(1, premul.get(i, j), 0.001);
          assertEquals(1, postmul.get(i, j), 0.001);
        } else {
          assertEquals(0, add.get(i, j), 0.001);
          assertEquals(0, premul.get(i, j), 0.001);
          assertEquals(0, postmul.get(i, j), 0.001);
        }
      }
    }
  }

  @Test
  public void testSquareMatrix() {
    SquareMatrix one = new ArrayMatrix(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixNegativeSize() {
    new ArrayMatrix(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixZeroSize() {
    new ArrayMatrix(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixNegativeSize() {
    new SparseMatrix(-5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixZeroSize() {
    new SparseMatrix(0);
  }

  @Test
  public void testArrayMatrixValidSize() {
    ArrayMatrix m = new ArrayMatrix(5);
    assertEquals(5, m.size());
  }

  @Test
  public void testSparseMatrixValidSize() {
    SparseMatrix m = new SparseMatrix(10);
    assertEquals(10, m.size());
  }

  @Test
  public void testSparseMatrixSetIdentiy() {
    SparseMatrix m = new SparseMatrix(4);
    m.setIdentity();
    assertEquals(1.0f, m.get(0, 0), 0.0001f);
    assertEquals(1.0f, m.get(1, 1), 0.0001f);
    assertEquals(1.0f, m.get(2, 2), 0.0001f);
    assertEquals(1.0f, m.get(3, 3), 0.0001f);
    assertEquals(0.0f, m.get(0, 1), 0.0001f);
    assertEquals(0.0f, m.get(2, 3), 0.0001f);
  }

  @Test
  public void testArrayMatrixSetIdentiy() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.setIdentity();
    assertEquals(1.0f, m.get(0, 0), 0.0001f);
    assertEquals(1.0f, m.get(1, 1), 0.0001f);
    assertEquals(1.0f, m.get(2, 2), 0.0001f);
    assertEquals(0.0f, m.get(0, 1), 0.0001f);
    assertEquals(0.0f, m.get(1, 0), 0.0001f);
    assertEquals(0.0f, m.get(0, 2), 0.0001f);
  }

  @Test
  public void testSparseMatrixSetIdentity() {
    SparseMatrix m = new SparseMatrix(4);
    m.set(0, 1, 5.0f);
    m.set(1, 2, 3.0f);
    m.setIdentity();
    assertEquals(1.0f, m.get(0, 0), 0.0001f);
    assertEquals(1.0f, m.get(1, 1), 0.0001f);
    assertEquals(0.0f, m.get(0, 1), 0.0001f);
    assertEquals(0.0f, m.get(1, 2), 0.0001f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixSetNegativeRow() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.set(-1, 0, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixSetNegativeCol() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.set(0, -1, -5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixSetRowOutOfBounds() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.set(3, 0, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixSetColOutOfBounds() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.set(0, 3, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixGetNegativeRow() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.get(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixGetNegativeCol() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.get(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixGetRowOoutOfBounds() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.get(3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixGetColOutOfBounds() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.get(0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixSetNegativeRow() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(-1, 0, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixSetNegativeCol() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(-1, 0, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixSetRowOutOfBounds() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(3, 0, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixSetColOutOfBounds() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(3, 3, 5.0f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixGetNegativeRow() {
    SparseMatrix m = new SparseMatrix(3);
    m.get(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixGetNegativeCol() {
    SparseMatrix m = new SparseMatrix(3);
    m.get(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixGetRowOutOfBounds() {
    SparseMatrix m = new SparseMatrix(3);
    m.get(3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixGetColOutOfBounds() {
    SparseMatrix m = new SparseMatrix(3);
    m.get(0, 3);
  }

  @Test
  public void testArrayMatrixSetAndGet() {
    ArrayMatrix m = new ArrayMatrix(3);
    m.set(0, 0, 1.5f);
    m.set(1, 2, -3.2f);
    m.set(2, 1, 7.8f);
    assertEquals(1.5f, m.get(0, 0), 0.0001f);
    assertEquals(-3.2f, m.get(1, 2), 0.0001f);
    assertEquals(7.8f, m.get(2, 1), 0.0001f);
    assertEquals(0.0f, m.get(0, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixSetAndGet() {
    SparseMatrix m = new SparseMatrix(4);
    m.set(0, 1, 2.5f);
    m.set(2, 3, -1.5f);
    m.set(3, 0, 4.2f);

    assertEquals(2.5f, m.get(0, 1), 0.0001f);
    assertEquals(-1.5f, m.get(2, 3), 0.0001f);
    assertEquals(4.2f, m.get(3, 0), 0.0001f);
    assertEquals(0.0f, m.get(0, 0), 0.0001f);
    assertEquals(0.0f, m.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixUpdateExistingNode() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(1, 1, 5.0f);
    assertEquals(5.0f, m.get(1, 1), 0.0001f);
    m.set(1, 1, 10.0f);
    assertEquals(10.0f, m.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixSetVerySmallValue() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(0, 0, 5.0f);
    m.set(0, 0, 1e-11f);
    assertEquals(0.0f, m.get(0, 0), 0.0001f);
  }

  @Test
  public void testSparseMatrixSetZero() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(1, 2, 8.0f);
    assertEquals(8.0f, m.get(1, 2), 0.0001f);
    m.set(1, 2, 0.0f);
    assertEquals(0.0f, m.get(1, 2), 0.0001f);
  }

  @Test
  public void testSparseMatrixRemoveNonExistingNode() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(0, 0, 0.0f);
    assertEquals(0.0f, m.get(0, 0), 0.0001f);
  }

  @Test
  public void testSparseMatrixGetZeroInMiddleOfRow() {
    SparseMatrix m = new SparseMatrix(5);
    m.set(2, 0, 3.0f);
    m.set(2, 4, 5.0f);
    assertEquals(3.0f, m.get(2, 0), 0.0001f);
    assertEquals(0.0f, m.get(2, 1), 0.0001f);
    assertEquals(0.0f, m.get(2, 2), 0.0001f);
    assertEquals(0.0f, m.get(2, 3), 0.0001f);
    assertEquals(5.0f, m.get(2, 4), 0.0001f);
  }

  @Test
  public void testSparseMatrixInsertAtBeginningOfRow() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(1, 2, 5.0f);
    m.set(1, 0, 3.0f);
    assertEquals(3.0f, m.get(1, 0), 0.0001f);
    assertEquals(5.0f, m.get(1, 2), 0.0001f);
  }

  @Test
  public void testSparseMatrixInsertInMiddleOfRow() {
    SparseMatrix m = new SparseMatrix(5);
    m.set(2, 0, 1.0f);
    m.set(2, 4, 5.0f);
    m.set(2, 2, 3.0f);
    assertEquals(1.0f, m.get(2, 0), 0.0001f);
    assertEquals(3.0f, m.get(2, 2), 0.0001f);
    assertEquals(5.0f, m.get(2, 4), 0.0001f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixAddSizeMismatch() {
    ArrayMatrix m1 = new ArrayMatrix(3);
    ArrayMatrix m2 = new ArrayMatrix(4);
    m1.add(m2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixAddSizeMismatch() {
    SparseMatrix m1 = new SparseMatrix(3);
    SparseMatrix m2 = new SparseMatrix(5);
    m1.add(m2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMixedMatrixAddSizeMismatch() {
    SparseMatrix m1 = new SparseMatrix(3);
    ArrayMatrix m2 = new ArrayMatrix(4);
    m1.add(m2);
  }

  @Test
  public void testArrayMatrixAddArrayMatrix() {
    ArrayMatrix m1 = new ArrayMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(0, 1, 2.0f);
    m1.set(1, 0, 3.0f);
    m1.set(1, 1, 4.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 5.0f);
    m2.set(0, 1, 6.0f);
    m2.set(1, 0, 7.0f);
    m2.set(1, 1, 8.0f);
    SquareMatrix result = m1.add(m2);
    assertEquals(6.0f, result.get(0, 0), 0.0001f);
    assertEquals(8.0f, result.get(0, 1), 0.0001f);
    assertEquals(10.0f, result.get(1, 0), 0.0001f);
    assertEquals(12.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixAddSparseMatrix() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 0, 1.0f);
    m1.set(1, 2, 3.0f);
    SparseMatrix m2 = new SparseMatrix(3);
    m2.set(0, 0, 2.0f);
    m2.set(2, 1, 4.0f);
    SquareMatrix result = m1.add(m2);
    assertEquals(3.0f, result.get(0, 0), 0.0001f);
    assertEquals(3.0f, result.get(1, 2), 0.0001f);
    assertEquals(4.0f, result.get(2, 1), 0.0001f);
    assertEquals(0.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixAddArrayMatrix() {
    SparseMatrix m1 = new SparseMatrix(2);
    m1.set(0, 0, 2.0f);
    m1.set(1, 1, 3.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 1.0f);
    m2.set(0, 1, 2.0f);
    m2.set(1, 0, 3.0f);
    m2.set(1, 1, 4.0f);
    SquareMatrix result = m1.add(m2);
    assertEquals(3.0f, result.get(0, 0), 0.0001f);
    assertEquals(2.0f, result.get(0, 1), 0.0001f);
    assertEquals(3.0f, result.get(1, 0), 0.0001f);
    assertEquals(7.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixAddSparseMatrixResultInZero() {
    SparseMatrix m1 = new SparseMatrix(2);
    m1.set(0, 0, 5.0f);
    m1.set(1, 1, 3.0f);
    SparseMatrix m2 = new SparseMatrix(2);
    m2.set(0, 0, -5.0f);
    m2.set(1, 1, -3.0f);
    SquareMatrix result = m1.add(m2);
    assertEquals(0.0f, result.get(0, 0), 0.0001f);
    assertEquals(0.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixAddSparseMatrixDifferentColumns() {
    SparseMatrix m1 = new SparseMatrix(4);
    m1.set(0, 0, 1.0f);
    m1.set(0, 2, 2.0f);
    SparseMatrix m2 = new SparseMatrix(4);
    m2.set(0, 1, 3.0f);
    m2.set(0, 3, 4.0f);
    SquareMatrix result = m1.add(m2);
    assertEquals(1.0f, result.get(0, 0), 0.0001f);
    assertEquals(3.0f, result.get(0, 1), 0.0001f);
    assertEquals(2.0f, result.get(0, 2), 0.0001f);
    assertEquals(4.0f, result.get(0, 3), 0.0001f);
  }

  @Test
  public void testSparseMatrixAddSparseMatrixOneEmpty() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 0, 5.0f);
    m1.set(1, 1, 7.0f);
    SparseMatrix m2 = new SparseMatrix(3);
    SquareMatrix result = m1.add(m2);
    assertEquals(5.0f, result.get(0, 0), 0.0001f);
    assertEquals(7.0f, result.get(1, 1), 0.0001f);
    assertEquals(0.0f, result.get(2, 2), 0.0001f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixPremulSizeMismatch() {
    ArrayMatrix m1 = new ArrayMatrix(3);
    ArrayMatrix m2 = new ArrayMatrix(4);
    m1.premul(m2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixPremulSizeMismatch() {
    SparseMatrix m1 = new SparseMatrix(3);
    SparseMatrix m2 = new SparseMatrix(2);
    m1.premul(m2);
  }

  @Test
  public void testArrayMatrixPremulArrayMatrix() {
    ArrayMatrix m1 = new ArrayMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(0, 1, 2.0f);
    m1.set(1, 0, 3.0f);
    m1.set(1, 1, 4.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 2.0f);
    m2.set(0, 1, 0.0f);
    m2.set(1, 0, 1.0f);
    m2.set(1, 1, 2.0f);
    SquareMatrix result = m1.premul(m2);
    assertEquals(2.0f, result.get(0, 0), 0.0001f);
    assertEquals(4.0f, result.get(0, 1), 0.0001f);
    assertEquals(7.0f, result.get(1, 0), 0.0001f);
    assertEquals(10.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixPremulSparseMatrix() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 0, 1.0f);
    m1.set(1, 1, 2.0f);
    m1.set(2, 2, 3.0f);
    SparseMatrix m2 = new SparseMatrix(3);
    m2.set(0, 0, 2.0f);
    m2.set(1, 1, 3.0f);
    m2.set(2, 2, 4.0f);
    SquareMatrix result = m1.premul(m2);
    assertEquals(2.0f, result.get(0, 0), 0.0001f);
    assertEquals(6.0f, result.get(1, 1), 0.0001f);
    assertEquals(12.0f, result.get(2, 2), 0.0001f);
  }

  /**
   * correction for testSparseMatrixPremulArrayMatrix that is m2 x m1.
   * [3 4] x [1 0] = [3 8]
   * [5 6] x [0 2] = [5 12]
   */
  @Test
  public void testSparseMatrixPremulArrayMatrix() {
    SparseMatrix m1 = new SparseMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(1, 1, 2.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 3.0f);
    m2.set(0, 1, 4.0f);
    m2.set(1, 0, 5.0f);
    m2.set(1, 1, 6.0f);
    SquareMatrix result = m1.premul(m2);
    assertEquals(3.0f, result.get(0, 0), 0.0001f);
    assertEquals(8.0f, result.get(0, 1), 0.0001f);
    assertEquals(5.0f, result.get(1, 0), 0.0001f);
    assertEquals(12.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixPremulIdentity() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 1, 2.0f);
    m1.set(1, 2, 3.0f);
    m1.set(2, 0, 4.0f);
    SparseMatrix identity = new SparseMatrix(3);
    identity.setIdentity();
    SquareMatrix result = m1.premul(identity);
    assertEquals(0.0f, result.get(0, 0), 0.0001f);
    assertEquals(2.0f, result.get(0, 1), 0.0001f);
    assertEquals(3.0f, result.get(1, 2), 0.0001f);
    assertEquals(4.0f, result.get(2, 0), 0.0001f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayMatrixPostmulSizeMismatch() {
    ArrayMatrix m1 = new ArrayMatrix(3);
    ArrayMatrix m2 = new ArrayMatrix(4);
    m1.postmul(m2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSparseMatrixPostmulSizeMismatch() {
    SparseMatrix m1 = new SparseMatrix(2);
    SparseMatrix m2 = new SparseMatrix(3);
    m1.postmul(m2);
  }

  /**
   * error correction for testArrayMatrixPostmulArrayMatrix.
   * Post Mul for m1 x m2.
   */
  @Test
  public void testArrayMatrixPostmulArrayMatrix() {
    ArrayMatrix m1 = new ArrayMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(0, 1, 2.0f);
    m1.set(1, 0, 3.0f);
    m1.set(1, 1, 4.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 2.0f);
    m2.set(0, 1, 0.0f);
    m2.set(1, 0, 1.0f);
    m2.set(1, 1, 2.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(4.0f, result.get(0, 0), 0.0001f);
    assertEquals(4.0f, result.get(0, 1), 0.0001f);
    assertEquals(10.0f, result.get(1, 0), 0.0001f);
    assertEquals(8.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testSparseMatrixPostmulSparseMatrix() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 0, 1.0f);
    m1.set(1, 1, 2.0f);
    m1.set(2, 2, 3.0f);
    SparseMatrix m2 = new SparseMatrix(3);
    m2.set(0, 0, 2.0f);
    m2.set(1, 1, 3.0f);
    m2.set(2, 2, 4.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(2.0f, result.get(0, 0), 0.0001f);
    assertEquals(6.0f, result.get(1, 1), 0.0001f);
    assertEquals(12.0f, result.get(2, 2), 0.0001f);
  }

  @Test
  public void testSparseMatrixPostmulIdentity() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 1, 2.0f);
    m1.set(1, 2, 3.0f);
    m1.set(2, 0, 4.0f);
    SparseMatrix identity = new SparseMatrix(3);
    identity.setIdentity();
    SquareMatrix result = m1.postmul(identity);
    assertEquals(0.0f, result.get(0, 0), 0.0001f);
    assertEquals(2.0f, result.get(0, 1), 0.0001f);
    assertEquals(3.0f, result.get(1, 2), 0.0001f);
    assertEquals(4.0f, result.get(2, 0), 0.0001f);
  }

  @Test
  public void testSparseMatrixPostmulArrayMatrix() {
    SparseMatrix m1 = new SparseMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(1, 1, 2.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 3.0f);
    m2.set(0, 1, 4.0f);
    m2.set(1, 0, 5.0f);
    m2.set(1, 1, 6.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(3.0f, result.get(0, 0), 0.0001f);
    assertEquals(4.0f, result.get(0, 1), 0.0001f);
    assertEquals(10.0f, result.get(1, 0), 0.0001f);
    assertEquals(12.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testLargeSparseMatrixOperations() {
    int size = 100;
    SparseMatrix m1 = new SparseMatrix(size);
    SparseMatrix m2 = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      m1.set(i, i, i + 1.0f);
      m2.set(i, i, i + 2.0f);
    }
    SquareMatrix result = m1.add(m2);
    assertEquals(3.0f, result.get(0, 0), 0.0001f);
    assertEquals(103.0f, result.get(50, 50), 0.0001f);
    assertEquals(0.0f, result.get(0, 1), 0.0001f);
  }

  /**
   * Sparse x Array which a particular pattern is there in specific way.
   * sparse x array.
   * Row 0 x array = [2x1, 2x0, 2x2] = [2,0,4].
   * Row 1 x array = [3x3, 2x1, 3x0] = [9, 3, 0].
   * Row 2 x array = [1x1+4X2, 1X0+4X2, 1X2+4X1] = [1, 8, 6].
   */
  @Test
  public void testMixedMatrixMultiplication() {
    SparseMatrix sparse = new SparseMatrix(3);
    sparse.set(0, 0, 2.0f);
    sparse.set(1, 1, 3.0f);
    sparse.set(2, 0, 1.0f);
    sparse.set(2, 2, 4.0f);
    ArrayMatrix array = new ArrayMatrix(3);
    array.set(0, 0, 1.0f);
    array.set(0, 1, 0.0f);
    array.set(0, 2, 2.0f);
    array.set(1, 0, 3.0f);
    array.set(1, 1, 1.0f);
    array.set(1, 2, 0.0f);
    array.set(2, 0, 0.0f);
    array.set(2, 1, 2.0f);
    array.set(2, 2, 1.0f);
    SquareMatrix result = sparse.postmul(array);
    assertEquals(2.0f, result.get(0, 0), 0.0001f);
    assertEquals(0.0f, result.get(0, 1), 0.0001f);
    assertEquals(4.0f, result.get(0, 2), 0.0001f);
    assertEquals(9.0f, result.get(1, 0), 0.0001f);
    assertEquals(3.0f, result.get(1, 1), 0.0001f);
    assertEquals(0.0f, result.get(1, 2), 0.0001f);
    assertEquals(1.0f, result.get(2, 0), 0.0001f);
    assertEquals(8.0f, result.get(2, 1), 0.0001f);
    assertEquals(6.0f, result.get(2, 2), 0.0001f);
  }

  /**
   * additional test for m1 x m2 after getting build error.
   * m1 x m2.
   * result = m1 x m2.
   * Row 0 x columns = [5, 0, 0].
   * Row 1 x columns = [0, 12, 0].
   * Row 2 x columns = [0, 0, 12].
   */
  @Test
  public void testSparseMatrixMultiplicationNonDiagonal() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 1, 1.0f);
    m1.set(1, 2, 2.0f);
    m1.set(2, 0, 3.0f);
    SparseMatrix m2 = new SparseMatrix(3);
    m2.set(0, 2, 4.0f);
    m2.set(1, 0, 5.0f);
    m2.set(2, 1, 6.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(5.0f, result.get(0, 0), 0.0001f);
    assertEquals(0.0f, result.get(0, 1), 0.0001f);
    assertEquals(0.0f, result.get(0, 2), 0.0001f);
    assertEquals(0.0f, result.get(1, 0), 0.0001f);
    assertEquals(12.0f, result.get(1, 1), 0.0001f);
    assertEquals(0.0f, result.get(1, 2), 0.0001f);
    assertEquals(0.0f, result.get(2, 0), 0.0001f);
    assertEquals(0.0f, result.get(2, 1), 0.0001f);
    assertEquals(12.0f, result.get(2, 2), 0.0001f);
  }

  @Test
  public void testSparseMatrixPostmulResultsInZero() {
    SparseMatrix m1 = new SparseMatrix(2);
    m1.set(0, 0, 1.0f);
    SparseMatrix m2 = new SparseMatrix(2);
    m2.set(1, 1, 1.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(0.0f, result.get(0, 0), 0.0001f);
    assertEquals(0.0f, result.get(0, 1), 0.0001f);
    assertEquals(0.0f, result.get(1, 0), 0.0001f);
    assertEquals(0.0f, result.get(1, 1), 0.0001f);
  }

  @Test
  public void testMatrixMultiplicactionBasic() {
    ArrayMatrix m1 = new ArrayMatrix(2);
    m1.set(0, 0, 1.0f);
    m1.set(0, 1, 2.0f);
    m1.set(1, 0, 3.0f);
    m1.set(1, 1, 4.0f);
    ArrayMatrix m2 = new ArrayMatrix(2);
    m2.set(0, 0, 5.0f);
    m2.set(0, 1, 6.0f);
    m2.set(1, 0, 7.0f);
    m2.set(1, 1, 8.0f);
    SquareMatrix result = m1.postmul(m2);
    assertEquals(19.0f, result.get(0, 0), 0.0001f);
    assertEquals(22.0f, result.get(0, 1), 0.0001f);
    assertEquals(43.0f, result.get(1, 0), 0.0001f);
    assertEquals(50.0f, result.get(1, 1), 0.0001f);
  }

  /**
   * Covering remaining tests for add generic sparse matrix.
   */
  @Test
  public void testSparseMatrixAddGeneric() {
    SparseMatrix m1 = new SparseMatrix(3);
    m1.set(0, 0, 1.0f);
    m1.set(1, 1, 2.0f);
    ArrayMatrix m2 = new ArrayMatrix(3);
    m2.set(0, 0, 3.0f);
    m2.set(1, 1, 4.0f);
    m2.set(2, 2, 5.0f);
    SquareMatrix result = m1.addGeneric(m2);
    assertEquals(4.0f, result.get(0, 0), 0.0001f);
    assertEquals(6.0f, result.get(1, 1), 0.0001f);
    assertEquals(5.0f, result.get(2, 2), 0.0001f);
  }

  /**
   * tests for remaining coverage for SparseMatrixNodeCases.
   * tests to remove remaining nodes present.
   * tests to remove node which is not present there.
   */
  @Test
  public void testSparseMatrixRemoveNodeCases() {
    SparseMatrix m = new SparseMatrix(3);
    m.set(0, 0, 2.0f);
    m.set(0, 1, 3.0f);
    m.set(1, 1, 4.0f);
    m.set(0, 1, 0.0f);
    assertEquals(0.0f, m.get(0, 1), 0.0001f);
    m.set(2, 2, 0.0f);
    assertEquals(0.0f, m.get(2, 2), 0.0001f);
  }

  @Test
  public void testlargeMixedMatrices1() {
    int size = 100;
    ArrayMatrix dense = new ArrayMatrix(size);
    SparseMatrix sparse = new SparseMatrix(size);
    sparse.setIdentity();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        dense.set(i, j, (i + j) % 5);
      }
    }

    SquareMatrix result = dense.premul(sparse);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(dense.get(i, j), result.get(j, i), 0.0001f);
      }
    }
  }

  @Test
  public void testlargeMixedmatrices2() {
    int size = 555;
    ArrayMatrix dense = new ArrayMatrix(size);
    SparseMatrix sparse = new SparseMatrix(size);
    for (int i = 0; i < size; i += 50) {
      sparse.set(i, i, 2.0f);
    }
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        dense.set(i, j, 1.0f);
      }
    }
    SquareMatrix result = dense.premul(sparse);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float expected = (i % 50 == 0) ? 2.0f : 0.0f;
        assertEquals(expected, result.get(i, j), 0.0001f);
      }
    }
  }

  @Test
  public void testLargeSparseMatrixMultiplication() {
  }
}


