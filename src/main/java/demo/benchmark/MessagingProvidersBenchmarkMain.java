package demo.benchmark;

import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;
import org.apache.commons.lang3.StringUtils;

import javax.jms.JMSException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessagingProvidersBenchmarkMain {

    public static void main(String[] args) throws Exception {
        MessageBrokerType type = MessageBrokerType.isValid(System.getProperty("broker_type"));
        Protocol protocol = Protocol.isValid(System.getProperty("protocol"));
        AbstractMessageBroker messageBroker = new MessageBrokerConnectionFactory().createMessageBroker(type, protocol);
        messageBroker.init();
        messageBroker.enqueue();
        messageBroker.dequeue();
        messageBroker.closeConnection();
        messageBroker.getDbServer().printResults();
    }

}
