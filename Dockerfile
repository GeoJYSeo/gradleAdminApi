FROM openjdk:11

# Create app directory
WORKDIR /api
ADD . /api/

ENV HOST 0.0.0.0
EXPOSE 8080

# start command
CMD [ "SPRING_PROFILES_ACTIVE=docker", "./gradlew", "bootRun" ]
