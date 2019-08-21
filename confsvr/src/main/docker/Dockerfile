FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY configurationserver-0.0.1-SNAPSHOT.jar configurationserver-0.0.2.jar

RUN bash -c "touch /configurationserver-0.0.2.jar"

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "configurationserver-0.0.2.jar"]