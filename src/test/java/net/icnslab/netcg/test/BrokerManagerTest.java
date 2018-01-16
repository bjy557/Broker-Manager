package net.icnslab.netcg.test;

import net.icnslab.netcg.core.BrokerManager;
import org.junit.Test;

/**
 * Created by lab_ShinEunSeop on 2016-08-16.
 */
public class BrokerManagerTest {

    @Test
    public void MainTest() {
        BrokerManager bm = new BrokerManager("asdf");

        bm.start();

        System.out.println("testsetset");
    }

}
