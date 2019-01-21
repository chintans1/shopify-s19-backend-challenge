# Build stage
FROM openjdk:8-jdk-alpine AS build

# This is so we can use bash inside the docker container (Alpine builds don't have it by default)
# https://stackoverflow.com/questions/40944479/how-to-use-bash-with-an-alpine-based-docker-image
RUN apk add --no-cache bash

# Declare variables to use in the build
ARG WORKING_DIRECTORY="/usr/src/app"

WORKDIR $WORKING_DIRECTORY

COPY gradlew .
COPY gradle/ $WORKING_DIRECTORY/gradle
RUN ./gradlew

# Copy over all the gradle files and source code before running a gradle build
COPY . $WORKING_DIRECTORY

# Run gradle build to generate the .jar file
RUN ./gradlew build

# Deploy stage
FROM openjdk:8-jre-alpine AS release

# Declare variables to use in the release stage
ARG WORKING_DIRECTORY="/usr/src/app"

COPY --from=build $WORKING_DIRECTORY/build/libs/shopify-challenge-0.0.1-SNAPSHOT.jar /usr/app/

# The API will be available at port 8080 now
EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/app/shopify-challenge-0.0.1-SNAPSHOT.jar"]