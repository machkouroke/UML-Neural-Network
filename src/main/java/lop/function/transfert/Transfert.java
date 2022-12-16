package lop.function.transfert;


import lop.utilities.Matrix;
import lop.utilities.SerializationUtil;


public interface Transfert extends SerializationUtil {
    long serialversionUID = 1L;

    Matrix apply(Matrix matrix);

    Matrix derivative(Matrix matrix);
}
