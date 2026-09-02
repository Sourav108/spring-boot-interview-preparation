# 30-01: Spring Boot Annotations Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-01`
> **Primary Technology**: Spring Framework 6.2 | Spring Boot 3.4.13
> **Verification Date**: 2026-09-01

---

## 🏷️ Core & Stereotype Annotations
- `@SpringBootApplication`: Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.
- `@Component`: Root stereotype for Spring-managed beans.
- `@Service`: Meta-annotated with `@Component` for domain services.
- `@Repository`: Meta-annotated with `@Component` + exception translation.
- `@Controller` / `@RestController`: Presentation controllers (`@RestController = @Controller + @ResponseBody`).
- `@Configuration`: Declares full CGLIB-proxied configuration classes with `@Bean` factory methods.
- `@Scope`: Declares bean scope (`singleton`, `prototype`, `request`, `session`).
- `@Primary`: Default candidate when multiple beans match injection type.
- `@Qualifier("name")`: Disambiguates bean selection by unique name.
- `@Lazy`: Defers bean initialization until first access.
- `@Order(1)`: Configures ordering precedence for aspects, filters, and bean collections.

---

## 🌐 Web & HTTP Annotations
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`: HTTP routing shortcuts.
- `@PathVariable("id")`: Binds URI template variable.
- `@RequestParam("query")`: Binds query parameter.
- `@RequestBody`: Deserializes JSON request body to DTO.
- `@ResponseStatus(HttpStatus.CREATED)`: Sets default HTTP status code.
- `@ExceptionHandler(DomainException.class)`: Handles exceptions within `@ControllerAdvice`.
- `@HttpExchange`, `@GetExchange`, `@PostExchange`: Modern Spring 6 declarative HTTP client interfaces.

---

## 💾 Data, Transactions & Caching
- `@Transactional`: Declarative ACID transaction boundary.
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`: JPA entity mapping.
- `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@JoinColumn`: JPA relationship mappings.
- `@Version`: Optimistic locking version field.
- `@Query("SELECT ...")`: Custom JPQL or native SQL queries.
- `@Cacheable`, `@CachePut`, `@CacheEvict`, `@Caching`: Declarative caching operators.

---

## 🛡️ Security & Testing Annotations
- `@EnableWebSecurity`: Activates Spring Security web configuration.
- `@EnableMethodSecurity`: Activates `@PreAuthorize` and `@PostAuthorize` method security.
- `@SpringBootTest`: Full integration test container bootstrap.
- `@WebMvcTest`, `@DataJpaTest`, `@RestClientTest`: Sliced test configurations.
- `@MockitoBean`, `@MockitoSpyBean`: Modern Spring Boot 3.4 test mock injection.
