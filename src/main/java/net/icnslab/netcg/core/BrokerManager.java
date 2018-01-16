package net.icnslab.netcg.core;

import net.icnslab.netcg.datacontainer.HostInfo;
import net.icnslab.netcg.datacontainer.HostInfoWrapper;
import net.icnslab.netcg.db.DatabaseClient;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 2016 NetChallenge Broker Manager
 * <p>
 * Created by lab_ShinEunSeop on 2016-08-01.
 * <p>
 * Manager of Brokers
 */
public class BrokerManager {

    /**
     * Store Brokers IP and ports
     *
     */
    List<HostInfo> brokers;
    ContainerManager containerManager = new ContainerManager(this);
    SystemBroker systemBroker = new SystemBroker(this);
    /**
     * Temporary LB variable
     */
    int nextIpIdx;
    int nextPortIdx;
    int clientConn;

    String MANAGER_HOST = "";
    public String host = "";

    /**
     * The logger
     * @see Logger
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Constructor
     * Initialize variable and Brokers
     * When no available Broker exist, Create new Broker Container
     */
    public BrokerManager(String m_host) {
        host = m_host;
        MANAGER_HOST = m_host+":1883";
        System.out.println("host : " + m_host);
    }

    void reloadBrokers() {
        brokers = HostInfoWrapper.getHostInfoList(DatabaseClient.getBrokers());

        System.out.println("Brokers reloaded");
    }

    /**
     * start interface
     */
    public void start() {
    	System.out.println("Broker Manager Start");

        // initialize
        nextIpIdx = 0;
        nextPortIdx = 0;
        clientConn = 0;

        reloadBrokers();

        if (brokers.get(0).getPorts().size() == 0) {
            containerManager.createBroker();
        }

        containerManager.start();
        systemBroker.start();

    }

    public void stop() {
    	System.out.println("Broker Manager Stop");

        containerManager.stop();
        systemBroker.stop();
    }
}
