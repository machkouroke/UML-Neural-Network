package LOP.Function.Transfert;


import LOP.utilities.Matrix;

public abstract class Transfert  {
    protected Transfert() {

    }
    public abstract Matrix apply(Matrix matrix);
    public abstract Matrix derivative(Matrix matrix);
}
