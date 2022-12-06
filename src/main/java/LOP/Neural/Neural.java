package LOP.Neural;

import LOP.Function.Aggregate;
import LOP.Function.Transfert.Transfert;

public class Neural {
    private float weight;
    private Aggregate aggregateFunc;
    private Transfert transfertFunc;
    Neural(float weight, Aggregate aggregateFunc, Transfert transfertFunc) {
        this.weight = weight;
        this.aggregateFunc = aggregateFunc;
        this.transfertFunc = transfertFunc;
    }
}
