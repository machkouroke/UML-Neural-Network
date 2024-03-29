package lop.function.transfert;

import lop.utilities.Matrix;


public class Sigmoid implements Transfert {

    @Override
    public Matrix apply(Matrix matrix) {
        Matrix temp = new Matrix(
                matrix.getRows(),
                matrix.getCols()
        );
        for (int i = 0; i < temp.getRows(); i++) {
            for (int j = 0; j < temp.getCols(); j++)
                temp.setData(i, j, 1 / (1 + Math.exp(-matrix.getData()[i][j])));
        }
        return temp;
    }

    @Override
    public Matrix derivative(Matrix matrix) {
        Matrix temp = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i = 0; i < temp.getRows(); i++) {
            for (int j = 0; j < temp.getCols(); j++)
                temp.setData(i, j, matrix.getData()[i][j] * (1 - matrix.getData()[i][j]));
        }
        return temp;
    }

}
