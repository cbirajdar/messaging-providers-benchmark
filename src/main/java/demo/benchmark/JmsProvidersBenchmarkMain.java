package demo.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

public class JmsProvidersBenchmarkMain {

    private static final Logger log = LoggerFactory.getLogger(JmsProvidersBenchmarkMain.class);

    public static void main(String[] args) throws JMSException {
        AbstractMessageBroker messageBroker;
        String className = new StringBuilder("demo.benchmark.").append(args[0]).append("MessageBroker").toString();
        try {
            Class clazz = Class.forName(className);
            messageBroker = (AbstractMessageBroker) clazz.newInstance();
        } catch (Exception e) {
            log.error("Invalid argument! Please set the -Dexec.args to Hornetq or ActiveMq or RabbitMq");
            return;
        }
        log.info("Creating connection....");
        messageBroker.createConnection();
        log.info("Enqueue elements....");
        messageBroker.enqueue();
        log.info("Dequeue elements....");
        messageBroker.dequeue();
        log.info("Closing connection....");
        messageBroker.closeConnection();
    }

}
