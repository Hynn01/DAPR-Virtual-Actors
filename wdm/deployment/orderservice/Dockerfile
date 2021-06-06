FROM openjdk:11-jre-slim
# RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
COPY ../../target/*.jar app.jar
ADD run-in-docker.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
