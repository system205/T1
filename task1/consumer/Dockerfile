FROM eclipse-temurin:17-jdk-alpine AS build

# Copy parent POM
WORKDIR /workspace/task1
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw install -N

WORKDIR /workspace/app
COPY consumer/pom.xml .
COPY consumer/src src

RUN ../task1/mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","study.consumer.ConsumerApplication"]