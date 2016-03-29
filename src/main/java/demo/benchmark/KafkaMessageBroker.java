package demo.benchmark;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

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
        for (int i = 0; i < enqueue_count; i++) {
            producer.send(new ProducerRecord<>(QUEUE, "Test", "Test"));
        }
    }

    @Override public void dequeue() {
        consumer.subscribe(Arrays.asList(QUEUE));
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            log().info("Received message: {}", record.value());
        }
    }

    @Override public void closeConnection() {
        producer.close();
        consumer.close();
    }
}
