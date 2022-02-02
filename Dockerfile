FROM maven:3.8.4-jdk-11
COPY . /gcpa-back
WORKDIR /gcpa-back
RUN mvn clean -f pom.xml
RUN mvn compile -f pom.xml
RUN mvn package -f pom.xml
WORKDIR /gcpa-back/target
RUN mv *.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

#Agora vamos compilar a imagem Docker da nossa aplicação Springboot:
# docker build -t gcpae-exame/spring-boot .

#Finalmente, vamos executar a aplicação Springboot em um container Docker:
# docker run -d -p 8000:8000 --rm --name gcpae-back gcpae-exame/spring-boot

