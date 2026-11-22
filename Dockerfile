FROM eclipse-temurin:27.0.1.1_4-jre-ubi10-minimal
ARG JAR_FILE
ARG JAR_NAME
ADD ${JAR_FILE} /opt/rest.recipes/${JAR_NAME}
EXPOSE 9000
ENV ENV_JAR_NAME=$JAR_NAME
ENTRYPOINT java -jar /opt/rest.recipes/${ENV_JAR_NAME}