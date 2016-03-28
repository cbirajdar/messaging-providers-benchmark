package demo.benchmark.enums;

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
}
