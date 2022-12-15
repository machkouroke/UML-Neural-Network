package lop.neural;

import lop.exception.DimensionMismatchException;
import lop.function.aggregate.Sum;
import lop.function.transfert.Transfert;
import lop.neural.layer.HiddenLayer;
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

    public List<Matrix> forwardPropagation(Matrix x) throws DimensionMismatchException {
        List<Matrix> output = new ArrayList<>();
        output.add(x);
        for (int i = 0; i < this.layerNumber - 1; i++) {
            Transfert transferFunc = ((HiddenLayer) this.layers.get(i)).getTransfertFunc();
            Sum aggregateFunc = (Sum) ((HiddenLayer) this.layers.get(i)).getAggregateFunc();
            if (i > 0) {
                output.add(
                        transferFunc.apply(
                                aggregateFunc.apply(
                                        output.get(i - 1),
                                        ((HiddenLayer) this.layers.get(i)).getBias(),
                                        ((HiddenLayer) this.layers.get(i)).getIncomingWeights()
                                )
                        )
                );
            }

        }
        return output;
    }


    public HashMap<String, HashMap> backwardPropagation(List<Matrix> output, Matrix y) throws DimensionMismatchException {
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

    public void update(HashMap<Integer, Matrix> dW, HashMap<Integer, Double> db) throws DimensionMismatchException {
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
    public void fit(Matrix x, Matrix y, int epochs) throws DimensionMismatchException {
        for (int i = 0; i < epochs; i++) {
            List<Matrix> a = this.forwardPropagation(x);
            HashMap<String, HashMap> grads = this.backwardPropagation(a, y);
            this.update(grads.get("dW"), grads.get("db"));
        }
    }

    @Override
    public Matrix predict(Matrix x) throws DimensionMismatchException {

        return this.forwardPropagation(x).get(this.layerNumber);
    }
}