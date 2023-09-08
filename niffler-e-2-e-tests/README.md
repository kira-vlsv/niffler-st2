## **Технологии, использованные для тестирования**

- [JUnit 5 (Extensions, Resolvers, etc)](https://junit.org/junit5/docs/current/user-guide/)
- [Retrofit 2](https://square.github.io/retrofit/)
- [Allure](https://docs.qameta.io/allure/)
- [Selenide](https://selenide.org/)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle 7.6](https://docs.gradle.org/7.6/release-notes.html)
- [Mockito](https://site.mockito.org)
- [Wiremock](https://wiremock.org)
- Also Hibernate, Spring JDBC, gRPC and more:)

## **Что сделано в проекте**

### **1. JUnit5 Extensions**

В рамках проекта расширена функциональность JUnit с помощью кастомных Extensions имплементирующих интерфейсы ParameterResolver, 
ArgumentConverter, BeforeEachCallback, AfterEachCallback и др. классов callback.

**Custom Extensions решают следующие задачи:**

- Подготовка тестовых данных и подстановка их в параметры теста
- Логин через API для UI тестов
- Конвертирование для работы с DTO
- Выполнение скриншота при падении теста

```posh
package guru.qa.niffler.jupiter.*;
```

### **2. Работа с базой данных**
 
Управление данными в БД реализовано 3 разными способами:

- JDBC
- Spring JDBC
- Hibernate (JPA)

```posh
package guru.qa.niffler.db.*;
```

### **3. Работа с API**

- Интерцепторы для реализации API логирования

```posh
package guru.qa.niffler.api.interceptor.*;
```

### **4. Моки и стабы**

- Mockito используется в классе ```guru.qa.niffler.service.GrpcCurrencyServiceTest``` для unit тестирования
gRPC сервиса
- Wiremock используется для мокирования веб-сервисов, пример реализован в ветке ```wiremock``` в классе
```guru.qa.niffler.tests.uitests```

### **5. Тестирование gRPC сервиса**

```guru.qa.niffler.tests.grpc.*```
