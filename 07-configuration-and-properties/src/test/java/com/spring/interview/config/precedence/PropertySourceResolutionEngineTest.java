package com.spring.interview.config.precedence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PropertySourceResolutionEngineTest {

    @Test
    @DisplayName("Should inspect environment property sources and list their ordering")
    void shouldInspectPropertySources() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("customHighPriority", Map.of("key1", "val1")));

        List<PropertySourceResolutionEngine.PropertySourceInfo> infos =
            PropertySourceResolutionEngine.inspectEnvironmentSources(env);

        assertThat(infos).isNotEmpty();
        assertThat(infos.getFirst().name()).isEqualTo("customHighPriority");
        assertThat(infos.getFirst().order()).isEqualTo(1);
    }
}
