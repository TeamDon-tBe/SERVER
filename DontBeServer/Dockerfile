FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/DontBeServer-0.0.1-SNAPSHOT.jar /app/dontbeServer.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active={profile}", "dontbeServer.jar"]