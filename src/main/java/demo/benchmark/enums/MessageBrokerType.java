package demo.benchmark.enums;

import java.util.Arrays;

public enum MessageBrokerType {

    HORNETQ,
    ACTIVEMQ,
    RABBITMQ,
    KAFKA;

    public static MessageBrokerType isValid(String type) {
        for (MessageBrokerType brokerType : MessageBrokerType.values()) {
            if (brokerType.name().equals(type)) {
                return MessageBrokerType.valueOf(type);
            }
        }
        throw new RuntimeException("Invalid message broker Type. Accepted values are: " + Arrays.asList(MessageBrokerType.values()));
    }
}
