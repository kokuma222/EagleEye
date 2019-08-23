FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY configurationserver-0.0.1-SNAPSHOT.jar configurationserver.jar

RUN bash -c "touch /configurationserver.jar"

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "configurationserver.jar"]