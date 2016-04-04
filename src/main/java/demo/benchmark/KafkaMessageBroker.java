package demo.benchmark;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaMessageBroker extends AbstractMessageBroker {

    private KafkaProducer<Integer, String> producer;

    private KafkaConsumer<Integer, String> consumer;

    public KafkaMessageBroker(String port) {
        createConnection(port);
    }

    @Override public void createConnection(String port) {
        String host = String.join(":", hostname, port);
        createProducer(host);
        createConsumer(host);
    }

    private void createProducer(String host) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "TestProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(props);
    }

    private void createConsumer(String host) {
        /*
         * Properties based on example the following example,
         * https://github.com/apache/kafka/blob/trunk/examples/src/main/java/kafka/examples/Consumer.java
         */
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "TestConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "TestProducer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer = new KafkaConsumer<>(props);
    }

    @Override public void enqueue() {
        Runnable runnable = () -> stream(enqueue_count, i -> producer.send(new ProducerRecord<>(QUEUE, i, String.valueOf(i))), "Enqueue");
        submit(producerThreads, runnable);
    }

    @Override public void dequeue() {
        consumer.subscribe(Collections.singletonList(QUEUE));
        Runnable runnable = () -> {
            while(true) {
                ConsumerRecords<Integer, String> records = consumer.poll(1000);
                log().info("Received: {}", records.count());
                if (records.count() == 0) break;
                for (ConsumerRecord<Integer, String> record : records) {
                    // log().info(record.value()); //Do something with the value
                }
            }
        };
        submit(consumerThreads, runnable);
    }

    @Override public void closeConnection() {
        waitForThreadPoolTermination();
        producer.close();
        consumer.close();
    }
}
