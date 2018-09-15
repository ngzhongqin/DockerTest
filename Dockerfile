FROM openjdk:8-jre-alpine
EXPOSE 8080
COPY /target/DockerTest-1.0-SNAPSHOT-bin.jar /app/app.jar
CMD ["/usr/bin/java", "-cp", "/app/app.jar","-Dapplication.root=\"/app/\"","Main"]
