import lop.exception.DimensionMismatchException;
import lop.neural.MLPerceptron;
import lop.utilities.Matrix;

public class Main {
    public static void main(String[] args) throws DimensionMismatchException {
        double[][] x = {
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1}
        };
        double[][] y = {
                {0}, {1}, {1}, {0}
        };



        MLPerceptron nn = new MLPerceptron(3, new int[]{2, 2, 1});  // 2 inputs, 2 hidden layers, 1 output
        nn.fit(Matrix.transpose(new Matrix(x)), Matrix.transpose(new Matrix(y)), 50000);
        double[][] input = {{1, 1}, {0, 0}, {0, 1}, {1, 1}};
        Matrix result = Matrix.transpose(new Matrix(input));
        System.out.println(nn.predict(result));

    }
}