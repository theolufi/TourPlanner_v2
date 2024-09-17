FROM openjdk:22-jdk

COPY target/tourplanner-0.0.1-SNAPSHOT.jar tourplanner-1.0.0.jar

ENTRYPOINT [ "java", "-jar", "tourplanner-1.0.0.jar" ]
