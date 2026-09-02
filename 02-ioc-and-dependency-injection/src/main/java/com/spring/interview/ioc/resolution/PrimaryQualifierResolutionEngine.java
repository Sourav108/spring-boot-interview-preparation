package com.spring.interview.ioc.resolution;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Strategy-driven notification dispatcher demonstrating @Primary, @Qualifier, and Map injection.
 */
public class PrimaryQualifierResolutionEngine {

    public interface FormatterStrategy {
        String format(String raw);
    }

    @Component("jsonFormatter")
    @Primary
    public static class JsonFormatter implements FormatterStrategy {
        @Override
        public String format(String raw) {
            return "{\"payload\": \"" + raw + "\"}";
        }
    }

    @Component("xmlFormatter")
    public static class XmlFormatter implements FormatterStrategy {
        @Override
        public String format(String raw) {
            return "<payload>" + raw + "</payload>";
        }
    }

    private final FormatterStrategy defaultFormatter;
    private final FormatterStrategy explicitXmlFormatter;
    private final Map<String, FormatterStrategy> allFormatters;

    public PrimaryQualifierResolutionEngine(
        FormatterStrategy defaultFormatter,
        @Qualifier("xmlFormatter") FormatterStrategy explicitXmlFormatter,
        Map<String, FormatterStrategy> allFormatters
    ) {
        this.defaultFormatter = Objects.requireNonNull(defaultFormatter);
        this.explicitXmlFormatter = Objects.requireNonNull(explicitXmlFormatter);
        this.allFormatters = Objects.requireNonNull(allFormatters);
    }

    public String formatMessage(String formatType, String content) {
        FormatterStrategy strategy = allFormatters.getOrDefault(formatType + "Formatter", defaultFormatter);
        return strategy.format(content);
    }

    public FormatterStrategy getDefaultFormatter() {
        return defaultFormatter;
    }

    public FormatterStrategy getExplicitXmlFormatter() {
        return explicitXmlFormatter;
    }

    public int getAvailableFormatterCount() {
        return allFormatters.size();
    }
}
