package LOP.Neural.Layer;

import LOP.Neural.Neural;
import java.util.List;

public class Layer {
    protected int neuroneNumber;
    protected List<Neural> neurones;

    protected Layer(int neuroneNumber) {
        this.neuroneNumber = neuroneNumber;

    }
}
