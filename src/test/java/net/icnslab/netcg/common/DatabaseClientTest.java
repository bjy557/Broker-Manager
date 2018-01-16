package net.icnslab.netcg.common;

import net.icnslab.netcg.db.DatabaseClient;
import org.junit.Test;

/**
 * Created by nexusz99 on 16. 8. 11.
 */
public class DatabaseClientTest {

    @Test
    public void testgetBrokers() {
        System.out.println(DatabaseClient.getBrokers());
    }
}
