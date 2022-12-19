FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY . .

EXPOSE 8000
RUN ./script/install-maven.sh
RUN mkdir model
ENV M2_HOME='/opt/apache-maven-3.6.3'
ENV PATH=$PATH:$M2_HOME/bin

RUN mvn clean compile
CMD mvn exec:java
