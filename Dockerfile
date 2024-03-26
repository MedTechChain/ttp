FROM gradle:8.6.0-jdk21 as builder

COPY --chown=gradle:gradle . /home/gradle/src

RUN test -f /home/gradle/src/lib/encrypt  # "Encryption binary not provided"

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk-jammy

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY --from=builder /home/gradle/src/lib/encrypt /app/lib/encrypt

WORKDIR /app

EXPOSE 6000

CMD ["java", "-jar", "/app/spring-boot-application.jar"]