package lop.function.transfert;


import lop.utilities.Matrix;

public interface Transfert {

    Matrix apply(Matrix matrix);

    Matrix derivative(Matrix matrix);
}
