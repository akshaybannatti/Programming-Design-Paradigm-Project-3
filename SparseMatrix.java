package mat;
package mat;

/**
 * SparseMatrix uses a square matrix using a linked list data structure.
 * stores the non-zero value.
 */
public class SparseMatrix implements SquareMatrix {

  /**
   * Node shows a single non-zero element in the sparse matrix.
   * Each node links to the next non-zero element in row and column.
   */
  private static class Node {
    int row;
    int col;
    float value;
    Node rowNext;
    Node colNext;

    /**
     * Node for a non-zero matrix.
     *
     * @param row   row index
     * @param col   column index
     * @param value without zero value.
     */
    Node(int row, int col, float value) {
      this.row = row;
      this.col = col;
      this.value = value;
      this.rowNext = null;
      this.colNext = null;
    }
  }

  private final int size;
  private final Node[] rowHeads;
  private final Node[] colHeads;

  /**
   * Created a SparseMatrix of given size.
   * At the beginning all nodes are zero & it's not kept.
   *
   * @param size the dimension of the square matrix
   * @throws IllegalArgumentException if size is non-positive
   */
  public SparseMatrix(int size) throws IllegalArgumentException {
    if (size <= 0) {
      throw new IllegalArgumentException("Size > 0");
    }
    this.size = size;
    this.rowHeads = new Node[size];
    this.colHeads = new Node[size];
  }

  @Override
  public void setIdentity() {
    for (int i = 0; i < size; i++) {
      rowHeads[i] = null;
      colHeads[i] = null;
    }
    for (int i = 0; i < size; i++) {
      set(i, i, 1.0f);
    }
  }

  @Override
  public void set(int i, int j, float value) throws IllegalArgumentException {
    if (i < 0 || i >= size || j < 0 || j >= size) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    if (Math.abs(value) < 1e-10f) {
      removeNode(i, j);
    } else {
      insertOrUpdateNode(i, j, value);
    }
  }

  /**
   * Gave a new node in existing node at (row, col).
   * This keeps the nodes in both row and column.
   *
   * @param row   the row index
   * @param col   the column index
   * @param value the non-zero value to set
   */
  private void insertOrUpdateNode(int row, int col, float value) {
    Node previousRow = null;
    Node currentRow = rowHeads[row];

    while (currentRow != null && currentRow.col < col) {
      previousRow = currentRow;
      currentRow = currentRow.rowNext;
    }
    if (currentRow != null && currentRow.col == col) {
      currentRow.value = value;
      return;
    }

    Node newNode = new Node(row, col, value);
    if (previousRow == null) {
      newNode.rowNext = rowHeads[row];
      rowHeads[row] = newNode;
    } else {
      newNode.rowNext = previousRow.rowNext;
      previousRow.rowNext = newNode;
    }
    Node prevInCol = null;
    Node currentInCol = colHeads[col];

    while (currentInCol != null && currentInCol.row < row) {
      prevInCol = currentInCol;
      currentInCol = currentInCol.colNext;
    }

    if (prevInCol == null) {
      newNode.colNext = colHeads[col];
      colHeads[col] = newNode;
    } else {
      newNode.colNext = prevInCol.colNext;
      prevInCol.colNext = newNode;
    }
  }

  /**
   * Removes a node from the place (row, col) if it's already there.
   * When value is zero.
   *
   * @param row the row.
   * @param col the column.
   */
  private void removeNode(int row, int col) {
    Node previousRow = null;
    Node currentRow = rowHeads[row];

    while (currentRow != null && currentRow.col < col) {
      previousRow = currentRow;
      currentRow = currentRow.rowNext;
    }
    if (currentRow == null || currentRow.col != col) {
      return;
    }
    if (previousRow == null) {
      rowHeads[row] = currentRow.rowNext;
    } else {
      previousRow.rowNext = currentRow.rowNext;
    }
    Node prevInCol = null;
    Node currentInCol = colHeads[col];

    while (currentInCol != null && currentInCol.row < row) {
      prevInCol = currentInCol;
      currentInCol = currentInCol.colNext;
    }

    if (prevInCol == null) {
      colHeads[col] = currentInCol.colNext;
    } else {
      prevInCol.colNext = currentInCol.colNext;
    }
  }

