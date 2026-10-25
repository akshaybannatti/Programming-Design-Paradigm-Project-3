Assignment 3 -- Matrices
=========


# 1 Introduction

$$
\begin{bmatrix}
2.5 & 8 & -3.2 \\
2 & -1.23 & 5 \\
1 & -1 & 2
\end{bmatrix}
$$

Matrices are rectangular arrangement of numbers (see above for an illustration). Rows are horizontal and columns are vertical. Given a matrix $P$, the number in the $i^{th}$ row and $j^{th}$ column is referred to as $P(i,j)$. For example, in the above illustration $P(0,2)=-3.2$. Matrices are used in many scientific and mathematical applications. In this assignment you will work with several implementations of matrices. We will only work with square matrices (i.e. matrices that have the same number of rows as columns). The above example is a square matrix.

Several arithmetic operations on matrices are possible:

   * Addition/subtraction: Two square matrices $M$ and $N$ can be added by adding/subtracting corresponding elements to create a square matrix of the same size. $M$ and $N$ must be of the same size.

   * Multiplication: Given two square matrices $M$ and $N$, they can be multiplied to produce $P = MN$. This is done element-by-element: $P(i,j)$ is calculated by multiplying corresponding numbers of the $i^{th}$ row of $M$ and the $j^{th}$ column of $N$. $M$ and $N$ must be of the same size. It is important to note that unlike addition, matrix multiplication is not symmetric. In other words, $MN \neq NM$. Given $M$ and $N$, the product $MN$ can be described as "pre-multiplying $N$ with $M$", or "post-multiplying $M$ with $N$".

An identity matrix (often referred as $I$) is a square matrix where only the diagonal elements ($P(i,i)$) are 1, and all other elements are $0$. An identity matrix is the equivalent of the number 1: $IP = PI = P$.

# 2 What to do

## 2.1 Provided code

The `SquareMatrix` interface provided to you represents a square matrix. You are not allowed to add or edit any methods in this interface. Your task is to implement this interface twice in this assignment.

All new code must reside in the `mat` package. All tests should continue to be in the default package.

## 2.2 Implementation 1: `ArrayMatrix`

You must implement the provided `SquareMatrix` interface in a class named `ArrayMatrix`. This class uses a 2D array to represent a square matrix and implements all its operations accordingly. This class should have only one public constructor that takes a single argument: the size of the matrix. A non-positive size should result in an `IllegalArgumentException`. 

You have also been provided a test class that contains two relatively simple tests. You are expected to add more tests for this class as appropriate. 

## 2.3 The Problem with `ArrayMatrix`

For a matrix of size $n$, an `ArrayMatrix` object needs an array of size $n^2$. This is otherwise a tight representation, but in some applications this is problematic. 

Many scientific applications involve very large square matrices, with sizes ranging in the hundreds or thousands. However most of the elements in these matrices are $0$. These are called "sparse" matrices. A simple example of a sparse matrix is an identity matrix. An array representation of sparse matrices is very inefficient: an array matrix that represents an identity matrix of size $1000$ stores $1000000$ numbers, only $1000$ of which are non-zero! Operations such as addition and multiplication of matrices are also much more inefficient when they are sparse. You can see an example of this by running the provided tests: one of the tests creates large sparse matrices and that test takes a long time to run.

It is much more efficient to represent only the non-zero elements of such a matrix. An entry in such a matrix stores not only the value, but also its row and column explicitly. This creates two challenges:

   * The indexing scheme is no longer trivial: getting $P(i,j)$ is harder. Note that the `ArrayMatrix` implementation heavily depends on this indexing.

   * While one may choose to represent either rows or columns using arrays, operations require traversing rows and columns. In such cases, the dimension that is not represented as an array will have an expensive traversal.

## 2.4 Sparse Matrix Representation

A sparse matrix can be represented using a linked data structure. Specifically consider the following sparse matrix:

$$
\begin{bmatrix} 
0 & 8 & 0 & 1\\ 
0 & 0 & 5 & 0\\ 
1 & -1 & 2 & 0 \\ 
1 & 0 & 1 & 0 
\end{bmatrix}
$$

This can be represented in linked form as follows:

 ![Image not loaded](pics/sparse-matrix-example.png)

Implement the provided `SquareMatrix` interface again in a class called `SparseMatrix`. This class represents a matrix using links. **The above picture is just an illustration, and you are not expected to use only and exactly this representation.** 

Your implementation must have the following characteristics:

   * Like `ArrayMatrix`, this class should have only one public constructor that takes its size as the only argument.

   * At any point of time, the matrix should store only non-zero values. This means that when a non-zero value is set to 0, its node should no longer exist in the matrix.

   * Your matrix should be represented in linked form.

   * **Every non-zero value in the matrix should be present exactly once in your matrix representation.**

   * From a non-zero value, it should be possible to directly go to the previous and next non-zero value in its row, and its column. This allows efficient traversal of rows and columns starting from any element within them.

   * All methods in this class should be implemented directly using this representation. That is, you are not allowed to convert a row, column or matrix into an array/list/set/map, complete the operation in that form and then convert back to your linked form. **Doing so will take an order of magnitude more memory, so this restriction is imposed so that your implementation is significantly more efficient than the `ArrayMatrix` implementation when the matrices are sparse.**

In addition, binary operations on matrices (add, pre- and post-multiply) should be "optimized":

   * If both matrices are `ArrayMatrix`, it should use the given implementation of these operations.

   * If both matrices are `SparseMatrix`, it should use an implementation that leverages this for both (if implemented correctly, these operations should be significantly faster than the given implementation if the matrices are truly sparse).

   * If the types are mixed, then you must optimize for the `SparseMatrix`. This means that whenever a matrix is known to be a sparse matrix, you should leverage the fact that it only stores non-zero values.

