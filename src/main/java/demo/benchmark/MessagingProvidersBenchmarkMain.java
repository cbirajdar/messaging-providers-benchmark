package demo.benchmark;

import demo.benchmark.database.DatabaseServer;
import demo.benchmark.enums.MessageBrokerType;
import demo.benchmark.enums.Protocol;

public class MessagingProvidersBenchmarkMain {

    public static void main(String[] args) throws Exception {
        MessageBrokerType type = MessageBrokerType.isValid(System.getProperty("broker_type"));
        Protocol protocol = Protocol.isValid(System.getProperty("protocol"));
        AbstractMessageBroker messageBroker = new MessageBrokerConnectionFactory().createMessageBroker(type, protocol);
        DatabaseServer databaseServer = new DatabaseServer();
        int totalIterations = messageBroker.numberOfIterations;
        messageBroker.setDbServer(databaseServer);
        for (int i = 1; i <= totalIterations; i++) {
            messageBroker.init(i);
            messageBroker.enqueue();
            messageBroker.dequeue();
            messageBroker.waitForThreadPoolTermination();
        }
        messageBroker.closeConnection();
        databaseServer.printResults(totalIterations);
        databaseServer.close();
    }

}
