package lop.function.aggregate;

import lop.exception.DimensionMismatchException;
import lop.utilities.Matrix;

public class Sum implements Aggregate {
    public Matrix apply(Matrix a, Matrix b, Matrix w) throws DimensionMismatchException {
        Matrix aw = Matrix.dot(w, a);
        return Matrix.add(aw, Matrix.broadcast(aw, b).get(1));
    }

    @Override
    public Matrix apply(Matrix matrix) {
        return matrix;
    }
}
