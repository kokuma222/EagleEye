FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY specialroutesservice-0.0.1-SNAPSHOT.jar app.jar

RUN bash -c "touch /app.jar"

EXPOSE 8956

ENTRYPOINT ["java", "-jar", "app.jar"]