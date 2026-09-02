# 30-08: JVM & HikariCP Performance Tuning Cheatsheet

> **Module**: `MOD-30: Cheatsheets`
> **Topic ID**: `SB-30-08`
> **Primary Technology**: Java 21 LTS | Generational ZGC | HikariCP
> **Verification Date**: 2026-09-01

---

## ⚡ Production Java 21 Container Flags
```bash
java \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -Xms3g -Xmx3g \
  -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m \
  -Xss512k \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/dumps/heapdump.hprof \
  -jar app.jar
```

---

## 🗄️ HikariCP Connection Pool Hardware Formula
$$\text{Pool Size} = (\text{DB CPU Cores} \times 2) + \text{Disk Spindles}$$
- For 8-core DB with SSD: $8 \times 2 + 1 = 17$ total connections across all application pods!
