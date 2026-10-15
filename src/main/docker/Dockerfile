
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY app.jar /app.war
RUN sh -c 'touch /app.jar'
EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=docker", "/app.war"]