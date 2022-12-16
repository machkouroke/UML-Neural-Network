package lop.function.aggregate;

import lop.utilities.Matrix;
import lop.utilities.SerializationUtil;


public interface Aggregate extends SerializationUtil {
    long serialversionUID = 1L;

    Matrix apply(Matrix matrix);

}
