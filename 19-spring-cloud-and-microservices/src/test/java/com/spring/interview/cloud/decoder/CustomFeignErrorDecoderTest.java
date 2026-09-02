package com.spring.interview.cloud.decoder;

import feign.Request;
import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CustomFeignErrorDecoderTest {

    private final CustomFeignErrorDecoder decoder = new CustomFeignErrorDecoder();

    private Response createMockResponse(int status) {
        Request request = Request.create(
            Request.HttpMethod.POST,
            "/api/v1/payments",
            Map.of(),
            new byte[0],
            StandardCharsets.UTF_8,
            null
        );

        return Response.builder()
            .status(status)
            .reason("Simulated Error")
            .request(request)
            .headers(Collections.emptyMap())
            .build();
    }

    @Test
    @DisplayName("Should translate HTTP 400 into PaymentGatewayException with status 400")
    void shouldTranslate400ToDomainException() {
        Response response = createMockResponse(400);
        Exception exception = decoder.decode("processPayment", response);

        assertThat(exception).isInstanceOf(CustomFeignErrorDecoder.PaymentGatewayException.class);
        var pgEx = (CustomFeignErrorDecoder.PaymentGatewayException) exception;
        assertThat(pgEx.getStatusCode()).isEqualTo(400);
        assertThat(pgEx.getMessage()).contains("Payment rejected");
    }

    @Test
    @DisplayName("Should translate HTTP 500 into PaymentGatewayException with status 500")
    void shouldTranslate500ToDomainException() {
        Response response = createMockResponse(500);
        Exception exception = decoder.decode("processPayment", response);

        assertThat(exception).isInstanceOf(CustomFeignErrorDecoder.PaymentGatewayException.class);
        var pgEx = (CustomFeignErrorDecoder.PaymentGatewayException) exception;
        assertThat(pgEx.getStatusCode()).isEqualTo(500);
        assertThat(pgEx.getMessage()).contains("internal server error");
    }
}
