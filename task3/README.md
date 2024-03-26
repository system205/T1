# Description

The repo contains a simple management system with **User** and **Order** entities. 
- It's possible to create, get, update and delete users, 
- Create, update and delete orders for a user.
- The project utilizes **Spring AOP** to implement logging functionality of entering, exiting a method of a service layer in addition to logging of the occurred exceptions.
- All the data is packed into the **H2** database for simple run and test of the application.

# Logging implementation with Spring AOP
First, the following dependency is excluded from actuator and web starters:
```
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-logging</artifactId>
```
This excludes "commons logging" allowing to have log4j2 under Slf4j with the dependency:
```
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-log4j2</artifactId>
```
The configuration of Appenders and Loggers is placed in [log/log4j2-spring.xml](task3/src/main/resources/log/log4j2-spring.xml)

The logging is implemented with Spring AOP as following:
1. ```@Aspect``` Annotation: The LoggingAspect class is annotated with ```@Aspect```, indicating it acts as an aspect for intercepting method executions.
2. Pointcut Expression: The ```@Around``` annotation defines a pointcut expression _execution(* study.task3.service.*.*(..))_, specifying that all methods within the ```study.task3.service``` package and its sub-packages will be intercepted. 
Specifically the methods of [OrderService](task3/src/main/java/study/task3/service/OrderService.java) and [UserService](task3/src/test/java/study/task3/service/UserServiceTest.java)
3. Advice Method: The ```LoggingAspect::log``` method acts as the advice, which is executed around the intercepted methods. It performs the following:
   - **Logs** the method name, declaring type, and arguments before execution.
   - Proceeds with the original method execution using ```joinPoint.proceed()```
   - **Logs** the return value after successful execution.
   - **Catches** (with rethrow) and logs any exceptions thrown during execution.

### Examples of logs
- Method call: 
  - INFO  [main] s.t.a.LoggingAspect: Method class study.task3.service.UserService.createUser is called with args [test, test@example.com]
- Method return
  - INFO  [main] s.t.a.LoggingAspect: Method createUser returns User{id=1, name='test', email='test@example.com'}
- Exception thrown
  - ERROR [main] s.t.a.LoggingAspect: Method getOrder throws an exception: java.lang.IllegalArgumentException: Order is not found with such id

# Testing

The logic of logging is tested for both services in [OrderServiceTest](task3/src/test/java/study/task3/service/OrderServiceTest.java) 
and [UserServiceTest](task3/src/test/java/study/task3/service/UserServiceTest.java) as following:
- ```@ExtendWith(OutputCaptureExtension.class)``` is used to have ```CapturedOutput``` dependency properly configured to contain the output of an app during testing
-  The simple logic for creation of User and Order is tested by calling methods and checking the presence of id
-  The presence of necessary logs is tested with CapturedOutput ```output.getOut().contains(expectedLogEntry)```

# How to run

```shell
mvn spring-boot:run
```