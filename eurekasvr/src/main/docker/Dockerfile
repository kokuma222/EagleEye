FROM java:8

MAINTAINER zzz

VOLUME /tmp

COPY eurekasvr-0.0.1-SNAPSHOT.jar eurekasvr.jar

RUN bash -c "touch /eurekasvr.jar"

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "eurekasvr.jar"]