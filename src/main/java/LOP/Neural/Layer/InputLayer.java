package LOP.Neural.Layer;

import LOP.Neural.Neural;
import LOP.utilities.Matrix;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputLayer extends Layer {

    public InputLayer(int neuroneNumber) {
        super(neuroneNumber);
        this.neurones = IntStream.of(neuroneNumber)
                .mapToObj(i -> new Neural(new Random().nextDouble(), null, null))
                .collect(Collectors.toList());
    }
}
