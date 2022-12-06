package LOP.Neural;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private int layerNumber;
    private Layer inputLayer;
    private Layer outputLayer;
    private List<Layer> hiddenLayers;

    NeuralNetwork(int layerNumber) {
        this.layerNumber = layerNumber;
        this.inputLayer = new Layer(0);
        this.outputLayer = new Layer(0);
        this.hiddenLayers = new ArrayList<>(layerNumber);
    }
}
