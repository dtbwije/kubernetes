FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=build/libs/hello-kube-1.0.0.jar
RUN mkdir /opt/app
COPY ${JAR_FILE} /opt/app/app.jar

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
EXPOSE 8080