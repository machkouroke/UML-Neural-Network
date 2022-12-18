package lop.utilities;

import javafx.util.Pair;
import lop.exception.DimensionMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Matrix implements SerializationUtil {
    private static final long serialversionUID = 1L;
    public double[][] data;

    public double[][] getData() {
        return data;
    }

    public void setData(int x, int y, double value) {
        this.data[x][y] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    int rows, cols;


    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    public Matrix(double[][] data) {
        this.data = Arrays.stream(data)
                .map(double[]::clone)
                .toArray(s -> data.clone());

        this.rows = data.length;
        this.cols = data[0].length;
    }

    public Matrix(List<List<Double>> data) {
        this.data = data.stream().
                map(x -> x.stream().mapToDouble(Double::doubleValue).toArray()).
                collect(Collectors.toList()).
                toArray(new double[0][0]);

        this.rows = data.size();
        this.cols = data.get(0).size();
    }


    public static Matrix add(Matrix a, Matrix b) throws DimensionMismatchException {
        if (a.cols != b.cols || a.rows != b.rows) {
            throw new DimensionMismatchException("Matrix dimensions must agree.");
        }
        Matrix temp = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[i][j] = a.data[i][j] + b.data[i][j];
            }
        }
        return temp;
    }

    public static Matrix subtract(Matrix a, Matrix b) throws DimensionMismatchException {
        return Matrix.add(a, Matrix.dot(b, -1));
    }

    public static Matrix transpose(Matrix a) {
        Matrix temp = new Matrix(a.cols, a.rows);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[j][i] = a.data[i][j];
            }
        }
        return temp;
    }

    public static Matrix fromArray(double[] x) {
        Matrix temp = new Matrix(x.length, 1);
        for (int i = 0; i < x.length; i++)
            temp.data[i][0] = x[i];
        return temp;
    }

    /**
     * Effectue une multiplication matricielle entre A et B de la forme A.B
     *
     * @param a Matrice A
     * @param b Matrice B
     * @return Matrice résultat A.B
     * @throws DimensionMismatchException Si les dimensions des matrices ne sont pas compatibles
     */
    public static Matrix dot(Matrix a, Matrix b) throws DimensionMismatchException {
        if (a.cols != b.rows) {
            throw new DimensionMismatchException("Number of columns of first matrix must be " +
                    "equal to number of rows of second matrix.");
        }
        Matrix temp = new Matrix(a.rows, b.cols);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                temp.data[i][j] = sum;
            }
        }
        return temp;
    }

    /**
     * Effectue une multiplication terme à terme entre A et B de la forme A*B
     *
     * @param a Matrice A
     * @param b Matrice B
     * @return Matrice résultat A*B
     * @throws DimensionMismatchException Si les dimensions des matrices ne sont pas compatibles
     */
    public static Matrix multiply(Matrix a, Matrix b) throws DimensionMismatchException {
        if (a.cols != b.cols || a.rows != b.rows) {
            throw new DimensionMismatchException("Matrix dimensions must agree.");
        }
        Matrix temp = new Matrix(a.rows, b.cols);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.cols; j++) {
                temp.data[i][j] = a.data[i][j] * b.data[i][j];
            }
        }
        return temp;
    }

    /**
     * Effectue une multiplication par un scalaire c de la forme c*A
     *
     * @param a Matrice A
     * @param c Scalaire c
     * @return Matrice résultat c*A
     * @throws DimensionMismatchException Si les dimensions des matrices ne sont pas compatibles.
     *                                    Elle n'est jamais lancée car les deux matrices seront de mêmes tailles
     */
    public static Matrix dot(Matrix a, double c) throws DimensionMismatchException {
        return Matrix.multiply(a, Matrix.fromNumber(c, a.rows, a.cols));
    }

    /**
     * Crée une matrice de taille rows*cols remplie de x.
     *
     * @param x    Scalaire x
     * @param rows Nombre de lignes
     * @param cols Nombre de colonnes
     * @return Matrice de taille rows*cols remplie de x.
     */
    public static Matrix fromNumber(double x, int rows, int cols) {
        Matrix temp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.data[i][j] = x;
            }
        }
        return temp;
    }

    /**
     * @return
     */
    public Matrix sum(int axis) {
        if (axis == 0) {
            Matrix temp = new Matrix(1, this.cols);
            for (int i = 0; i < this.cols; i++) {
                double sum = 0;
                for (int j = 0; j < this.rows; j++) {
                    sum += this.data[j][i];
                }
                temp.data[0][i] = sum;
            }
            return temp;
        } else if (axis == 1) {
            Matrix temp = new Matrix(this.rows, 1);
            for (int i = 0; i < this.rows; i++) {
                double sum = 0;
                for (int j = 0; j < this.cols; j++) {
                    sum += this.data[i][j];
                }
                temp.data[i][0] = sum;
            }
            return temp;
        } else {
            throw new IllegalArgumentException("Axis must be 0 or 1");
        }
    }


    public static Matrix cat(Matrix a, Matrix b, int axis) throws DimensionMismatchException {
        if (axis == 0) {
            if (a.cols != b.cols) {
                throw new DimensionMismatchException("Matrices must have the same number of columns.");
            }
            Matrix temp = new Matrix(a.rows + b.rows, a.cols);
            for (int i = 0; i < a.rows; i++) {
                if (a.cols >= 0) System.arraycopy(a.data[i], 0, temp.data[i], 0, a.cols);
            }
            for (int i = 0; i < b.rows; i++) {
                if (b.cols >= 0) System.arraycopy(b.data[i], 0, temp.data[i + a.rows], 0, b.cols);
            }
            return temp;
        } else if (axis == 1) {
            if (a.rows != b.rows) {
                throw new DimensionMismatchException("Matrices must have the same number of rows.");
            }
            Matrix temp = new Matrix(a.rows, a.cols + b.cols);
            for (int i = 0; i < a.rows; i++) {
                if (a.cols >= 0) System.arraycopy(a.data[i], 0, temp.data[i], 0, a.cols);
            }
            for (int i = 0; i < b.rows; i++) {
                if (b.cols >= 0) System.arraycopy(b.data[i], 0, temp.data[i], a.cols, b.cols);
            }
            return temp;
        } else {
            throw new IllegalArgumentException("Axis must be 0 or 1.");
        }
    }

    /**
     * Permet de dupliquer des matrices suivant un axes données
     *
     * @param a    Matrice à dupliquer
     * @param n    Nombre de fois à dupliquer
     * @param axis Axe de duplication
     * @return Matrice dupliquée
     */
    public static Matrix duplicate(Matrix a, int n, int axis) {
        if (axis == 0) {
            Matrix temp = new Matrix(a.rows * n, a.cols);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < a.rows; j++) {
                    if (a.cols >= 0) System.arraycopy(a.data[j], 0, temp.data[j + i * a.rows], 0, a.cols);
                }
            }
            return temp;
        } else if (axis == 1) {
            Matrix temp = new Matrix(a.rows, a.cols * n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < a.rows; j++) {
                    if (a.cols >= 0) System.arraycopy(a.data[j], 0, temp.data[j], i * a.cols, a.cols);
                }
            }
            return temp;
        } else {
            throw new IllegalArgumentException("Axis must be 0 or 1.");
        }
    }

    public static Pair<Integer, String> compatible(Matrix a, Matrix b) throws DimensionMismatchException {
        if (a.cols == b.cols) {
            if (a.rows == 1 || b.rows == 1) /* Ne peuvent pas être égaux */
                return a.rows == 1 ? new Pair<>(0, "a") : new Pair<>(0, "b");
            else
                throw new DimensionMismatchException("Incompatible dimensions for matrix broadcasts");
        } else if (a.cols == 1 || b.cols == 1) {
            if (a.rows == b.rows)
                return a.cols == 1 ? new Pair<>(1, "a") : new Pair<>(1, "b");
            else
                throw new DimensionMismatchException("Incompatible dimensions for matrix broadcasts");
        } else {
            throw new DimensionMismatchException("Incompatible dimensions for matrix broadcasts");
        }

    }

    /**
     * @param A
     * @param B
     * @return
     * @throws DimensionMismatchException
     */
    public static List<Matrix> broadcast(Matrix A, Matrix B) throws DimensionMismatchException {
        List<Integer> aShape = Arrays.asList(A.rows, A.cols);
        List<Integer> bShape = Arrays.asList(B.rows, B.cols);
        if (aShape.equals(bShape)) {
            return Arrays.asList(A, B);
        }
        if (aShape.equals(Arrays.asList(1, 1))) {
            List<Matrix> answer = broadcast(B, A.data[0][0]);
            return Arrays.asList(answer.get(1), answer.get(0));
        }
        if (bShape.equals(Arrays.asList(1, 1))) {
            return broadcast(A, B.data[0][0]);
        }
        Pair<Integer, String> axisToBroadcast = compatible(A, B);
        if (Objects.equals(axisToBroadcast.getValue(), "a")) {
            int axis = axisToBroadcast.getKey();
            return Arrays.asList(Matrix.duplicate(A, bShape.get(axis), axis), B);
        } else {
            int axis = axisToBroadcast.getKey();
            return Arrays.asList(A, Matrix.duplicate(B, aShape.get(axis), axis));
        }
    }

    public static List<Matrix> broadcast(Matrix a, double c) {
        return Arrays.asList(a, Matrix.fromNumber(c, a.rows, a.cols));
    }

    @Override
    public String toString() {
        return Arrays.stream(data)
                .map(Arrays::toString)
                .collect(Collectors.joining("\n"));
    }

    public Matrix lt(Matrix b) throws DimensionMismatchException {
        if (b.rows != this.rows || b.cols != this.cols) {
            throw new DimensionMismatchException("Matrices must have the same dimensions.");
        }
        List<Matrix> broadcasted = broadcast(this, b);
        Matrix a = broadcasted.get(0);
        b = broadcasted.get(1);
        Matrix temp = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[i][j] = a.data[i][j] < b.data[i][j] ? 1 : 0;
            }
        }
        return temp;
    }

    public Matrix gt(Matrix b) throws DimensionMismatchException {
        return b.lt(this);
    }

    public Matrix getColumn(int j) {
        Matrix temp = new Matrix(this.rows, 1);
        for (int i = 0; i < this.rows; i++) {
            temp.data[i][0] = this.data[i][j];
        }
        return temp;
    }

    public Matrix dropColumn(int j) {
        Matrix temp = new Matrix(this.rows, this.cols - 1);
        for (int i = 0; i < this.rows; i++) {
            for (int k = 0; k < this.cols; k++) {
                if (k < j) {
                    temp.data[i][k] = this.data[i][k];
                } else if (k > j) {
                    temp.data[i][k - 1] = this.data[i][k];
                }
            }
        }
        return temp;
    }
}
