package com.adenon.sp.see;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.osgi.framework.BundleContext;

public class JarFileTest {

    public void testJarFile() throws Exception {
        BundleContext context = null;
        String location = "file:/elron/workspace/srv_platform/platform/repository/services/service-test_01.00.00.000.jar";
        String path = location.split(":")[1];
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> entries = jarFile.entries();
        List<Class<?>> classes = new ArrayList<Class<?>>();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = path.replace("/", ".");
            }
        }
        System.out.println(classes);
    }

}
