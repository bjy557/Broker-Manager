package net.icnslab.netcg.main;

import net.icnslab.netcg.core.BrokerManager;

import java.io.IOException;

/**
 * Created by lab_ShinEunSeop on 2016-08-01.
 */

public class Main {

    // main
    public static void main(String args[]) throws IOException {

        final BrokerManager bm = new BrokerManager(args[0]);
        //final BrokerManager bm = new BrokerManager(0);
        bm.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bm.stop();
            }
        });
    }

}
