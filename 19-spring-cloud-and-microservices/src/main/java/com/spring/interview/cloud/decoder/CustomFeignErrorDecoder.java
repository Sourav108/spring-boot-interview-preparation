package com.spring.interview.cloud.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

/**
 * Custom Feign ErrorDecoder translating HTTP error statuses into structured domain exceptions.
 */
@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {

    public static class PaymentGatewayException extends RuntimeException {
        private final int statusCode;
        public PaymentGatewayException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }
        public int getStatusCode() { return statusCode; }
    }

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        if (status == 400 || status == 402) {
            return new PaymentGatewayException(status, "Payment rejected: Bad request or insufficient funds (" + status + ")");
        }
        if (status >= 500) {
            return new PaymentGatewayException(status, "Downstream gateway internal server error (" + status + ")");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
