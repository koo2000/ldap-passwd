FROM openjdk:17-jdk
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ARG JAR_FILE=ldappasswd-0.0.1.jar

ARG PROPERTIES_FILE_DIR /opt/ldappasswd/conf

# additional properties file
#RUN mkdir -p ${PROPERTIES_FILE_DIR} 
RUN mkdir -p /opt/ldappasswd
COPY src/main/resources/application.properties ${PROPERTIES_FILE_DIR}
COPY target/${JAR_FILE} ldappasswd.jar

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -classpath /opt/ldappasswd/conf -jar ldappasswd.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ldappasswd.jar
