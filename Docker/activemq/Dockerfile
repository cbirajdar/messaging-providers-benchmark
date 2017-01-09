FROM java:8

#Download the ActiveMq binaries
RUN curl http://apache.mesi.com.ar/activemq/5.13.2/apache-activemq-5.13.2-bin.tar.gz | tar -xz

# Open and expose following ports
# Stomp - 61613
# OpenWire - 61616
# AMQP - 5672
EXPOSE 61613 61616 5672

RUN mv apache-activemq-5.13.2/conf/activemq.xml apache-activemq-5.13.2/conf/activemq.xml.orig

RUN sed -e 's/stomp/tcp/2' -e 's/amqp/tcp/2' apache-activemq-5.13.2/conf/activemq.xml.orig >> apache-activemq-5.13.2/conf/activemq.xml

CMD java -jar apache-activemq-5.13.2/bin/activemq.jar start
