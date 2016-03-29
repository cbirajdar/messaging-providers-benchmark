package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;

import javax.jms.JMSException;

public class JmsProvidersBenchmarkMain {

    public static void main(String[] args) throws Exception {
        MessageBrokerType type = MessageBrokerType.isValid(System.getProperty("broker_type"));
        Protocol protocol = Protocol.isValid(System.getProperty("protocol"));
        AbstractMessageBroker messageBroker = new MessageBrokerConnectionFactory().createMessageBroker(type, protocol);
        messageBroker.enqueue();
        messageBroker.dequeue();
        messageBroker.closeConnection();
    }

}
