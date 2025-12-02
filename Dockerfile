# Use a small JDK image
FROM eclipse-temurin:21-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the built jar into the image
# Change the JAR name if yours is differentsudo dnf install -y git
COPY target/CloudBasedFacilityBooking-0.0.1-SNAPSHOT.jar app.jar
# Expose the port your app runs on
EXPOSE 8080

# Environment variable for Mongo URI (can be overridden at run time)
ENV MONGODB_URI=mongodb://host.docker.internal:27017/fbs_simple

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
