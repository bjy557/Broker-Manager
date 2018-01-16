package net.icnslab.netcg;

import net.icnslab.netcg.datacontainer.HostInfoWrapper;
import net.icnslab.netcg.db.DatabaseClient;
import org.junit.Test;

/**
 * Created by Lab_kairos on 2016-08-22.
 */
public class HostInfoWrapperTest {
    @Test
    public void getHostInfoList() throws Exception {
        HostInfoWrapper.getHostInfoList(DatabaseClient.getBrokers());
    }

}