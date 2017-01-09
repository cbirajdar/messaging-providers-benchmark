package demo.benchmark.database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import demo.benchmark.logging.Loggable;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.util.UUID;

/*
 * Based on: https://github.com/bwaldvogel/mongo-java-server
 */
public class DatabaseServer implements Loggable {

    private MongoServer server;

    private MongoClient client;

    private MongoCollection<Document> collection;

    public DatabaseServer() {
        server = new de.bwaldvogel.mongo.MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        client = new MongoClient(new ServerAddress(serverAddress));
        collection = getCollection();
    }

    public void insertResults(String key, long value) {
        Document document = new Document("_id", UUID.randomUUID().toString()).append(key, value);
        collection.insertOne(document);
    }

    public void printResults(int iterations) {
        long totalEnqueueTime = 0;
        for (Document document : collection.find()) {
            if (document.get("Enqueue") != null) {
                totalEnqueueTime += Long.parseLong(document.get("Enqueue").toString());
            }
        }
        log().info("******** Total time to Enqueue elements: {} ********", totalEnqueueTime);
        log().info("******** Average time to Enqueue elements: {} ********", totalEnqueueTime / iterations);
    }

    public void close() {
        client.close();
        server.shutdown();
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase("benchmark").getCollection("results");
    }

}
