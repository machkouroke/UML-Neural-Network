package LOP.Neural.Layer;

import LOP.Function.Aggregate.Aggregate;
import LOP.Function.Transfert.Transfert;
import LOP.Neural.Neural;
import LOP.utilities.Matrix;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HiddenLayer extends Layer {
    private Matrix incomingWeights, bias;
    private Transfert transfertFunc;
    private Aggregate aggregateFunc;

    public Matrix getBias() {
        return bias;
    }

    public void setBias(Matrix bias) {
        this.bias = bias;
    }

    public Transfert getTransfertFunc() {
        return transfertFunc;
    }

    public void setTransfertFunc(Transfert transfertFunc) {
        this.transfertFunc = transfertFunc;
    }

    public Aggregate getAggregateFunc() {
        return aggregateFunc;
    }

    public void setAggregateFunc(Aggregate aggregateFunc) {
        this.aggregateFunc = aggregateFunc;
    }

    public Matrix getIncomingWeights() {
        return incomingWeights;
    }

    public void setIncomingWeights(Matrix incomingWeights) {
        this.incomingWeights = incomingWeights;
    }

    public HiddenLayer(int neuroneNumber, Matrix incomingWeights, Matrix bias, Transfert transfertFunc,
                       Aggregate aggregateFunc) {
        super(neuroneNumber);
        this.incomingWeights = incomingWeights;
        this.bias = bias;
        this.transfertFunc = transfertFunc;
        this.aggregateFunc = aggregateFunc;
        this.neurones = IntStream.of(neuroneNumber)
                .mapToObj(i -> new Neural(bias.data[i][0], aggregateFunc, transfertFunc))
                .collect(Collectors.toList());
    }
}
