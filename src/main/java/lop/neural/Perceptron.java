package lop.neural;

public abstract class Perceptron extends NeuralNetwork {

    Perceptron(int layerNumber, int inputSize) {
        super(layerNumber, new int[]{inputSize, 1});
    }
}


