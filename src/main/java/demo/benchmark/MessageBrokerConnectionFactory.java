package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;
import demo.benchmark.logging.Loggable;

import javax.jms.JMSException;

public class MessageBrokerConnectionFactory implements Loggable {

    public AbstractMessageBroker createMessageBroker(MessageBrokerType type, Protocol protocol) throws JMSException {
        log().info("Creating connection....");

        if (type.equals(MessageBrokerType.HORNETQ)) {
            return new HornetqMessageBroker(protocol.getPort());
        } else if (type.equals(MessageBrokerType.ACTIVEMQ)) {
            return new ActiveMqMessageBroker(protocol.getPort());
        } else if (type.equals(MessageBrokerType.RABBITMQ)) {
            log().error("{} message broker type is not implemented.", type);
            System.exit(0);
        }

        return null;
    }
}
