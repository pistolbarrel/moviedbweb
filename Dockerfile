FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

COPY build/libs/movie*SNAPSHOT.jar /moviedbweb.jar

CMD ["java", "-jar", "/moviedbweb.jar"]