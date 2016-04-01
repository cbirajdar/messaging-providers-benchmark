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

    private ExecutorService executorService;

    public void createThreadPoolExecutor() {
        log().info("Creating ThreadPoolExecutor with Producer Threads: {}, Consumer Threads: {}.", producerThreads, consumerThreads);
        executorService = Executors.newFixedThreadPool(producerThreads + consumerThreads);
    }

    public abstract void createConnection(String port) throws Exception;

    public void enqueue() throws Exception {
        MessageProducer messageProducer = session.createProducer(queue);
        Runnable runnable = () -> stream(enqueue_count, i -> send(messageProducer, String.valueOf(i)), "Enqueue");
        submit(producerThreads, runnable);
    }

    public void dequeue() throws Exception {
        MessageConsumer messageConsumer = session.createConsumer(queue);
        Runnable runnable = () -> stream(enqueue_count, i -> receive(messageConsumer), "Dequeue");
        submit(consumerThreads, runnable);
    }

    protected void submit(int numberOfThreads, Runnable runnable) {
        IntStream.range(0, numberOfThreads).forEach(i -> executorService.submit(runnable));
    }

    protected void stream(int n, IntConsumer action, String type) {
        long startTime = System.currentTimeMillis();
        IntStream.range(0, n).forEach(action);
        long endTime = System.currentTimeMillis();
        log().info("******** Time to {}: {} ********", type, endTime - startTime);
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
        connection.close();
    }

    protected void waitForThreadPoolTermination() {
        while (true) {
            if (executorService instanceof ThreadPoolExecutor) {
                if (((ThreadPoolExecutor) executorService).getActiveCount() == 0) {
                    executorService.shutdownNow();
                    break;
                }
            }
        }
        log().info("Closing connection....");
    }
}
