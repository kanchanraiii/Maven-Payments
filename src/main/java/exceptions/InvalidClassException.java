package exceptions;


public class InvalidClassException extends RuntimeException {
    public InvalidClassException(String message) {
        super(message);
    }
}