  @Override
  public float get(int i, int j) throws IllegalArgumentException {
    if (i < 0 || i >= size || j < 0 || j >= size) {
      throw new IllegalArgumentException("Index not in bounds");
    }

    Node current = rowHeads[i];
    while (current != null) {
      if (current.col == j) {
        return current.value;
      }
      if (current.col > j) {
        break;
      }
      current = current.rowNext;
    }
    return 0.0f;
  }

  @Override
  public SquareMatrix add(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size of the matrix is not matching");
    }
    if (other instanceof ArrayMatrix) {
      return addWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return addWithSparseMatrix((SparseMatrix) other);
    }
    return addGeneric(other);
  }

  /**
   * Did this for the addition when other matrix is ArrayMatrix.
   * This uses this matrix which only has non-zero values.
   *
   * @param other the ArrayMatrix to add
   * @return the sum of the two matrices
   */
  private SquareMatrix addWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      Node current = rowHeads[i];
      while (current != null) {
        float sum = current.value + other.get(i, current.col);
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, current.col, sum);
        }
        current = current.rowNext;
      }
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (this.get(i, j) == 0.0f) {
          float val = other.get(i, j);
          if (Math.abs(val) >= 1e-10f) {
            result.set(i, j, val);
          }
        }
      }
    }

    return result;
  }

  /**
   * Made the addition better when both matrices are SparseMatrix.
   * Only iterates through non-zero elements of both matrices.
   *
   * @param other the SparseMatrix to add
   * @return the sum of the two matrices
   */
  private SquareMatrix addWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      Node curr1 = this.rowHeads[i];
      Node curr2 = other.rowHeads[i];

      while (curr1 != null || curr2 != null) {
        if (curr1 == null) {
          result.set(i, curr2.col, curr2.value);
          curr2 = curr2.rowNext;
        } else if (curr2 == null) {
          result.set(i, curr1.col, curr1.value);
          curr1 = curr1.rowNext;
        } else if (curr1.col < curr2.col) {
          result.set(i, curr1.col, curr1.value);
          curr1 = curr1.rowNext;
        } else if (curr1.col > curr2.col) {
          result.set(i, curr2.col, curr2.value);
          curr2 = curr2.rowNext;
        } else {
          float sum = curr1.value + curr2.value;
          if (Math.abs(sum) >= 1e-10f) {
            result.set(i, curr1.col, sum);
          }
          curr1 = curr1.rowNext;
          curr2 = curr2.rowNext;
        }
      }
    }

    return result;
  }

  /**
   * Addition for unknown matrix type's.
   *
   * @param other matrix to add.
   * @return the sum of the two matrices.
   */
  public SquareMatrix addGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = this.get(i, j) + other.get(i, j);
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public SquareMatrix premul(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size doesn't have any effect");
    }
    if (other instanceof ArrayMatrix && this instanceof SparseMatrix) {
      return premulWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return premulWithSparseMatrix((SparseMatrix) other);
    }

    return premulGeneric(other);
  }

  /**
   * Used this for pre-multiplication and the other with this where other is ArrayMatrix.
   * Leverages column traversal of this sparse matrix.
   *
   * @param other the ArrayMatrix to pre-multiply
   * @return the product other * this
   */
  private SquareMatrix premulWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;

        Node current = colHeads[j];
        while (current != null) {
          sum += other.get(i, current.row) * current.value;
          current = current.colNext;
        }

        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }

  /**
   * Did this for pre-mul and other with this where both are SparseMatrix.
   * Only iterates from non-zero elements.
   *
   * @param other from SparseMatrix to pre-multiply.
   * @return the product other.
   */
  private SquareMatrix premulWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node anotherNode = other.rowHeads[i];

      while (anotherNode != null) {
        int k = anotherNode.col;
        float otherValue = anotherNode.value;

        Node thisNode = this.rowHeads[k];
        while (thisNode != null) {
          int j = thisNode.col;
          float thisValue = thisNode.value;

          float currentValue = result.get(i, j);
          result.set(i, j, currentValue + otherValue * thisValue);

          thisNode = thisNode.rowNext;
        }

        anotherNode = anotherNode.rowNext;
      }
    }

    return result;
  }

  /**
   * pre-mul used for unknown matrix.
   *
   * @param other matrix use for pre-multiply.
   * @return the product other with this one.
   */
  private SquareMatrix premulGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;
        for (int k = 0; k < size; k++) {
          sum += other.get(i, k) * this.get(k, j);
        }
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public SquareMatrix postmul(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size does not match for the Matrix");
    }
    if (other instanceof ArrayMatrix) {
      return postmulWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return postmulWithSparseMatrix((SparseMatrix) other);
    }

    return postmulGeneric(other);
  }

  /**
   * Did the post-multiplication with this and with other one where other is ArrayMatrix.
   * Leverages row traversal of this sparse matrix.
   *
   * @param other the ArrayMatrix to post-multiply.
   * @return the product this * other
   */
  private SquareMatrix postmulWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node current = rowHeads[i];

      while (current != null) {
        int k = current.col;
        float thisValue = current.value;
        for (int j = 0; j < size; j++) {
          float product = thisValue * other.get(k, j);
          if (Math.abs(product) >= 1e-10f) {
            float currentValue = result.get(i, j);
            result.set(i, j, currentValue + product);
          }
        }

        current = current.rowNext;
      }
    }

    return result;
  }

  /**
   * Optimized post-multiplication with this and other where both are SparseMatrix.
   * Only loops through non-zero elements.
   *
   * @param other the SparseMatrix to post-multiply.
   * @return the product this with the other.
   */
  private SquareMatrix postmulWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node thisNode = this.rowHeads[i];

      while (thisNode != null) {
        int k = thisNode.col;
        float thisValue = thisNode.value;

        Node anotherNode = other.rowHeads[k];
        while (anotherNode != null) {
          int j = anotherNode.col;
          float otherValue = anotherNode.value;

          float currentValue = result.get(i, j);
          result.set(i, j, currentValue + thisValue * otherValue);

          anotherNode = anotherNode.rowNext;
        }

        thisNode = thisNode.rowNext;
      }
    }

    return result;
  }

  /**
   * post-multiplication for unknown matrix.
   *
   * @param other the matrix to post-multiply.
   * @return the product this * other.
   */
  private SquareMatrix postmulGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;
        for (int k = 0; k < size; k++) {
          sum += this.get(i, k) * other.get(k, j);
        }
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public int size() {
    return size;
  }
}
/**
 * SparseMatrix uses a square matrix using a linked list data structure.
 * stores the non-zero value.
 */
