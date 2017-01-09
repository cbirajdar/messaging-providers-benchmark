package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;

import javax.jms.JMSException;

public class JmsProvidersBenchmarkMain {

    public static void main(String[] args) throws JMSException {
        MessageBrokerType type = MessageBrokerType.valueOf(args[0]);
        AbstractMessageBroker messageBroker = new MessageBrokerConnectionFactory().createMessageBroker(type, Protocol.STOMP);
        messageBroker.enqueue();
        messageBroker.dequeue();
        messageBroker.closeConnection();
    }

}
