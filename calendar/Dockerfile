FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/calendar
COPY target/calendar-0.0.1-SNAPSHOT.jar /opt/calendar
ARG JAR_FILE=/opt/calendar/calendar-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/calendar/calendar-0.0.1-SNAPSHOT.jar"]
