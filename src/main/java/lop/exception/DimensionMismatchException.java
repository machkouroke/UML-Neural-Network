package lop.exception;

/**
 * Exception levée lorsqu'une opération entre deux matrices de dimensions incompatible pour l'opération donnée
 * est effectuée
 * @author Machkour Oke
 */
public class DimensionMismatchException extends Exception {
    public DimensionMismatchException(String message) {
        super("Dimension Mismatch: " + message);
    }
}
