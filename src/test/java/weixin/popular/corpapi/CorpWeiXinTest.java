package weixin.popular.corpapi;

import org.junit.Test;
import weixin.popular.bean.Ticket;
import weixin.popular.bean.Token;
import weixin.popular.util.JsUtil;

import static org.junit.Assert.*;

/**
 * 企业微信测试
 */
public class CorpWeiXinTest {

    @Test
    public  void TestGetAccessToken(){

        Token token = CorpTokenAPI.token("wx9f383452d7edb40b","GQjaaNK2bFHHZ-YuoMbbY0ULdoKoFE3lNLAMxB12AFI");
        assertEquals(token.getErrcode(),"0");

        //获取ticket

        Ticket ticket = CorpTicketAPI.ticket(token.getAccess_token());
        assertEquals(ticket.getErrcode(),"0");

    }

    @Test
    public void testJsSign(){

        String sign = JsUtil.generateConfigSignature("Wm3WZYTPz0wzccnW",
                "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg",
                "1414587457",
                "http://mp.weixin.qq.com?params=value");

        assertEquals("0f9de62fce790f9a083d5c99e95740ceb90c27ed",sign);

    }



}
