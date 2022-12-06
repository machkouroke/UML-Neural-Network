package LOP.Neural;

import LOP.Function.Transfert.Sigmoid;
import LOP.utilities.Matrix;

import java.util.List;

public abstract class Perceptron extends NeuralNetwork {

    Perceptron(int layerNumber, int inputSize) {
        super(layerNumber, new int[]{inputSize, 1});
    }
}


