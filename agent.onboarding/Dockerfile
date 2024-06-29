FROM oraclelinux:8-slim

# Install Java
RUN yum install -y java-11-openjdk

# Copy the JAR file
COPY target/spring-postures-docker-0.0.1-SNAPSHOT.jar /app.jar

# Set the entry point
ENTRYPOINT ["java","-jar","/app.jar"]
