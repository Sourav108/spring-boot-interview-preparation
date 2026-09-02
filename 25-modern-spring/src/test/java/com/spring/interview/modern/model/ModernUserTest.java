package com.spring.interview.modern.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModernUserTest {

    @Test
    @DisplayName("Should format Admin user correctly using pattern matching for switch")
    void shouldFormatAdminUser() {
        ModernUser admin = new ModernUser.Admin("adm-1", "AliceAdmin", "SUPERUSER", 5);
        String summary = ModernUser.formatUserSummary(admin);

        assertThat(summary).isEqualTo("Admin 'AliceAdmin' (ID: adm-1, Role: SUPERUSER, Level: 5)");
    }

    @Test
    @DisplayName("Should format Customer user correctly using pattern matching for switch")
    void shouldFormatCustomerUser() {
        ModernUser customer = new ModernUser.Customer("cust-10", "BobBuyer", 1250.50);
        String summary = ModernUser.formatUserSummary(customer);

        assertThat(summary).isEqualTo("Customer 'BobBuyer' (ID: cust-10, Balance: $1250.50)");
    }

    @Test
    @DisplayName("Should format Guest user correctly using pattern matching for switch")
    void shouldFormatGuestUser() {
        ModernUser guest = new ModernUser.Guest("gst-99", "Anonymous", 1799999999L);
        String summary = ModernUser.formatUserSummary(guest);

        assertThat(summary).isEqualTo("Guest 'Anonymous' (ID: gst-99, Expiry: 1799999999)");
    }
}
