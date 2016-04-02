package demo.benchmark;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Session;

public class ActiveMqMessageBroker extends AbstractMessageBroker {

    public ActiveMqMessageBroker(String port) throws JMSException {
        createConnection(port);
    }

    @Override public void createConnection(String port) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(String.join("tcp://", hostname, ":port"));
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue(QUEUE);
    }
}
