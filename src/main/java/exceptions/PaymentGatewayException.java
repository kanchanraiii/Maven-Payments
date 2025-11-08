package exceptions;

public class PaymentGatewayException extends Exception {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
