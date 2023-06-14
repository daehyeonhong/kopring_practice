FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
ARG JAR_FILE=*.jar
ARG STAGE=dev
ENV STAGE=${STAGE}
COPY ${JAR_FILE} app.jar
COPY ./wallet /home/spring/.oracle/wallet
ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-jar", "-Dspring.profiles.active=${STAGE}", "/app.jar"]
