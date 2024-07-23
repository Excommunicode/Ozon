FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar Ozon.jar
ENTRYPOINT ["java","-jar","/Ozon.jar"]