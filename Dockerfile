FROM ubuntu:lunar
RUN apt update && \
    apt install -y openjdk-20-jre-headless ca-certificates-java && \
    apt clean && \
    update-ca-certificates -f
ENV JAVA_HOME /usr/lib/jvm/java-20-openjdk-amd64/
RUN export JAVA_HOME
RUN mkdir -p /opt/rest.recipes/logs/
ARG JAR_FILE
ADD ${JAR_FILE} /opt/rest.recipes/app.jar
EXPOSE 9000 27017
ENTRYPOINT ["java","-jar","/opt/rest.recipes/app.jar"]