public class SparseMatrix implements SquareMatrix {

  /**
   * Node shows a single non-zero element in the sparse matrix.
   * Each node links to the next non-zero element in row and column.
   */
  private static class Node {
    int row;
    int col;
    float value;
    Node rowNext;
    Node colNext;

    /**
     * Node for a non-zero matrix.
     *
     * @param row   row index
     * @param col   column index
     * @param value without zero value.
     */
    Node(int row, int col, float value) {
      this.row = row;
      this.col = col;
      this.value = value;
      this.rowNext = null;
      this.colNext = null;
    }
  }

  private final int size;
  private final Node[] rowHeads;
  private final Node[] colHeads;

  /**
   * Created a SparseMatrix of given size.
   * At the beginning all nodes are zero & it's not kept.
   *
   * @param size the dimension of the square matrix
   * @throws IllegalArgumentException if size is non-positive
   */
  public SparseMatrix(int size) throws IllegalArgumentException {
    if (size <= 0) {
      throw new IllegalArgumentException("Size > 0");
    }
    this.size = size;
    this.rowHeads = new Node[size];
    this.colHeads = new Node[size];
  }

  @Override
  public void setIdentity() {
    for (int i = 0; i < size; i++) {
      rowHeads[i] = null;
      colHeads[i] = null;
    }
    for (int i = 0; i < size; i++) {
      set(i, i, 1.0f);
    }
  }

  @Override
  public void set(int i, int j, float value) throws IllegalArgumentException {
    if (i < 0 || i >= size || j < 0 || j >= size) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    if (Math.abs(value) < 1e-10f) {
      removeNode(i, j);
    } else {
      insertOrUpdateNode(i, j, value);
    }
  }

  /**
   * Gave a new node in existing node at (row, col).
   * This keeps the nodes in both row and column.
   *
   * @param row   the row index
   * @param col   the column index
   * @param value the non-zero value to set
   */
  private void insertOrUpdateNode(int row, int col, float value) {
    Node previousRow = null;
    Node currentRow = rowHeads[row];

    while (currentRow != null && currentRow.col < col) {
      previousRow = currentRow;
      currentRow = currentRow.rowNext;
    }
    if (currentRow != null && currentRow.col == col) {
      currentRow.value = value;
      return;
    }

    Node newNode = new Node(row, col, value);
    if (previousRow == null) {
      newNode.rowNext = rowHeads[row];
      rowHeads[row] = newNode;
    } else {
      newNode.rowNext = previousRow.rowNext;
      previousRow.rowNext = newNode;
    }
    Node prevInCol = null;
    Node currentInCol = colHeads[col];

    while (currentInCol != null && currentInCol.row < row) {
      prevInCol = currentInCol;
      currentInCol = currentInCol.colNext;
    }

    if (prevInCol == null) {
      newNode.colNext = colHeads[col];
      colHeads[col] = newNode;
    } else {
      newNode.colNext = prevInCol.colNext;
      prevInCol.colNext = newNode;
    }
  }

