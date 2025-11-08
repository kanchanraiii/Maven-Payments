package exceptions;

public class PaymentGatewayTimeoutException extends PaymentGatewayException {
    public PaymentGatewayTimeoutException(String message) {
        super(message);
    }
}