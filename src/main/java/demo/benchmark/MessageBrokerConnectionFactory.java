package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;
import demo.benchmark.logging.Loggable;

import javax.jms.JMSException;

public class MessageBrokerConnectionFactory implements Loggable {

    public AbstractMessageBroker createMessageBroker(MessageBrokerType type, Protocol protocol) throws JMSException {
        log().info("Creating connection....");

        if (type.equals(MessageBrokerType.Hornetq)) {
            return new HornetqMessageBroker(protocol.getPort());
        } else if (type.equals(MessageBrokerType.ActiveMq)) {
            return new ActiveMqMessageBroker(protocol.getPort());
        } else if (type.equals(MessageBrokerType.RabbitMq)) {
            log().error("{} message broker type is not implemented.");
            System.exit(0);
        }

        return null;
    }
}
