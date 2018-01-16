package net.icnslab.netcg.datacontainer;

import java.util.List;

/**
 * Created by Lab_kairos on 2016-08-22.
 */
public class HostInfo {

    private String IP;
    private List<Integer> ports;

    public HostInfo(String IP, List<Integer> ports) {
        this.IP = IP;
        this.ports = ports;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public void addPort(int port) {
        this.ports.add(port);
    }

    public int getMaxPort() {
        int max = 0;
        for (int i = 0; i < ports.size(); i++) {
            max = ports.get(i) > max ? ports.get(i) : max;
        }
        return max;
    }

}
