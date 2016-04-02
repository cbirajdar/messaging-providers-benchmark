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
        factory.setHost(hostname);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE, false, false, false, null);
    }

    @Override public void enqueue() {
        submit(producerThreads, () -> stream(enqueue_count, i -> publish(String.valueOf(i)), "Enqueue"));
    }

    private void publish(String data) {
        try {
            channel.basicPublish("", QUEUE, null, String.valueOf(data).getBytes());
        } catch (IOException e) {
            log().error("Error publishing data to the queue", e);
        }
    }

    @Override public void dequeue() throws IOException {
        Consumer consumer = new DefaultConsumer(channel);
        submit(consumerThreads, () -> stream(enqueue_count, i -> consume(consumer), "Dequeue"));
    }

    private void consume(Consumer consumer) {
        try {
            channel.basicConsume(QUEUE, true, consumer);
        } catch (IOException e) {
            log().error("Error consuming data from the queue", e);
        }
    }

    @Override public void closeConnection() throws IOException, TimeoutException {
        waitForThreadPoolTermination();
        channel.close();
        connection.close();
    }
}
