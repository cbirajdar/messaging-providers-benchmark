FROM java:8

#Download the Hornetq binaries
RUN curl http://downloads.jboss.org/hornetq/hornetq-2.4.0.Final-bin.tar.gz | tar -xz

# Open and expose following ports
# Stomp - 61613
# OpenWire - 61616
# AMQP - 5672
EXPOSE 61613 61616 5672

COPY hornetq-configuration.xml .

RUN mv hornetq-configuration.xml hornetq-2.4.0.Final/config/stand-alone/non-clustered/hornetq-configuration.xml

RUN cd hornetq-2.4.0.Final/bin

CMD ./run.sh
