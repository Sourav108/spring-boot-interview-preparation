package com.spring.interview.boot.diagnostics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.net.BindException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomPortConflictFailureAnalyzerTest {

    @Test
    @DisplayName("Should generate actionable FailureAnalysis description and action for BindException")
    void shouldAnalyzeBindException() {
        CustomPortConflictFailureAnalyzer analyzer = new CustomPortConflictFailureAnalyzer();

        BindException bindException = new BindException("Address already in use: 8080");
        FailureAnalysis analysis = analyzer.analyze(bindException, bindException);

        assertThat(analysis).isNotNull();
        assertThat(analysis.getDescription()).contains("Address already in use: 8080");
        assertThat(analysis.getAction()).contains("server.port=8081");
        assertThat(analysis.getCause()).isSameAs(bindException);
    }
}
