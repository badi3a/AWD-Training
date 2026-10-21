FROM openjdk:17
#port ulistené par le projet
EXPOSE 8050
ADD target/Job-0.0.1-SNAPSHOT.jar job.jar
ENTRYPOINT ["java", "-jar", "job.jar"]
