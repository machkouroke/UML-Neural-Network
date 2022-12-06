package LOP;


import LOP.Neural.MLPerceptron;
import LOP.utilities.Matrix;

public class Main {
    public static void main(String[] args) {
        double[][] X = {
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1}
        };
        double[][] Y = {
                {0}, {1}, {1}, {0}
        };
        MLPerceptron nn = new MLPerceptron(1, 2, 10, 1);
        nn.fit(new Matrix(X), new Matrix(Y), 50000);
        double[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (double[] d : input) {
            System.out.println(nn.predict(d).toString());
        }
    }
}