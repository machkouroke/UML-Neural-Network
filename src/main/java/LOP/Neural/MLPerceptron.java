package LOP.Neural;

import LOP.Function.Transfert.Sigmoid;
import LOP.utilities.Matrix;

import java.util.List;

public class MLPerceptron extends Perceptron {
    Matrix weights_ih, weights_ho, bias_h, bias_o;
    double l_rate = 0.01;

    public MLPerceptron(int layerNumber, int i, int h, int o) {
        super(layerNumber);
        weights_ih = new Matrix(h, i);
        weights_ho = new Matrix(o, h);

        bias_h = new Matrix(h, 1);
        bias_o = new Matrix(o, 1);
    }

    public void train(double[] X, double[] Y) {
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden = Sigmoid.apply(hidden);

        Matrix output = Matrix.multiply(weights_ho, hidden);
        output.add(bias_o);
        output = Sigmoid.apply(output);

        Matrix target = Matrix.fromArray(Y);

        Matrix error = Matrix.subtract(target, output);
        Matrix gradient = Sigmoid.derivative(output);
        gradient.multiply(error);
        gradient.multiply(l_rate);

        Matrix hidden_T = Matrix.transpose(hidden);
        Matrix who_delta = Matrix.multiply(gradient, hidden_T);

        weights_ho.add(who_delta);
        bias_o.add(gradient);

        Matrix who_T = Matrix.transpose(weights_ho);
        Matrix hidden_errors = Matrix.multiply(who_T, error);

        Matrix h_gradient = Sigmoid.derivative(hidden);
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(l_rate);

        Matrix i_T = Matrix.transpose(input);
        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);

        weights_ih.add(wih_delta);
        bias_h.add(h_gradient);

    }

    @Override
    public void fit(Matrix X, Matrix Y, int epochs) {
        for (int i = 0; i < epochs; i++) {
            int sampleN = (int) (Math.random() * X.getRows());
            this.train(X.getData()[sampleN], Y.getData()[sampleN]);
        }
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