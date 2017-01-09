package demo.benchmark;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.IntStream;

public class KafkaMessageBroker extends AbstractMessageBroker {

    private KafkaProducer<String, String> producer;

    private KafkaConsumer<String, String> consumer;

    public KafkaMessageBroker(String port) {
        createConnection(port);
    }

    @Override public void createConnection(String port) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:" + port);
        props.put("group.id", "TestExample");
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        producer = new KafkaProducer<>(props);
        consumer = new KafkaConsumer<>(props);
    }

    @Override public void enqueue() {
        Runnable runnable = () -> {
            IntStream.range(0, enqueue_count).forEach(i -> producer.send(new ProducerRecord<>(QUEUE, "Test", "Test")));
        };
        submit(producerThreads, runnable);
    }

    @Override public void dequeue() {
        consumer.subscribe(Arrays.asList(QUEUE));
        Runnable runnable = () -> {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
            }
        };
        submit(consumerThreads, runnable);
    }

    @Override public void closeConnection() {
        waitForThreadPoolTermination();
        log().info("Closing producer and consumer");
        producer.close();
        consumer.close();
    }
}
