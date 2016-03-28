package demo.benchmark.enums;

import java.util.Arrays;

public enum Protocol {

    OPEN_WIRE("61616"),
    STOMP("61613"),
    AMQP("5672");

    private final String port;

    Protocol(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public static Protocol isValid(String protocol) {
        for (Protocol p : Protocol.values()) {
            if (p.name().equals(protocol)) {
                return Protocol.valueOf(protocol);
            }
        }
        throw new RuntimeException("Invalid protocol Type. Accepted values are: " + Arrays.asList(Protocol.values()));
    }

}
