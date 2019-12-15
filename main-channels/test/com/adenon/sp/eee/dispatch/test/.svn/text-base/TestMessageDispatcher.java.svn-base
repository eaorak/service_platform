package com.adenon.sp.eee.dispatch.test;

import org.junit.Test;
import org.mvel2.MVEL;

public class TestMessageDispatcher {

    @Test
    public void testCompile() throws Exception {
        String expr1 = "import com.adenon.sp.test.TestMessage;"
                       + "def dispatch(p_msg) {"
                       + "    message = (com.adenon.sp.test.TestMessage) p_msg;"
                       + "    if ((message.message contains 'message')) {"
                       + "        message.message = 'hebele';"
                       + "        return 'com.adenon.sp.service.test.TestService';"
                       + "    }"
                       + "}"
                       + "serviceId = dispatch(msg);"
                       + "if (serviceId == null) {"
                       + "    throw new RuntimeException('No service could be found for message ['+com.adenon.sp.test.TestMessage.name+']');"
                       + "}"
                       + "dialog.serviceUniqueId = serviceId;";
        MVEL.compileExpression(expr1);
    }
}
