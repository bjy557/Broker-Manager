package net.icnslab.netcg.core;

import alicek106.DockerManager.DockerManager;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerInfo;
import net.icnslab.netcg.datacontainer.HostInfo;
import net.icnslab.netcg.db.DatabaseClient;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Lab_kairos on 2016-08-22.
 */
public class ContainerManager {

    private Logger log = Logger.getLogger(this.getClass());

    private BrokerManager brokerManager;

    public ContainerManager(BrokerManager brokerManager) {
        this.brokerManager = brokerManager;
    }

    public void start() {
    	System.out.println("Container Manager Start");

        // Create Starting Broker
        if (brokerManager.brokers.size() == 0)
            createBroker();
    }

    // TODO stop
    public void stop() {
        log.info("Broker Manager Stop");
    }

    /**
     * Get Proper Docker host to assign client
     *
     * @return host ip and port, type "IP:port"
     */
    private HostInfo getProperDockerHost() {

        // load host data
        int size = brokerManager.brokers.size();
        HostInfo properHost = null;

        // select host that minimum opened-port
        for (int i = 0; i < size; i++) {
            //hostInfo
            HostInfo hostInfo = brokerManager.brokers.get(i);

            if (properHost == null || properHost.getPorts().size() > hostInfo.getPorts().size()) {
                properHost = hostInfo;
            }
        }
        //TODO need to confirm
        return properHost;
    }

    /**
     * Generate BrokerID SHA-256
     *
     * @param s String to generate SHA-256, in this project use host ip and port
     * @return Broker ID (SHA-256 string) when cannot found SHA algorithm null returned
     */
    private String generateBrokerID(String s) {
        String SHA;
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(s.getBytes());
            byte byteData[] = sh.digest();
            StringBuilder sb = new StringBuilder();

            for (byte character : byteData) {
                sb.append(Integer.toString((character & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    /**
     * Create New Broker in
     */
    void createBroker() {

        // update brokers
        brokerManager.reloadBrokers();

        // get Container IP and port
        HostInfo host = getProperDockerHost();

        int port = host.getMaxPort() == 0 ? 55500 : host.getMaxPort() + 1;

        // Env Setting
        String MQTT_CLUSTER_HOST = "tcp://"+brokerManager.MANAGER_HOST;
        String MQTT_CLUSTER_BROKER_ID = generateBrokerID(MQTT_CLUSTER_HOST + new Random().nextInt(100));
        String DB_HOST = "http://"+brokerManager.host+":8080";

        // Container Create
        DockerManager dm = new DockerManager();
        try {
            // connect to docker daemon
            dm.ConnectDockerDaemon("myhost", "http://" + host.getIP() + ":4243");
            System.out.println("connect success");

            // get docker client instance
            DockerClient dc = dm.GetDockerClient("myhost");

            // create container
            ContainerInfo info = dm.CreateContainer(port + ":1883", "", "", false, false, "",
                    MQTT_CLUSTER_BROKER_ID, "broker:0.1", true, true, "",
                    "", "MQTT_CLUSTER_HOST=" + MQTT_CLUSTER_HOST + " MQTT_CLUSTER_BROKER_ID=" + MQTT_CLUSTER_BROKER_ID +
                    " DB_HOST=" + DB_HOST, "100000000", "2048", "Broker", dc);

            // Register Broker
            DatabaseClient.addBroker(MQTT_CLUSTER_BROKER_ID, host.getIP(), info.id(), port);

            log.info("Container Created : " +host.getIP()+":"+port );
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Container Creation Error");
            System.out.println(e.getStackTrace());
        }

    }

    // Get Proper Broker Info
    String getProperBroker() {

        // update brokers
        brokerManager.reloadBrokers();

        HostInfo hostInfo = brokerManager.brokers.get(brokerManager.nextIpIdx);
        String ip = hostInfo.getIP();
        int port = hostInfo.getPorts().get(brokerManager.nextPortIdx++);

        
        // not need after
        if (brokerManager.nextPortIdx >= hostInfo.getPorts().size()) {
            brokerManager.nextIpIdx++;
            brokerManager.nextPortIdx = 0;
        }
        if (brokerManager.nextIpIdx >= brokerManager.brokers.size()) {
            brokerManager.nextIpIdx = 0;
        }

        return "tcp://" + ip + ":" + port;
    }
}
