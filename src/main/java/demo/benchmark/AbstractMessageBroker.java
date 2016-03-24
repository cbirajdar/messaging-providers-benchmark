package demo.benchmark;

import org.apache.activemq.transport.stomp.StompConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

abstract class AbstractMessageBroker {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    Connection connection;

    Queue queue;

    Session session;

    final String QUEUE = "TestQueue";

    public abstract void createConnection() throws JMSException;

    public void enqueue() throws JMSException {
        long startTime = System.currentTimeMillis();
        MessageProducer messageProducer = session.createProducer(queue);
        for (int i = 0; i < 1000; i++) {
            messageProducer.send(session.createTextMessage(String.valueOf(i)));
        }
        long endTime = System.currentTimeMillis();
        log.info("******** Time to Enqueue: {} ********", endTime - startTime);
    }

    public void dequeue() throws JMSException {
        long startTime = System.currentTimeMillis();
        MessageConsumer messageConsumer = session.createConsumer(queue);
        for (int i = 0; i < 1000; i++) {
            messageConsumer.receive();
        }
        long endTime = System.currentTimeMillis();
        log.info("******** Time to Dequeue: {} ********", endTime - startTime);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }
}
