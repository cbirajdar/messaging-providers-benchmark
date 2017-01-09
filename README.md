# jms-amqp-providers-benchmark - WIP

Running simple benchmark for different JMS/AMQP providers.

## TODO:
- Create scripts (preferably Docker) in order to download/install a standalone instance of these providers to run this benchmark against.
- Create an interface to run the benchmark against an embedded instance and add acceptors for stomp, amqp etc.
- Persistent vs non-persistent message delivery

## How to run the benchmark

- ```git clone git@github.com:cbirajdar/jms-amqp-providers-benchmark.git```
- Broker Type: Hornetq, protocol: OPEN_WIRE, Enqueue count: 1000
   -    ```gradle execute -PjvmArgs="-Dbroker_type=HORNETQ -Dprotocol=OPEN_WIRE -Denqueue_count=1000"```
- Broker Type: ActiveMq, protocol: STOMP, Enqueue count: 10000
   -    ```gradle execute -PjvmArgs="-Dbroker_type=ACTIVEMQ -Dprotocol=STOMP -Denqueue_count=10000"```
- Broker Type: RabbitMq, protocol: AMQP, Enqueue count: 100
   -    ```gradle execute -PjvmArgs="-Dbroker_type=RABBITMQ -Dprotocol=AMQP -Denqueue_count=100"```
