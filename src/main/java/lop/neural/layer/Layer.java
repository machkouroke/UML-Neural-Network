package lop.neural.layer;

import lop.neural.Neural;
import lop.utilities.SerializationUtil;

import java.io.Serializable;
import java.util.List;

public class Layer implements SerializationUtil {
    private static final long serialversionUID = 1L;
    protected int neuroneNumber;
    protected List<Neural> neurones;

    protected Layer(int neuroneNumber) {
        this.neuroneNumber = neuroneNumber;

    }
}
