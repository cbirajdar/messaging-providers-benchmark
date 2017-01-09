package demo.benchmark;

import demo.benchmark.database.DatabaseServer;
import demo.benchmark.logging.Loggable;
import org.apache.commons.lang3.StringUtils;

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

    String hostname;

    String payload;

    int enqueueCount;

    int producerThreads;

    int consumerThreads;

    int numberOfIterations;

    final String QUEUE = "TestQueue";

    private ExecutorService executorService;

    private DatabaseServer dbServer;

    public void setDbServer(DatabaseServer dbServer) {
        this.dbServer = dbServer;
    }

    public AbstractMessageBroker() {
        hostname = getProperty("host_name");
        enqueueCount = intVal(getProperty("enqueue_count"));
        producerThreads = intVal(getProperty("producer_threads"));
        consumerThreads = intVal(getProperty("consumer_threads"));
        numberOfIterations = intVal(getProperty("number_of_iterations"));
        payload = StringUtils.repeat("*", intVal(getProperty("payload_size")));
    }

    public void init(int i) {
        log().info("Running Iteration Number: {}", i);
        createThreadPoolExecutor();
    }

    private void createThreadPoolExecutor() {
        log().info("Creating ThreadPoolExecutor with Producer Threads: {}, Consumer Threads: {}.", producerThreads, consumerThreads);
        executorService = Executors.newFixedThreadPool(producerThreads + consumerThreads);
    }

    public abstract void createConnection(String port) throws Exception;

    public void enqueue() throws Exception {
        MessageProducer messageProducer = session.createProducer(queue);
        Runnable runnable = () -> stream(enqueueCount, i -> send(messageProducer, payload), "Enqueue");
        submit(producerThreads, runnable);
    }

    public void dequeue() throws Exception {
        MessageConsumer messageConsumer = session.createConsumer(queue);
        Runnable runnable = () -> stream(enqueueCount, i -> receive(messageConsumer), "Dequeue");
        submit(consumerThreads, runnable);
    }

    protected void submit(int numberOfThreads, Runnable runnable) {
        IntStream.range(0, numberOfThreads).forEach(i -> executorService.submit(runnable));
    }

    protected void stream(int n, IntConsumer action, String type) {
        long startTime = System.currentTimeMillis();
        IntStream.range(0, n).forEach(action);
        long endTime = System.currentTimeMillis();
        dbServer.insertResults(type, endTime - startTime);
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
            messageConsumer.receive().acknowledge();
        } catch (Exception e) {
            log().error("Error consuming data from the queue", e);
        }
    }

    public void closeConnection() throws Exception {
        log().info("Closing connection....");
        session.close();
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
    }

    private int intVal(String str) {
        return Integer.valueOf(str);
    }

    private String getProperty(String key) {
        return System.getProperty(key);
    }
}
