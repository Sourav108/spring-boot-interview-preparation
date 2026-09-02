package com.spring.interview.ioc.resolution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PrimaryQualifierResolutionEngineTest {

    @Test
    @DisplayName("Should resolve formatters via @Primary default, @Qualifier, and dynamic Map lookup")
    void shouldResolveFormatters() {
        var json = new PrimaryQualifierResolutionEngine.JsonFormatter();
        var xml = new PrimaryQualifierResolutionEngine.XmlFormatter();

        var engine = new PrimaryQualifierResolutionEngine(
            json, xml, Map.of("jsonFormatter", json, "xmlFormatter", xml)
        );

        // 1. Default primary format
        assertThat(engine.formatMessage("unknown", "hello")).isEqualTo("{\"payload\": \"hello\"}");

        // 2. Explicit XML format
        assertThat(engine.formatMessage("xml", "hello")).isEqualTo("<payload>hello</payload>");

        // 3. Properties
        assertThat(engine.getDefaultFormatter()).isSameAs(json);
        assertThat(engine.getExplicitXmlFormatter()).isSameAs(xml);
        assertThat(engine.getAvailableFormatterCount()).isEqualTo(2);
    }
}
