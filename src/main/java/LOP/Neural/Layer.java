package LOP.Neural;

import LOP.Function.Aggregate.Aggregate;
import LOP.Function.Transfert.Transfert;
import LOP.utilities.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Layer {
    private int neuroneNumber;
    private Matrix bias;
    private List<Neural> neurones;

    Layer(int neuroneNumber, Matrix bias, Transfert transfertFunc, Aggregate aggregateFunc) {
        this.neuroneNumber = neuroneNumber;
        this.bias = bias;
        this.neurones = IntStream.of(neuroneNumber)
                .mapToObj(i -> new Neural(bias.getData()[i][0], aggregateFunc, transfertFunc))
                .collect(Collectors.toList());
    }
}
