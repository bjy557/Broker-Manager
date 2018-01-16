package net.icnslab.netcg.datacontainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lab_kairos on 2016-08-22.
 * Json String to ArrayList<HostInfo>
 */
public class HostInfoWrapper {
    public static List<HostInfo> getHostInfoList(JSONObject hostJson) {
        List<HostInfo> brokers = new ArrayList<HostInfo>();
        JSONArray arr = (JSONArray) hostJson.get("hosts");

        for (int i = 0; i < arr.length(); i++) {

            JSONObject j_info = arr.getJSONObject(i);
            JSONArray j_ports = (JSONArray) j_info.get("port");

            String ip = j_info.getString("ip");
            List<Integer> ports = new ArrayList<Integer>();
            for (int j = 0; j < j_ports.length(); j++) {
                ports.add(j_ports.getInt(j));
            }

            HostInfo hostInfo = new HostInfo(ip, ports);

            brokers.add(hostInfo);
        }

        return brokers;
    }
}
