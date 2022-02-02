FROM maven:3.8.4-jdk-11
COPY . /gcpa-back
WORKDIR /gcpa-back
RUN mvn clean -f pom.xml
RUN mvn compile -f pom.xml
RUN mvn package -f pom.xml
WORKDIR /gcpa-back/target
RUN mv *.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# docker build -t gcpa-exame/spring-boot-docker .
# docker run -p 8000:8000 gcpa-exame/spring-boot-docker