package com.adenon.sp.kernel.codegen;

import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.log4j.Logger;

import com.adenon.sp.kernel.properties.SysProps;
import com.adenon.sp.kernel.services.IAssistService;
import com.adenon.sp.kernel.utils.StringUtils;
import com.adenon.sp.kernel.utils.assist.Advice;
import com.adenon.sp.kernel.utils.assist.IAdviceListener;
import com.adenon.sp.kernel.utils.assist.IAssistBuilder;


public class AssistService implements IAssistService {

    private final ClassPool            pool      = ClassPool.getDefault();
    private final Class<?>[]           imports   = new Class<?>[] { Logger.class, Arrays.class };
    private static String              LOG_LEVEL = "debug";
    private static final AssistService INSTANCE  = new AssistService();

    public static final AssistService getInstance() {
        return INSTANCE;
    }

    private AssistService() {
        final String spHome = SysProps.HOME_PATH.value();
        final String bundleHome = spHome + "/" + SysProps.BUNDLE_HOME_PATH.value();
        try {
            this.pool.appendClassPath(bundleHome + "/*");
        } catch (final NotFoundException e) {
            throw new RuntimeException(e);
        }
        for (final Class<?> c : this.imports) {
            this.pool.importPackage(c.getPackage().getName());
        }
        final LoaderClassPath loaderClassPath = new LoaderClassPath(this.getClass().getClassLoader());
        this.pool.appendClassPath(loaderClassPath);
    }

    @Override
    public IAssistBuilder createProxyFor(final Object target, final boolean extend) throws Exception {
        return new CodeBuilder(this.pool, target, extend);
    }

    @Override
    public Object addLoggingTo(final IAssistBuilder builder) throws Exception {
        final StringBuilder startAdvice = new StringBuilder();
        startAdvice.append("long start = System.currentTimeMillis();").append(IAssistBuilder.NLT);
        startAdvice.append("try {").append(IAssistBuilder.NLT);
        //
        final StringBuilder catchAdvice = new StringBuilder();
        catchAdvice.append("} catch (Throwable e) {").append(IAssistBuilder.NLT);
        catchAdvice.append("_$logger.error(\"Error ! :: \", e);").append(IAssistBuilder.NLT);
        catchAdvice.append("throw e;");
        //
        final StringBuilder finallyAdvice = new StringBuilder();
        finallyAdvice.append("} finally {").append(IAssistBuilder.NLT);
        log(finallyAdvice, "\"[Delta:\" + (System.currentTimeMillis() - start) + \"ms][Params:\"+Arrays.toString($args)+\"]\"");
        finallyAdvice.append("}").append(IAssistBuilder.NLT);
        //
        return builder.addImport(Logger.class, Arrays.class)
                      .addField("private Logger _$logger = Logger.getLogger(" + builder.getTarget().getClass().getName() + ".class);")
                      .register(Advice.BEFORE, startAdvice.toString())
                      .register(Advice.AFTER, catchAdvice.toString(), new IAdviceListener() {
                          @Override
                          public boolean addFor(final Advice advice, final Method m, final String code) {
                              return !m.toString().contains("throws");
                          }
                      })
                      .register(Advice.AFTER, finallyAdvice.toString())
                      .generate();
    }

    private static void log(final StringBuilder body, final String code) {
        final String level = StringUtils.cap(LOG_LEVEL);
        body.append("if (_$logger.is" + level + "Enabled()) {").append(IAssistBuilder.NLT);
        body.append("_$logger." + LOG_LEVEL + "(").append(code).append(");").append(IAssistBuilder.NLT);
        body.append("}").append(IAssistBuilder.NLT);
    }

}