  /**
   * Removes a node from the place (row, col) if it's already there.
   * When value is zero.
   *
   * @param row the row.
   * @param col the column.
   */
  private void removeNode(int row, int col) {
    Node previousRow = null;
    Node currentRow = rowHeads[row];

    while (currentRow != null && currentRow.col < col) {
      previousRow = currentRow;
      currentRow = currentRow.rowNext;
    }
    if (currentRow == null || currentRow.col != col) {
      return;
    }
    if (previousRow == null) {
      rowHeads[row] = currentRow.rowNext;
    } else {
      previousRow.rowNext = currentRow.rowNext;
    }
    Node prevInCol = null;
    Node currentInCol = colHeads[col];

    while (currentInCol != null && currentInCol.row < row) {
      prevInCol = currentInCol;
      currentInCol = currentInCol.colNext;
    }

    if (prevInCol == null) {
      colHeads[col] = currentInCol.colNext;
    } else {
      prevInCol.colNext = currentInCol.colNext;
    }
  }

  @Override
  public float get(int i, int j) throws IllegalArgumentException {
    if (i < 0 || i >= size || j < 0 || j >= size) {
      throw new IllegalArgumentException("Index not in bounds");
    }

    Node current = rowHeads[i];
    while (current != null) {
      if (current.col == j) {
        return current.value;
      }
      if (current.col > j) {
        break;
      }
      current = current.rowNext;
    }
    return 0.0f;
  }

