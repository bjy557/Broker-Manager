package net.icnslab.netcg.db;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Created by nexusz99 on 16. 8. 11.
 */
public class DatabaseClient {

    public static JSONObject getBrokers() {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://163.180.117.45:8080/brokers").asJson();
            JsonNode node = jsonResponse.getBody();
            return node.getObject();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addBroker(String broker_id, String host_ip, String container_id, int port) {
        JSONObject broker = new JSONObject();
        broker.put("host", host_ip);
        broker.put("broker_id", broker_id);
        broker.put("container_id", container_id);
        broker.put("port", port);

        try {
            HttpResponse<String> response = Unirest.post("http://163.180.117.45:8080/brokers")
                    .header("Content-Type", "application/json")
                    .body(broker).asString();
            if (response.getStatus() != 200)
                System.out.println(response.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }


    }
}
