package lop.utilities;

import lop.exception.DimensionMismatchException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Matrix {
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
    public double sum() {
        return Arrays.stream(this.getData())
                .flatMapToDouble(Arrays::stream)
                .sum();
    }


    @Override
    public String toString() {
        return Arrays.stream(data)
                .map(Arrays::toString)
                .collect(Collectors.joining("\n"));
    }
}
