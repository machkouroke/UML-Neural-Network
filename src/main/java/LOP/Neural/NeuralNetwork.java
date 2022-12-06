package LOP.Neural;

import LOP.Function.Aggregate.Sum;
import LOP.Function.Transfert.Sigmoid;
import LOP.Neural.Layer.HiddenLayer;
import LOP.Neural.Layer.InputLayer;
import LOP.Neural.Layer.Layer;
import LOP.utilities.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class NeuralNetwork {
    protected int layerNumber;
    protected InputLayer inputLayer;
    protected HiddenLayer outputLayer;
    protected List<HiddenLayer> hiddenLayers;
    protected List<Layer> layers;

    NeuralNetwork(int layerNumber, int[] layersSize) {
        this.layerNumber = layerNumber;
        this.hiddenLayers = IntStream.range(1, layersSize.length - 2)
                .mapToObj(i -> new HiddenLayer(layersSize[i], new Matrix(layersSize[i], layersSize[i - 1]),
                        new Matrix(layersSize[i], 1), new Sigmoid(), new Sum()))
                .collect(Collectors.toList());
        this.inputLayer = new InputLayer(layersSize[0]);
        int last = layersSize.length - 1;
        this.outputLayer = new HiddenLayer(layersSize[last], new Matrix(layersSize[last - 1], layersSize[last]),
                new Matrix(layersSize[last], 1), new Sigmoid(), new Sum());
        this.layers = Stream.concat(Arrays.asList(inputLayer).stream(), Stream.concat(hiddenLayers.stream(),
                        Arrays.asList(outputLayer).stream()))
                .collect(Collectors.toList());
    }

    public abstract void fit(Matrix X, Matrix Y, int epochs);

    public abstract List<Double> predict(double[] X);
}
