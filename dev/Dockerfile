FROM centos
MAINTAINER chenlang@lattebank.com

RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel \
       wget \
       git \
       golang-1.10

ENV TZ Asia/Shanghai
ENV JAVA_HOME /etc/alternatives/jre
ENV SERVER_PORT 8080
ENV JMX_PORT 1099
ENV JAVA_OPTS "-Xmx2G -Xms500M"
ENV APP_NAME banyan
ENV SPRING_PROFILES_ACTIVE dev

RUN mkdir /tmp/deploy/
COPY dev/data-soar.jar /tmp/deploy/app.jar
COPY dev/env.sh /tmp/deploy/
RUN bash /tmp/deploy/env.sh

EXPOSE 8080 1099

ENTRYPOINT exec java $JAVA_OPTS -jar /tmp/deploy/app.jar