FROM eclipse-temurin:26-jre-resolute
ARG JAR_FILE
ARG JAR_NAME
ADD ${JAR_FILE} /opt/rest.recipes/${JAR_NAME}
EXPOSE 9000
ENV ENV_JAR_NAME=$JAR_NAME
ENTRYPOINT java -jar /opt/rest.recipes/${ENV_JAR_NAME}