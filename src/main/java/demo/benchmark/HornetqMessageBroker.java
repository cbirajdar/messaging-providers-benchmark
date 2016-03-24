package demo.benchmark;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public class HornetqMessageBroker extends AbstractMessageBroker {

    @Override public void createConnection() throws JMSException {
        Map<String, Object> cParams = new HashMap<String, Object>() {{
            put(TransportConstants.PORT_PROP_NAME, "61616");
            put(TransportConstants.HOST_PROP_NAME, "localhost");

        }};
        TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), cParams);
        ConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, configuration);
        connection = cf.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue(QUEUE);
    }
}
