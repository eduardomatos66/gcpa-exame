FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# docker build -t gcpa-exame/spring-boot-docker .
# docker run -p 8000:8000 gcpa-exame/spring-boot-docker