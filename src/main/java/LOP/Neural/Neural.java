package LOP.Neural;

import LOP.Function.Aggregate.Aggregate;
import LOP.Function.Transfert.Transfert;

public class Neural {
    private double bias;
    private Aggregate aggregateFunc;
    private Transfert transfertFunc;

    public Neural(double bias, Aggregate aggregateFunc, Transfert transfertFunc) {
        this.bias = bias;
        this.aggregateFunc = aggregateFunc;
        this.transfertFunc = transfertFunc;
    }
}