Finally, while you can change the provided `ArrayMatrix` class to improve its code or design, its list of public methods should remain exactly the same. You are also not allowed to change the `SquareMatrix` interface at all.

Beyond these restrictions, you are allowed to use any classes and interfaces from the JDK. You may create other interfaces and classes as you wish. 

**Hint:** If your implementations are correct and well-designed, the provided test that works very slowly when both matrices are `ArrayMatrix` should be considerably faster only when at least one of them is a `SparseMatrix`. In that sense, the tests on the server will be checking for efficiency through test timeouts.

## 2.5 Testing

You are expected to thoroughly test both implementations of the `SquareMatrix` interface. The provided tests for `ArrayMatrix` are largely for illustrative purposes: they are not complete, and you are expected to add more tests for it. You may choose to directly test any supporting classes, or rely on the assumption that because you tested the above implementations thoroughly, the supporting classes work correctly. Please note that this assumption makes sense **only** if you did thoroughly test the matrix implementations!

Please review the testing guidelines in Canvas module 0. You can find out more about how to test for exceptions in Canvas under the module "Java Safari".

# 3 Documentation

We expect your code to be well-commented. The expectations are: 

   * Each interface and class contains a comment above it explaining specifically what it represents. This should be in plain language, understandable by anybody wishing to use it. Comment above a class should be specific: it should not merely state that it is an implementation of a particular interface.

   * Each public method in the interface should have information about what this method accomplishes (purpose), the nature and explanation of any arguments, return values and exceptions thrown by it and whether it changes the calling object in any way (contract).

   * If a class implements a method declared in an interface that it implements, **and** the comments in the interface describe this implementation completely and accurately, there is no need to replicate that documentation in the class.

   * All comments should be in Javadoc-style.

# 4 What to submit

Please submit all your source files and tests in a single zip file (including the code that was provided to you).

# 5 Criteria for grading
 You will be graded on: 

   1. The correctness of your methods.

   2. Whether your implementations obey the restrictions specified in the assignment

   3. How well you have tested your methods.

   4. Whether you have written enough comments for your classes and methods, and whether they are in proper Javadoc style.

   5. Whether you have placed appropriate restrictions on attributes and methods.

   6. Whether your code is modular and non-repetitive, and each method/class has been designed suitably.

   7. Whether your code is formatted correctly (according to the style grader).

   8. Your code will be assessed for code coverage and mutation testing.

   9. Avoid code smells (review code smells repository in Canvas module 0).

# 6 Development Environment

The project is configured as a gradle project (similar to lab 1). The gradle
tasks are configured in the `build.gradle` file. You
can look at the file to see the list of tasks allowed.
We highly recommend that you do not change this file
unless you know what you are doing. You can run the following
commands from a terminal:

1. Clean and build will run the checkstyle and the tests

   `$ ./gradlew clean build`

   You can view the style report for the source code by opening the file `build/reports/checkstyle/main.html` in a browser.
   You can view the style report for the test code by opening the file `build/reports/checkstyle/test.html` in a browser.
2. Run only tests

   `$ ./gradlew test`

   You can view the test report by opening the file `build/reports/tests/test/index.html` in a browser.
   You can view the jacoco coverage report by opening the file `build/reports/jacoco/test/html/index.html` in a browser.
3. Run PIT mutation

   `$ ./gradlew pitest`

   You can view the test report by opening the file `build/reports/pitest/index.html` in a browser.

If you are on Windows replace the `./gradlew` with `./gradlew.bat`.

If you do not want to use the terminal open
the gradle tool window under **View -> Tool Windows -> Gradle**.
Select the gradle task that you want to run from the gradle menu.

# 7 Submission

Submit your code to ypur private git repository on Pawtograder. Review the instructions [here](https://docs.pawtograder.com/students/assignments/create-submission).
Remember submitting to Pawtograder is the same as pushing to a remote git repository. Here are some useful commands for the terminal:

```bash
git add <files>  # or use . to add all files
git commit -m "your commit message"
git push
```

**The following files are required for submission**, that is, the autograder will not run if any of these files are missing.
- 'src/main/java/mat/SquareMatrix.java'
- 'src/main/java/mat/ArrayMatrix.java'
- 'src/main/java/mat/SparseMatrix.java'
- 'src/test/java/MatrixTests.java'

You may add other files as needed.

Additional notes for submission:
- Your implementation and tests must follow the package structure outlined above, otherwise, they won't be considered for grading.
- Fix any style errors before submitting code, otherwise your submission will not be considered for grading.
- Follow the expected naming conventions for classes and test files. Otherwise, your submission will not be considered for grading.
- You can push code up to three times an hour. The rate limiting is to encourage you to test your code locally before pushing.
- When you push code, your solution will be graded against instructor tests. Some will be visible to you and some will not. You will get some feedback on the tests that are visible to you. For hidden tests, you will only see their results when the grades are released after manual grading.
- The tests you write will be assessed for completeness and correctness using mutation testing and code coverage.
- You can push code multiple times before the deadline. The last submission before the deadline will be considered for grading.
- You can take up to 1 late token (if available) for this assignment. Read how late tokens are applied [here](https://docs.pawtograder.com/students/assignments/late-tokens).
- ** There is a self-review component to this assignment. You must complete the self-review 24 hrs after the due date. ** Read how to take a self review [here](https://docs.pawtograder.com/students/assignments/self-review).
