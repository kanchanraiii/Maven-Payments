package exceptions;

public class TransactionFailedException extends Exception {
    public TransactionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
