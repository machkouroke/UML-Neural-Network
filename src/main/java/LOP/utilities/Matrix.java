package LOP.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Matrix() {
        this.rows = 0;
        this.cols = 0;
        this.data = new double[0][0];
    }

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

    public void add(double scaler) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += scaler;
            }

        }
    }

    public void add(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            System.out.println("Shape Mismatch");
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += m.data[i][j];
            }
        }
    }

    public static Matrix add(Matrix a, Matrix b) {
        Matrix temp = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[i][j] = a.data[i][j] + b.data[i][j];
            }
        }
        return temp;
    }

    public static Matrix subtract(Matrix a, Matrix b) {

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

    public static Matrix dot(Matrix a, Matrix b) {
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

    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix temp = new Matrix(a.rows, b.cols);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.cols; j++) {
                temp.data[i][j] = a.data[i][j] * b.data[i][j];
            }
        }
        return temp;
    }

    public static Matrix dot(Matrix a, double c) {
        Matrix temp = new Matrix(a.rows, a.cols);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.cols; j++) {
                temp.data[i][j] *= c;
            }
        }
        return temp;
    }

    public static Matrix fromNumber(double x, int rows, int cols) {
        Matrix temp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.data[i][j] = x;
            }
        }
        return temp;
    }
    public double sum() {
        return Arrays.stream(this.getData())
                .flatMapToDouble(Arrays::stream)
                .sum();
    }

    public List<Double> toArray() {
        List<Double> temp = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.add(data[i][j]);
            }
        }
        return temp;
    }
}
