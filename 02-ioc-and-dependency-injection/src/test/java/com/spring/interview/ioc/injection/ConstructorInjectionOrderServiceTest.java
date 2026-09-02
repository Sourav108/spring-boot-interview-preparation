package com.spring.interview.ioc.injection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConstructorInjectionOrderServiceTest {

    @Test
    @DisplayName("Should execute order workflow using pure Java mocks in sub-millisecond execution time")
    void shouldProcessOrderWithMocks() {
        var mockRepo = Mockito.mock(ConstructorInjectionOrderService.OrderRepository.class);
        var mockPayment = Mockito.mock(ConstructorInjectionOrderService.PaymentClient.class);

        when(mockPayment.processPayment("ord-1", 100.0)).thenReturn(true);

        var service = new ConstructorInjectionOrderService(mockRepo, mockPayment);

        boolean success = service.placeOrder("ord-1", 100.0);

        assertThat(success).isTrue();
        verify(mockPayment, times(1)).processPayment("ord-1", 100.0);
        verify(mockRepo, times(1)).saveOrder("ord-1", 100.0);
    }
}
