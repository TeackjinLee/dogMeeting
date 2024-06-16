FROM amazoncorretto:17
COPY build/libs/*.jar dogmeeting.jar
ENTRYPOINT ["java", "-jar", "/dogmeeting.jar"]
