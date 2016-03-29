package demo.benchmark;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqMessageBroker extends AbstractMessageBroker {

    private Connection connection;

    private Channel channel;

    public RabbitMqMessageBroker(String port) throws Exception {
        createConnection(port);
    }

    @Override public void createConnection(String port) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE, false, false, false, null);
    }

    @Override public void enqueue() throws IOException {
        log().info("Sending....");
        for (int i = 0; i < enqueue_count; i++) {
            channel.basicPublish("", QUEUE, null, String.valueOf(i).getBytes());
        }
    }

    @Override public void dequeue() throws IOException {
        log().info("Receiving....");
        Consumer consumer = new DefaultConsumer(channel);
        for (int i = 0; i < enqueue_count; i++) {
            channel.basicConsume(QUEUE, true, consumer);
        }
    }

    @Override public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
