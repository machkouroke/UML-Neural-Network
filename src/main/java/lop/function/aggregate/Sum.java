package lop.function.aggregate;

import lop.utilities.Matrix;

public class Sum implements Aggregate {
    public Matrix apply(Matrix a, Matrix b, Matrix w) {
        return Matrix.add(Matrix.dot(a, w), b);
    }

    @Override
    public Matrix apply(Matrix matrix) {
        return matrix;
    }
}
