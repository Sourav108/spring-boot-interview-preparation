package com.spring.interview.boot.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.net.BindException;

/**
 * Custom FailureAnalyzer converting BindException into structured diagnostic action items.
 */
public class CustomPortConflictFailureAnalyzer extends AbstractFailureAnalyzer<BindException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, BindException cause) {
        String description = "Embedded Web Server failed to bind to its configured port: " + cause.getMessage();
        String action = """
            Remediation Steps:
            1. Verify if another process is occupying the port (run 'lsof -i :8080').
            2. Configure a different port in application.properties via 'server.port=8081'.
            3. Set 'server.port=0' to assign a random available port for tests.
            """;

        return new FailureAnalysis(description, action, cause);
    }
}
