# messaging-providers-benchmark - WIP

Running simple benchmark for different messaging (Jms/Non-Jms) providers.

## TODO:
- Create scripts (preferably Docker) in order to <i>download/install/start/stop</i> the standalone instance of these providers to run this benchmark against.
- Create an interface to run the benchmark against an embedded instance and add acceptors for stomp, amqp etc.
- Externalize application settings using properties file
- Persistent vs non-persistent message delivery

##Installation
###HomeBrew
If you are on MacOS, you can install these systems using homebrew

Steps to install and documentation - http://brew.sh/

You can either all packages together or one by one.
```
brew install hornetq activemq rabbitmq kafka
```

Before running the benchmark for a given broker type, you need to get it running locally. Try the following commands on the terminal or iTerm or whatever suits you.

- Hornetq - ```hornet-start```
- ActiveMq - ```activemq start```
- RabbitMq - ```rabbitmq-server```
- Kafka - Requires zookeeper internally for managing kafka nodes, topics, messages and other configurations.
   - ```zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties```
   - ```kafka-server-start /usr/local/etc/kafka/server.properties```

###Docker
Installation details for Docker (MacOS) - https://docs.docker.com/engine/installation/mac/

<i>Please refer to the documentation on how to build, run, stop, remove containers and images.</i>

There are already built docker images available for Hornetq and ActiveMq but it probably won't have (STOMP|AMQP) acceptors already configured.

- Hornetq
   - docker build -t demo/hornetq Docker/hornetq
   - docker run -t -p 61613:61613 -p 61616:61616 -p 5672:5672 --name hornetq demo/hornetq
   - telnet ${docker_host} port #Confirm if you can connect to these ports
- ActiveMq
   - docker build -t demo/activemq Docker/activemq
   - docker run -t -p 61613:61613 -p 61616:61616 -p 5672:5672 --name activemq demo/activemq

- RabbitMq
   - You can download the official docker image
   - docker pull rabbitmq:latest
   - docker run -d -p 5672:5672 -p 15672:15672  --name rabbitmq rabbitmq

## How to run the benchmark

For running against docker container, add -Dhost_name=${docker_host}

- ```git clone git@github.com:cbirajdar/messaging-providers-benchmark.git```
- Broker Type: Hornetq, protocol: OPEN_WIRE, Enqueue count: 1000
   -    ```gradle execute -PjvmArgs="-Dbroker_type=HORNETQ -Dprotocol=OPEN_WIRE -Denqueue_count=1000"```
- Broker Type: ActiveMq, protocol: STOMP, Enqueue count: 10000
   -    ```gradle execute -PjvmArgs="-Dbroker_type=ACTIVEMQ -Dprotocol=STOMP -Denqueue_count=10000"```
- Broker Type: RabbitMq, protocol: AMQP, Enqueue count: 100
   -    ```gradle execute -PjvmArgs="-Dbroker_type=RABBITMQ -Dprotocol=AMQP -Denqueue_count=100"```
- Broker Type: Kafka, Enqueue count: 100
   -    ```gradle execute -PjvmArgs="-Dbroker_type=KAFKA -Denqueue_count=100"```
