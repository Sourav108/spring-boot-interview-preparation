package com.spring.interview.foundations.mini;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MiniIocContainerTest {

    static class Repository {
        public String findData() {
            return "DATABASE_RECORD";
        }
    }

    static class Service {
        private final Repository repository;

        public Service(Repository repository) {
            this.repository = repository;
        }

        public String execute() {
            return "PROCESSED: " + repository.findData();
        }
    }

    static class CircularA {
        public CircularA(CircularB b) {}
    }

    static class CircularB {
        public CircularB(CircularA a) {}
    }

    @Test
    @DisplayName("Should resolve constructor dependencies recursively and maintain singleton instances")
    void shouldResolveDependenciesAndSingletons() {
        MiniIocContainer container = new MiniIocContainer();

        Service service = container.getBean(Service.class);

        assertThat(service).isNotNull();
        assertThat(service.execute()).isEqualTo("PROCESSED: DATABASE_RECORD");

        // Singleton identity check
        Service secondService = container.getBean(Service.class);
        assertThat(service).isSameAs(secondService);
        assertThat(container.getRegisteredBeanCount()).isEqualTo(2); // Repository + Service
    }

    @Test
    @DisplayName("Should detect and throw exception on constructor circular dependencies")
    void shouldDetectCircularDependency() {
        MiniIocContainer container = new MiniIocContainer();

        assertThatThrownBy(() -> container.getBean(CircularA.class))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Circular dependency detected");
    }
}
