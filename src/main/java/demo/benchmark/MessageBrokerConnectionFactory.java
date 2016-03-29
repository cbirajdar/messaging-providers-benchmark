package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;
import demo.benchmark.logging.Loggable;

import javax.jms.JMSException;

public class MessageBrokerConnectionFactory implements Loggable {

    public AbstractMessageBroker createMessageBroker(MessageBrokerType type, Protocol protocol) throws JMSException {
        log().info("Creating connection....");
        String port = protocol.getPort();
        if (type.equals(MessageBrokerType.HORNETQ)) {
            return new HornetqMessageBroker(port);
        } else if (type.equals(MessageBrokerType.ACTIVEMQ)) {
            return new ActiveMqMessageBroker(port);
        } else if (type.equals(MessageBrokerType.RABBITMQ)) {
            log().error("{} message broker type is not implemented.", type);
            System.exit(0);
        } else if (type.equals(MessageBrokerType.KAFKA)) {
            return new KafkaMessageBroker(Protocol.KAFKA.getPort());
        }
        return null;
    }
}
