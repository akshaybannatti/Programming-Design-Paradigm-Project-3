import static org.junit.Assert.assertEquals;

import mat.ArrayMatrix;
import mat.SquareMatrix;
import org.junit.Test;



/**
 * Written ArrayMatrix test's.
 */
public class ArrayMatrixTest {

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
    float[][] onevalues = {
        {1, 2, 3, 4},
        {1, 5, 2, 1},
        {0, 1, -1, 2},
        {0, 0, 1, 0}};

    float[][] twovalues = {
        {2, 1, 3, 1},
        {2, 1, 5, 4},
        {-1, 2, 1, 1},
        {1, 1, 1, 1}
    };

    float[][] answer = {
        {3, 3, 6, 5},
        {3, 6, 7, 5},
        {-1, 3, 0, 3},
        {1, 1, 2, 1}
    };
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
}