package LOP.Neural;

import LOP.Function.Aggregate;
import LOP.Function.Transfert;

public class Neural {
    private float weight;
    private Aggregate aggregFunc;
    private Transfert transfertFunc;
    Neural(float weight, Aggregate aggregFunc, Transfert transfertFunc) {
        this.weight = weight;
        this.aggregFunc = aggregFunc;
        this.transfertFunc = transfertFunc;
    }
}
