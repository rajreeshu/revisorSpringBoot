# Use the official OpenJDK image as a parent image
FROM openjdk:20-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build file
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

COPY target/${JAR_FILE} target/leetcodeRevision.jar
# Run the jar file 
ENTRYPOINT ["java","-jar","/app/target/leetcodeRevision.jar"]
