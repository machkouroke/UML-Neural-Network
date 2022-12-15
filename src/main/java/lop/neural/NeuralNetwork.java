package lop.neural;

import lop.exception.DimensionMismatchException;
import lop.function.aggregate.Sum;
import lop.function.transfert.Sigmoid;
import lop.neural.layer.HiddenLayer;
import lop.neural.layer.InputLayer;
import lop.neural.layer.Layer;
import lop.utilities.Matrix;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class NeuralNetwork {
    protected int layerNumber;
    protected InputLayer inputLayer;
    protected HiddenLayer outputLayer;
    protected List<HiddenLayer> hiddenLayers;
    protected List<Layer> layers;

    /**
     * @param layerNumber Nombre de couches du réseau
     * @param layersSize  Taille des neurones par couche. Le premier nombre est considéré comme le nombre
     *                    d'entrées et le dernier comme le nombre de sorties
     */
    NeuralNetwork(int layerNumber, int[] layersSize) {
        this.layerNumber = layerNumber;
        this.hiddenLayers = IntStream.range(1, layersSize.length - 1)
                .mapToObj(i -> new HiddenLayer(layersSize[i], new Matrix(layersSize[i], layersSize[i - 1]),
                        new Matrix(layersSize[i], 1), new Sigmoid(), new Sum()))
                .collect(Collectors.toList());
        this.inputLayer = new InputLayer(layersSize[0]);
        int last = layersSize.length - 1;
        this.outputLayer = new HiddenLayer(layersSize[last], new Matrix(layersSize[last - 1], layersSize[last]),
                new Matrix(layersSize[last], 1), new Sigmoid(), new Sum());
        this.layers = Stream.concat(hiddenLayers.stream(),
                        Stream.of(outputLayer))
                .collect(Collectors.toList());
    }

    public abstract void fit(Matrix x, Matrix y, int epochs) throws DimensionMismatchException;

    public abstract Matrix predict(Matrix y) throws DimensionMismatchException;
}
