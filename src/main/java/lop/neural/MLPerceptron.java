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
        for (int i = 1; i < this.layerNumber; i++) {
            Transfert transferFunc = ((HiddenLayer) this.layers.get(i - 1)).getTransfertFunc();
            Sum aggregateFunc = (Sum) ((HiddenLayer) this.layers.get(i - 1)).getAggregateFunc();

            output.add(
                    transferFunc.apply(
                            aggregateFunc.apply(
                                    output.get(i - 1),
                                    ((HiddenLayer) this.layers.get(i - 1)).getBias(),
                                    ((HiddenLayer) this.layers.get(i - 1)).getIncomingWeights()
                            )
                    )
            );


        }
        return output;
    }


    public HashMap<String, HashMap<Integer, Matrix>> backwardPropagation(List<Matrix> output, Matrix y)
            throws DimensionMismatchException {
        int m = y.getRows();
        Matrix lastLayer = output.get(this.layers.size() - 1);
        Matrix dZ = Matrix.subtract(
                lastLayer,
                Matrix.broadcast(
                        lastLayer, y
                ).get(1)
        );
        HashMap<Integer, Matrix> dW = new HashMap<>();
        HashMap<Integer, Matrix> db = new HashMap<>();
        for (int i = this.layerNumber - 1; i > 0; i--) {
            Matrix activation = output.get(i - 1);
            dW.put(i, Matrix.dot(
                    Matrix.dot(
                            dZ,
                            Matrix.transpose(activation)
                    ),
                    (double) 1 / m
            ));
            Matrix dZSum = dZ.sum(1);
            db.put(i,
                    Matrix.multiply(dZSum,
                            Matrix.broadcast(dZSum, (double) 1 / m).get(1))
            );
            if (i > 1) {
                dZ = Matrix.multiply(
                        Matrix.dot(
                                Matrix.transpose(((HiddenLayer) this.layers.get(i - 1)).getIncomingWeights()),
                                dZ
                        ),
                        Matrix.multiply(
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

        }
        HashMap<String, HashMap<Integer, Matrix>> result = new HashMap<>();
        result.put("dW", dW);
        result.put("db", db);
        return result;
    }

    public void update(HashMap<Integer, Matrix> dW, HashMap<Integer, Matrix> db) throws DimensionMismatchException {
        for (int i = 1; i < this.layerNumber; i++) {
            HiddenLayer actualLayer = ((HiddenLayer) this.layers.get(i - 1));
            actualLayer.setIncomingWeights(Matrix.add(
                    actualLayer.getIncomingWeights(),
                    Matrix.dot(dW.get(i), -this.learningRate)
            ));
            actualLayer.setBias(Matrix.add(
                    actualLayer.getBias(),
                    Matrix.dot(db.get(i), -this.learningRate)
            ));
        }
    }


    @Override
    public void fit(Matrix x, Matrix y, int epochs) throws DimensionMismatchException {
        for (int i = 0; i < epochs; i++) {
            List<Matrix> a = this.forwardPropagation(x);
            HashMap<String, HashMap<Integer, Matrix>> grads = this.backwardPropagation(a, y);
            this.update(grads.get("dW"), grads.get("db"));
        }
    }

    @Override
    public Matrix predict(Matrix x) throws DimensionMismatchException {
        Matrix prediction = this.forwardPropagation(x).get(this.layerNumber - 1);

        return prediction.gt(Matrix.broadcast(prediction, 0.5).get(1));
    }
}