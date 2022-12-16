package lop.neural;

import lop.function.aggregate.Aggregate;
import lop.function.transfert.Transfert;
import lop.utilities.SerializationUtil;

public class Neural implements SerializationUtil {
    private static final long serialversionUID = 1L;
    private final double bias;
    private final Aggregate aggregateFunc;
    private final Transfert transfertFunc;

    public Neural(double bias, Aggregate aggregateFunc, Transfert transfertFunc) {
        this.bias = bias;
        this.aggregateFunc = aggregateFunc;
        this.transfertFunc = transfertFunc;
    }
}
