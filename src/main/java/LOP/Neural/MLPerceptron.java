package LOP.Neural;

import LOP.Function.Aggregate.Aggregate;
import LOP.Function.Transfert.Sigmoid;
import LOP.Function.Transfert.Transfert;
import LOP.utilities.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MLPerceptron extends NeuralNetwork {
    Matrix weights_ih, weights_ho, bias_h, bias_o;
    double l_rate = 0.01;

    /**
     * @param layerNumber Nombre de couches du réseau
     * @param layersSize  Taille des neurones par couche. Le premier nombre est considéré comme le nombre
     *                    d'entrées et le dernier comme le nombre de sorties
     */
    public MLPerceptron(int layerNumber, int[] layersSize) {
        super(layerNumber, layersSize);
    }

    public List<Matrix> forwardPropagation(Matrix X) {
        List<Matrix> output = new ArrayList<>();
        output.set(0, X);
        for (int i = 1; i < this.layerNumber; i++) {
            Transfert transferFunc = this.hiddenLayers.get(i).getTransfertFunc();
            Aggregate aggregateFunc = this.hiddenLayers.get(i).getAggregateFunc();
            output.set(i, transferFunc.apply(aggregateFunc.apply(output.get(i - 1))));
        }
        return output;
    }


    public HashMap<String, HashMap> backwardPropagation(List<Matrix> output, Matrix Y) {
        int m = Y.getRows();
        Matrix dZ = Matrix.subtract(output.get(this.layerNumber), Y);
        HashMap<Integer, Matrix> dW = new HashMap<>();
        HashMap<Integer, Double> db = new HashMap<>();
        for (int i = this.layerNumber - 1; i > 0; i--) {
            Matrix activation = output.get(i - 1);
            dW.put(i, Matrix.dot(
                    Matrix.dot(
                            dZ,
                            Matrix.transpose(activation)
                    ),
                    (double) 1 / m
            ));
            db.put(i, dZ.sum() / m);
            dZ = Matrix.multiply(Matrix.dot(
                            Matrix.transpose(this.hiddenLayers.get(i).getIncomingWeights()),
                            dZ
                    ), Matrix.multiply(
                            activation,
                            Matrix.subtract(
                                    Matrix.fromNumber(
                                            1, activation.getRows(), activation.getCols()
                                    ),
                                    activation
                            )
                    )
            );
        }
        HashMap<String, HashMap> result = new HashMap<>();
        result.put("dW", dW);
        result.put("db", db);
        return result;
    }

    public void update(HashMap<Integer, Matrix> dW, HashMap<Integer, Double> db) {
        for (int i = 1; i < this.layerNumber; i++) {
            this.hiddenLayers.get(i).setIncomingWeights(Matrix.add(
                    this.hiddenLayers.get(i).getIncomingWeights(),
                    Matrix.dot(dW.get(i), this.l_rate)
            ));
            this.hiddenLayers.get(i).setBias(Matrix.add(
                    this.hiddenLayers.get(i).getBias(),
                    Matrix.fromNumber(db.get(i) * this.l_rate, 1, 1)
            ));
        }
    }


    @Override
    public void fit(Matrix X, Matrix Y, int epochs) {
        for (int i = 0; i < epochs; i++) {
            List<Matrix> A = this.forwardPropagation(X);
            HashMap<String, HashMap> grads = this.backwardPropagation(A, Y);
            this.update(grads.get("dW"), grads.get("db"));
        }
    }

    @Override
    public Matrix predict(Matrix X) {

       return this.forwardPropagation(X).get(this.layerNumber);
    }
}