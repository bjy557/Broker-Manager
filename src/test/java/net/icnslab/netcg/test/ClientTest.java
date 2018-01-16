package net.icnslab.netcg.test;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Client TEST
 * Created by lab_ShinEunSeop on 2016-08-01.
 */
public class ClientTest implements Runnable {
    public static void main(String args[]) throws InterruptedException {

        Runnable r = new ClientTest();
        for (int i = 0; i < 21; i++) {
            Thread t = new Thread(r);
            t.start();
        }
    }

    public void StartClient() {
        // Creating a MQTT Client using Eclipse Paho
        String topic = "news";
        String content = "Visit www.hascode.com! :D";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "paho-java-client";

        try {
            //MQTT Client connect & connect option
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);
            sampleClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable throwable) {

                }

                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("Topic : " + topic);
                    System.out.println("Message : " + mqttMessage.toString());
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

            //System.out.println("paho-client connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            //System.out.println("paho-client connected to broker");

            // MQTT Client message publish
            //System.out.println("paho-client publishing message: " + content);
            //MqttMessage message = new MqttMessage(content.getBytes());
            //message.setQos(qos);
            //sampleClient.publish(topic, message);
            //System.out.println("paho-client message published");

            // MQTT Client disconnect
            //sampleClient.disconnect();
            //System.out.println("paho-client disconnected");

        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    public void run() {
        StartClient();
    }
}
