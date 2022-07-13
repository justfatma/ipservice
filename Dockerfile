FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/ipservice-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} ipservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","ipservice-0.0.1-SNAPSHOT.jar"]