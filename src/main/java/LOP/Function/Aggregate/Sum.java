package LOP.Function.Aggregate;

import LOP.utilities.Matrix;

public class Sum extends Aggregate {
    public Matrix apply(Matrix a, Matrix b, Matrix w) {
        return Matrix.add(Matrix.dot(a, w), b);
    }

    @Override
    public Matrix apply(Matrix matrix) {
        return null;
    }
}
