FROM openjdk:8-jdk-alpine
WORKDIR /app
RUN apk add --no-cache maven
CMD java -cp target/job-0.1-jar-with-dependencies.jar com.job.app.App