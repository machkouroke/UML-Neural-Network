package LOP.Neural;

import LOP.Function.Transfert.Sigmoid;
import LOP.utilities.Matrix;

import java.util.List;

public abstract class Perceptron extends NeuralNetwork {

    Perceptron(int layerNumber) {
        super(layerNumber);
    }
}

class MLPerceptron extends Perceptron {
    Matrix weights_ih, weights_ho, bias_h, bias_o;
    double l_rate = 0.01;

    MLPerceptron(int layerNumber, int i, int h, int o) {
        super(layerNumber);
        weights_ih = new Matrix(h, i);
        weights_ho = new Matrix(o, h);

        bias_h = new Matrix(h, 1);
        bias_o = new Matrix(o, 1);
    }

    @Override
    public void fit() {

    }

    @Override
    public List<Double> predict(double[] X) {

        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden = Sigmoid.apply(hidden);

        Matrix output = Matrix.multiply(weights_ho, hidden);
        output.add(bias_o);
        output = Sigmoid.apply(output);

        return output.toArray();
    }
}
