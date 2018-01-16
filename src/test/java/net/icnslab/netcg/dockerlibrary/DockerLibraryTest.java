package net.icnslab.netcg.dockerlibrary;

/**
 * Created by lab_ShinEunSeop on 2016-08-11.
 */

import alicek106.DockerManager.DockerManager;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerInfo;
import org.junit.Test;

public class DockerLibraryTest {

    @Test
    public void test() {

        DockerManager dm = new DockerManager();

        // 1. connect to docker daemon
        try {
            dm.ConnectDockerDaemon("myhost", "http://52.78.85.231:4243");
            System.out.println("connect success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. get docker client instance
        DockerClient dc = dm.GetDockerClient("myhost");


        // 3. get container info
        try {
            ContainerInfo info = dm.InspectContainer("mcp_dashboard", dc);
            System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. create container
        try {
                    /* arg (1) : port -> 11111:80 (connect host 11111 port to container 80 port)
					 *           or 80 (connect random host port to container 80 port)
					 *           example ) "" or "11111:80" or "80" or "80 3306"
					 * arg (2) : DNS. Don't care ( example : "" )
					 * arg (3) : Link. Don't care ( example : "" )
					 * arg (4) : privileged. Don't care ( example : false )
					 * arg (5) : Publish All Port. Don't care ( example : false)
					 * arg (6) : Volume from. Dont care ( example : "" )
					 * arg (7) : container name. optional.
					 * 			 example ) "" -> random name, or "testcontainer"
					 * arg (8) : image name.
					 * 			 example ) "ubuntu:14.04"
					 * arg (9, 10) : attach and tty for bash. Don't care ( example : true, true)
					 * arg (11) : init command of container.
					 * 			  example ) "bin/bash" or "apachectl -DFOREGROUND"
					 * arg (12) : entry point. Don't care ( example : "")
					 * arg (13) : environment value.
					 * 			  example ) "MYSQL_ROOT_PASSWORD=1234"
					 * arg (14, 15) : memory limit(byte) and cpushare (default 1024).
					 * 				  example ) "300000000", "2048"  -> get 300MB and double cpu scheduling time
					 * arg (16) : container's host name. example ) "slave"
					 * arg (17) : dockerclient
					*/
//            ContainerInfo a = dm.CreateContainer("11111:8080 80", "", "", false, false, "", "mycontainer5", "ubuntu:14.04",
//                    true, true, "bin/bash", "", "", "30000000", "2048", "mycon", dc);

            ContainerInfo info = dm.CreateContainer("1234:1883", "", "", false, false, "",
                    "test", "broker:0.1", true, true, "",
                    "", "MQTT_CLUSTER_HOST=" + "1234" + " MQTT_CLUSTER_BROKER_ID=" + "5678",
                    "100000000", "2048", "Broker", dc);

            System.out.println("create success " + info.id());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
