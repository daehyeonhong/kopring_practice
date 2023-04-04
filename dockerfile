FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
ARG STAGE=dev
ENV STAGE=${STAGE}
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-jar", "-Dspring.profiles.active=${STAGE}", "/app.jar"]
