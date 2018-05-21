package weixin.popular.corpapi;

import org.junit.Test;
import weixin.popular.smallAppApi.SmallAppUserAPI;

import static org.junit.Assert.*;

public class SmallAppTest {

    @Test
    public void testjscode2session(){

        SmallAppUserAPI.SessionResp resp = SmallAppUserAPI.jscode2session("","","061JpSjV0BL0FU133YgV0pG7kV0JpSjn");

        assertTrue(resp.getOpenid()!=null);

        System.out.println(resp.getOpenid());
    }
}
