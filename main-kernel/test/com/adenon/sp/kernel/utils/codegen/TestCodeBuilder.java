package com.adenon.sp.kernel.utils.codegen;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.adenon.sp.kernel.codegen.AssistService;
import com.adenon.sp.kernel.services.IAssistService;
import com.adenon.sp.kernel.utils.assist.Advice;
import com.adenon.sp.kernel.utils.assist.IAdviceListener;
import com.adenon.sp.kernel.utils.assist.IAssistBuilder;


public class TestCodeBuilder {

    private static IAssistService assistService;

    @BeforeClass
    public void init() {
        assistService = AssistService.getInstance();
    }

    @Test
    @Ignore
    public void testGenerationWithExtend() throws Exception {
        final Service2 service = new Service2();
        final IAssistBuilder builder = assistService.createProxyFor(service, true);
        final ISrv myService = builder.register(Advice.BEFORE, "System.out.println(\"this is before\");")
        // .register(Advice.AROUND, "System.out.println(\"this is around\");")
                                          .register(Advice.AFTER, "System.out.println(\"this is after\");")
                                          .generate(ISrv.class);
        myService.hebe(19, "ehuehehe");
        myService.hube();
        System.out.println();
    }

    @Test
    public void testGenerationWithoutExtend() throws Exception {
        final Srv service = new Srv();
        final IAssistBuilder builder = assistService.createProxyFor(service, true);
        final ISrv myService = builder.register(Advice.BEFORE, "System.out.println(\"....this is before\");")
                                          .register(Advice.AFTER, "System.out.println(\"....this is after\");")
                                          .generate(ISrv.class);
        myService.hebe(19, "ehuehehe");
        myService.hube();
        ((Srv) myService).hebele(121, "ekiekieki", this);
    }

    @Test
    public void testGenericLogging() throws Exception {
        final ServiceLogging service = new ServiceLogging();
        final IAssistBuilder builder = assistService.createProxyFor(service, false).addInterface(ISrv.class);
        final ISrv serviceProxy = (ISrv) assistService.addLoggingTo(builder);
        serviceProxy.hebe(19, "ssss");
        serviceProxy.hube();
    }

    @Test
    public void testLogging() throws Exception {
        final ServiceLogging service = new ServiceLogging();
        final IAssistBuilder builder = assistService.createProxyFor(service, false);

        final StringBuilder before = new StringBuilder();
        before.append("long start = System.currentTimeMillis();").append(IAssistBuilder.NLT);
        before.append("try {").append(IAssistBuilder.NLT);
        log2(before, "\"Params :: \"+Arrays.toString($args)");
        //
        final StringBuilder after1 = new StringBuilder();
        after1.append("} finally {").append(IAssistBuilder.NLT);
        log2(after1, "\"Delta :: [*\" + (System.currentTimeMillis() - start) + \"* ms]\"");
        after1.append("}").append(IAssistBuilder.NLT);
        //
        final StringBuilder after2 = new StringBuilder();
        after2.append("} catch (Throwable e) {").append(IAssistBuilder.NLT);
        after2.append("_$logger.error(\"Error ! :: \", e);").append(IAssistBuilder.NLT);
        after2.append("throw e;");
        //
        final ISrv myService = builder.addInterface(ISrv.class)
                                          .addImport(Logger.class, Arrays.class)
                                          .addField("private Logger _$logger = Logger.getLogger(" + service.getClass().getName() + ".class);")
                                          .register(Advice.BEFORE, before.toString())
                                          .register(Advice.AFTER, after2.toString(), new IAdviceListener() {
                                              @Override
                                              public boolean addFor(final Advice advice, final Method m, final String code) {
                                                  return !m.toString().contains("throws");
                                              }
                                          })
                                          .register(Advice.AFTER, after1.toString())
                                          .generate(ISrv.class);
        myService.hebe(19, "ehuehehe");
        myService.hube();
        // myService.hebele(121, "ehuehe", this);
    }

    private static void log(final StringBuilder body, final String code) {
        body.append("if (_$logger.isDebugEnabled()) {").append(IAssistBuilder.NLT);
        body.append("_$logger.debug(").append(code).append(");").append(IAssistBuilder.NLT);
        body.append("}").append(IAssistBuilder.NLT);
    }

    private static void log2(final StringBuilder body, final String code) {
        body.append("System.out.println(").append(code).append(");").append(IAssistBuilder.NLT);
    }

    public static void main(final String[] args) throws Exception {
        new TestCodeBuilder().testGenerationWithoutExtend();
    }

}
