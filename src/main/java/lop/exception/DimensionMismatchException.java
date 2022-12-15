package lop.exception;

public class DimensionMismatchException extends Exception {
    public DimensionMismatchException(String message) {
        super("Dimension Mismatch: " + message);
    }
}
