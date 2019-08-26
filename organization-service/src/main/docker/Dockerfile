FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY organization-service-0.0.1-SNAPSHOT.jar orgservice.jar

RUN bash -c "touch /orgservice.jar"

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "orgservice.jar"]