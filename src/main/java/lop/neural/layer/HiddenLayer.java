package lop.neural.layer;

import lop.function.aggregate.Aggregate;
import lop.function.transfert.Transfert;
import lop.neural.Neural;
import lop.utilities.Matrix;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HiddenLayer extends Layer {
    private Matrix incomingWeights, bias;
    private final Transfert transfertFunc;
    private final Aggregate aggregateFunc;

    public Matrix getBias() {
        return bias;
    }

    public void setBias(Matrix bias) {
        this.bias = bias;
    }

    public Transfert getTransfertFunc() {
        return transfertFunc;
    }


    public Aggregate getAggregateFunc() {
        return aggregateFunc;
    }


    public Matrix getIncomingWeights() {
        return incomingWeights;
    }

    public void setIncomingWeights(Matrix incomingWeights) {
        this.incomingWeights = incomingWeights;
    }

    /**
     * @param neuroneNumber   Nombre de neurones dans la couche
     * @param incomingWeights Matrice de taille (neuroneNumber, neuroneNumber-1 (Nombre de
     *                        neurones dans la couche précédente) des poids entrants
     * @param bias            Matrice de taille (neuroneNumber, 1) des biais
     * @param transfertFunc   Fonction de transfert de tous les neurones de la couche
     * @param aggregateFunc   Fonction d'agrégation de tous les neurones de la couche
     */
    public HiddenLayer(int neuroneNumber, Matrix incomingWeights, Matrix bias, Transfert transfertFunc,
                       Aggregate aggregateFunc) {
        super(neuroneNumber);
        this.incomingWeights = incomingWeights;
        this.bias = bias;
        this.transfertFunc = transfertFunc;
        this.aggregateFunc = aggregateFunc;
        this.neurones = IntStream.range(0, neuroneNumber)
                .mapToObj(i -> new Neural(bias.data[i][0], aggregateFunc, transfertFunc))
                .collect(Collectors.toList());
    }
}
