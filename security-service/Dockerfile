FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

COPY /build/libs/*.jar build/

WORKDIR /app/build
EXPOSE 9090
ENTRYPOINT java -jar *.jar