  @Override
  public SquareMatrix add(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size of the matrix is not matching");
    }
    if (other instanceof ArrayMatrix) {
      return addWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return addWithSparseMatrix((SparseMatrix) other);
    }
    return addGeneric(other);
  }

  /**
   * Did this for the addition when other matrix is ArrayMatrix.
   * This uses this matrix which only has non-zero values.
   *
   * @param other the ArrayMatrix to add
   * @return the sum of the two matrices
   */
  private SquareMatrix addWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      Node current = rowHeads[i];
      while (current != null) {
        float sum = current.value + other.get(i, current.col);
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, current.col, sum);
        }
        current = current.rowNext;
      }
    }

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (this.get(i, j) == 0.0f) {
          float val = other.get(i, j);
          if (Math.abs(val) >= 1e-10f) {
            result.set(i, j, val);
          }
        }
      }
    }

    return result;
  }

  /**
   * Made the addition better when both matrices are SparseMatrix.
   * Only iterates through non-zero elements of both matrices.
   *
   * @param other the SparseMatrix to add
   * @return the sum of the two matrices
   */
  private SquareMatrix addWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      Node curr1 = this.rowHeads[i];
      Node curr2 = other.rowHeads[i];

      while (curr1 != null || curr2 != null) {
        if (curr1 == null) {
          result.set(i, curr2.col, curr2.value);
          curr2 = curr2.rowNext;
        } else if (curr2 == null) {
          result.set(i, curr1.col, curr1.value);
          curr1 = curr1.rowNext;
        } else if (curr1.col < curr2.col) {
          result.set(i, curr1.col, curr1.value);
          curr1 = curr1.rowNext;
        } else if (curr1.col > curr2.col) {
          result.set(i, curr2.col, curr2.value);
          curr2 = curr2.rowNext;
        } else {
          float sum = curr1.value + curr2.value;
          if (Math.abs(sum) >= 1e-10f) {
            result.set(i, curr1.col, sum);
          }
          curr1 = curr1.rowNext;
          curr2 = curr2.rowNext;
        }
      }
    }

    return result;
  }

  /**
   * Addition for unknown matrix type's.
   *
   * @param other matrix to add.
   * @return the sum of the two matrices.
   */
  public SquareMatrix addGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = this.get(i, j) + other.get(i, j);
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public SquareMatrix premul(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size doesn't have any effect");
    }
    if (other instanceof ArrayMatrix && this instanceof SparseMatrix) {
      return premulWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return premulWithSparseMatrix((SparseMatrix) other);
    }

    return premulGeneric(other);
  }

  /**
   * Used this for pre-multiplication and the other with this where other is ArrayMatrix.
   * Leverages column traversal of this sparse matrix.
   *
   * @param other the ArrayMatrix to pre-multiply
   * @return the product other * this
   */
  private SquareMatrix premulWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;

        Node current = colHeads[j];
        while (current != null) {
          sum += other.get(i, current.row) * current.value;
          current = current.colNext;
        }

        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }

    return result;
  }

  /**
   * Did this for pre-mul and other with this where both are SparseMatrix.
   * Only iterates from non-zero elements.
   *
   * @param other from SparseMatrix to pre-multiply.
   * @return the product other.
   */
  private SquareMatrix premulWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node anotherNode = other.rowHeads[i];

      while (anotherNode != null) {
        int k = anotherNode.col;
        float otherValue = anotherNode.value;

        Node thisNode = this.rowHeads[k];
        while (thisNode != null) {
          int j = thisNode.col;
          float thisValue = thisNode.value;

          float currentValue = result.get(i, j);
          result.set(i, j, currentValue + otherValue * thisValue);

          thisNode = thisNode.rowNext;
        }

        anotherNode = anotherNode.rowNext;
      }
    }

    return result;
  }

  /**
   * pre-mul used for unknown matrix.
   *
   * @param other matrix use for pre-multiply.
   * @return the product other with this one.
   */
  private SquareMatrix premulGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;
        for (int k = 0; k < size; k++) {
          sum += other.get(i, k) * this.get(k, j);
        }
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public SquareMatrix postmul(SquareMatrix other) throws IllegalArgumentException {
    if (other.size() != this.size) {
      throw new IllegalArgumentException("Size does not match for the Matrix");
    }
    if (other instanceof ArrayMatrix) {
      return postmulWithArrayMatrix((ArrayMatrix) other);
    } else if (other instanceof SparseMatrix) {
      return postmulWithSparseMatrix((SparseMatrix) other);
    }

    return postmulGeneric(other);
  }

  /**
   * Did the post-multiplication with this and with other one where other is ArrayMatrix.
   * Leverages row traversal of this sparse matrix.
   *
   * @param other the ArrayMatrix to post-multiply.
   * @return the product this * other
   */
  private SquareMatrix postmulWithArrayMatrix(ArrayMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node current = rowHeads[i];

      while (current != null) {
        int k = current.col;
        float thisValue = current.value;
        for (int j = 0; j < size; j++) {
          float product = thisValue * other.get(k, j);
          if (Math.abs(product) >= 1e-10f) {
            float currentValue = result.get(i, j);
            result.set(i, j, currentValue + product);
          }
        }

        current = current.rowNext;
      }
    }

    return result;
  }

  /**
   * Optimized post-multiplication with this and other where both are SparseMatrix.
   * Only loops through non-zero elements.
   *
   * @param other the SparseMatrix to post-multiply.
   * @return the product this with the other.
   */
  private SquareMatrix postmulWithSparseMatrix(SparseMatrix other) {
    SparseMatrix result = new SparseMatrix(size);

    for (int i = 0; i < size; i++) {
      Node thisNode = this.rowHeads[i];

      while (thisNode != null) {
        int k = thisNode.col;
        float thisValue = thisNode.value;

        Node anotherNode = other.rowHeads[k];
        while (anotherNode != null) {
          int j = anotherNode.col;
          float otherValue = anotherNode.value;

          float currentValue = result.get(i, j);
          result.set(i, j, currentValue + thisValue * otherValue);

          anotherNode = anotherNode.rowNext;
        }

        thisNode = thisNode.rowNext;
      }
    }

    return result;
  }

  /**
   * post-multiplication for unknown matrix.
   *
   * @param other the matrix to post-multiply.
   * @return the product this * other.
   */
  private SquareMatrix postmulGeneric(SquareMatrix other) {
    SparseMatrix result = new SparseMatrix(size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        float sum = 0.0f;
        for (int k = 0; k < size; k++) {
          sum += this.get(i, k) * other.get(k, j);
        }
        if (Math.abs(sum) >= 1e-10f) {
          result.set(i, j, sum);
        }
      }
    }
    return result;
  }

  @Override
  public int size() {
    return size;
  }
}