FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY authentication-service-0.0.1-SNAPSHOT.jar auth-service.jar

RUN bash -c "touch /auth-service.jar"

EXPOSE 8901

ENTRYPOINT ["java", "-jar", "auth-service.jar"]