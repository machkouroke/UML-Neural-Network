package lop.neural;

import lop.function.aggregate.Sum;
import lop.function.transfert.Transfert;
import lop.utilities.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MLPerceptron extends NeuralNetwork {

    double learningRate = 0.01;

    /**
     * @param layerNumber Nombre de couches du réseau
     * @param layersSize  Taille des neurones par couche. Le premier nombre est considéré comme le nombre
     *                    d'entrées et le dernier comme le nombre de sorties
     */
    public MLPerceptron(int layerNumber, int[] layersSize) {
        super(layerNumber, layersSize);
    }

    public List<Matrix> forwardPropagation(Matrix x) {
        List<Matrix> output = new ArrayList<>();
        output.set(0, x);
        for (int i = 1; i < this.layerNumber; i++) {
            Transfert transferFunc = this.hiddenLayers.get(i).getTransfertFunc();
            Sum aggregateFunc = (Sum) this.hiddenLayers.get(i).getAggregateFunc();
            output.set(
                    i,
                    transferFunc.apply(
                            aggregateFunc.apply(
                                    output.get(i - 1),
                                    this.hiddenLayers.get(i).getBias(),
                                    this.hiddenLayers.get(i).getIncomingWeights()
                            )
                    )
            );
        }
        return output;
    }


    public HashMap<String, HashMap> backwardPropagation(List<Matrix> output, Matrix y) {
        int m = y.getRows();
        Matrix dZ = Matrix.subtract(output.get(this.layerNumber), y);
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
                    Matrix.dot(dW.get(i), this.learningRate)
            ));
            this.hiddenLayers.get(i).setBias(Matrix.add(
                    this.hiddenLayers.get(i).getBias(),
                    Matrix.fromNumber(db.get(i) * this.learningRate, 1, 1)
            ));
        }
    }


    @Override
    public void fit(Matrix x, Matrix y, int epochs) {
        for (int i = 0; i < epochs; i++) {
            List<Matrix> a = this.forwardPropagation(x);
            HashMap<String, HashMap> grads = this.backwardPropagation(a, y);
            this.update(grads.get("dW"), grads.get("db"));
        }
    }

    @Override
    public Matrix predict(Matrix x) {

        return this.forwardPropagation(x).get(this.layerNumber);
    }
}