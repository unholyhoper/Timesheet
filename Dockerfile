FROM openjdk:8-jdk-alpine
EXPOSE 8082
ADD target/Timesheet-1.0.0-SNAPSHOT.war Timesheet-1.0.0-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/Timesheet-1.0.0-SNAPSHOT.war"]
