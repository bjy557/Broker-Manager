package net.icnslab.netcg.core;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.proto.messages.AbstractMessage;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathConfig;
import io.moquette.server.config.FilesystemConfig;
import io.moquette.server.config.IConfig;
import io.moquette.spi.ClientSession;
import io.moquette.spi.ISessionsStore;
import io.moquette.spi.impl.ProtocolProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lab_kairos on 2016-08-22.
 */
public class SystemBroker {

    private Logger log = Logger.getLogger(this.getClass());

    private final Server server = new Server();
    private BrokerManager brokerManager;

    public SystemBroker(BrokerManager brokerManager) {
        this.brokerManager = brokerManager;
    }

    public void start() {
        System.out.println("System Broker start");
        // MQTT Broker field
        //final IConfig classPathConfig = new ClasspathConfig();
        SystemBroker.ConnectListener connectListener = new SystemBroker.ConnectListener(server);
        final List<? extends InterceptHandler> userHandler = Arrays.asList(connectListener);

        // Broker server Start
        try {
        	server.startServer();
            //server.startServer(new FilesystemConfig(), userHandler);
        } catch (IOException e) {
        	System.out.println("System Broker Start Fail");
        	System.out.println(e.getStackTrace());
        }

        System.out.println("moquette mqtt broker started, press ctrl-c to shutdown..");

    }

    /**
     * Stop System Broker
     */
    public void stop() {
        server.stopServer();
    }

    /**
     * MQTT Client Connection Handler
     */
    private class ConnectListener extends AbstractInterceptHandler {

        private Server server;

        private ProtocolProcessor m_processor;
        private ISessionsStore m_sessionStore;
        private Method m_directSend;

        // constructor
        ConnectListener(Server server) {
            super();
            this.server = server;
        }

        // init member fields to get private member fields & method of Server (to use directSend())
        private void setClientsMap() {
            try {

                // get Server Member Fields and Method
                Field f_processor = Server.class.getDeclaredField("m_processor");
                Field f_sessionsStore = ProtocolProcessor.class.getDeclaredField("m_sessionsStore");
                Method m_directSend = ProtocolProcessor.class.getDeclaredMethod("directSend", ClientSession.class,
                        String.class, AbstractMessage.QOSType.class, ByteBuffer.class, boolean.class, Integer.class);

                // change Accessible of Fields (private->public)
                f_processor.setAccessible(true);
                f_sessionsStore.setAccessible(true);

                // change Accessible of Method (protected->public)
                m_directSend.setAccessible(true);

                // init member field amd Method
                this.m_processor = (ProtocolProcessor) f_processor.get(this.server);
                this.m_sessionStore = (ISessionsStore) f_sessionsStore.get(this.m_processor);
                this.m_directSend = m_directSend;

            } catch (Exception e) {
                e.getStackTrace();
            }
        }


        // send direct message to clientSession use ProtocolProcessor.directSend()
        private void directSend(ClientSession session, String topic, AbstractMessage.QOSType qos, String brokerToConnect,
                                boolean retained, Integer messageID) {
            try {
                // String brokerToConnect to ByteBuffer
                ByteBuffer buf = ByteBuffer.wrap(brokerToConnect.getBytes());

                // use directSend()
                m_directSend.invoke(this.m_processor, session, topic, qos, buf, retained, messageID);

            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        // onConnect send proper Broker IP
        @Override
        public void onConnect(InterceptConnectMessage msg) {
        	System.out.println("Connection in coming : " + msg.getClientID());

            setClientsMap();

            ClientSession clientSession = this.m_sessionStore.sessionForClient(msg.getClientID());

            String brokerHost = brokerManager.containerManager.getProperBroker();

            directSend(clientSession, "redirect_broker_connection", AbstractMessage.QOSType.MOST_ONE, brokerHost, false, null);

            System.out.println("send BrokerIP : " + brokerHost + " to " + msg.getClientID());
        }
    }
}
