FROM centos
MAINTAINER chenlang@lattebank.com

RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel \
       wget \
       git

ADD go1.10.3.linux-amd64.tar.gz /root
ADD golang.conf /root/golang.conf
RUN mv /root/go /usr/local/
RUN cat /root/golang.conf >> /etc/profile
RUN echo "source /etc/profile" >> /root/.bashrc
RUN mkdir -p /home/golang

ENV TZ Asia/Shanghai
ENV JAVA_HOME /etc/alternatives/jre
ENV SERVER_PORT 8080
ENV JMX_PORT 1099
ENV JAVA_OPTS "-Xmx2G -Xms500M"
ENV APP_NAME data-soar
ENV SPRING_PROFILES_ACTIVE dev

RUN mkdir /tmp/deploy/
COPY dev/data-soar.jar /tmp/deploy/app.jar
RUN go get -d github.com/XiaoMi/soar
RUN cd ${GOPATH}/src/github.com/XiaoMi/soar && make

EXPOSE 8080 1099

ENTRYPOINT exec java $JAVA_OPTS -jar /tmp/deploy/app.jar