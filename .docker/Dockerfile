FROM openjdk:17-jdk-alpine3.14

ENV APP_HOME=/opt/eberry-travel

COPY eberry-travel.jar $APP_HOME/
COPY application.yaml $APP_HOME/

EXPOSE 16001

# Launch the application
WORKDIR $APP_HOME
RUN adduser -D eberry
USER eberry

CMD java -XX:MaxRAMPercentage=75 \
         -XX:+CrashOnOutOfMemoryError \
         -XX:+UseZGC \
         -jar eberry-travel.jar -Dspring.config.location=application.yaml
