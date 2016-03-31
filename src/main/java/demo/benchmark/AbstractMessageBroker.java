package demo.benchmark;

import demo.benchmark.logging.Loggable;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

abstract class AbstractMessageBroker implements Loggable {

    Connection connection;

    Queue queue;

    Session session;

    final String QUEUE = "TestQueue";

    final Integer enqueue_count = Integer.valueOf(System.getProperty("enqueue_count"));

    final Integer producerThreads = Integer.valueOf(System.getProperty("producer_threads"));

    final Integer consumerThreads = Integer.valueOf(System.getProperty("consumer_threads"));

    ExecutorService executorService;

    public void createThreadPoolExecutor() {
        executorService = Executors.newFixedThreadPool(producerThreads + consumerThreads);
    }

    public abstract void createConnection(String port) throws Exception;

    public void enqueue() throws Exception {
        MessageProducer messageProducer = session.createProducer(queue);
        Runnable runnable = () -> {
            long startTime = System.currentTimeMillis();
            stream(enqueue_count, i -> send(messageProducer, String.valueOf(i)));
            long endTime = System.currentTimeMillis();
            log().info("******** Time to Enqueue: {} ********", endTime - startTime);
        };
        submit(producerThreads, runnable);
    }

    public void dequeue() throws Exception {
        MessageConsumer messageConsumer = session.createConsumer(queue);
        Runnable runnable = () -> {
            long startTime = System.currentTimeMillis();
            stream(enqueue_count, i -> receive(messageConsumer));
            long endTime = System.currentTimeMillis();
            log().info("******** Time to Dequeue: {} ********", endTime - startTime);
        };
        submit(consumerThreads, runnable);
    }

    protected void submit(int numberOfThreads, Runnable runnable) {
        stream(numberOfThreads, i -> executorService.submit(runnable));
    }

    protected void stream(int n, IntConsumer action) {
        IntStream.range(0, n).forEach(action);
    }

    private void send(MessageProducer messageProducer, String data) {
        try {
            messageProducer.send(session.createTextMessage(String.valueOf(data)));
        } catch (Exception e) {
            log().error("Error sending data to the queue", e);
        }
    }

    private void receive(MessageConsumer messageConsumer) {
        try {
            messageConsumer.receive();
        } catch (Exception e) {
            log().error("Error consuming data from the queue", e);
        }

    }

    public void closeConnection() throws Exception {
        waitForThreadPoolTermination();
        log().info("Closing connection....");
        connection.close();
    }

    protected void waitForThreadPoolTermination() {
        while (true) {
            if (executorService instanceof ThreadPoolExecutor) {
                int count = ((ThreadPoolExecutor) executorService).getActiveCount();
                if (count == 0) {
                    executorService.shutdownNow();
                    break;
                }
            }
        }
    }
}
