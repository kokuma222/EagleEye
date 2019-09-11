FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY authentication-service-0.0.1-SNAPSHOT.jar authservice.jar

RUN bash -c "touch /authservice.jar"

EXPOSE 8901

ENTRYPOINT ["java", "-jar", "authservice.jar"]