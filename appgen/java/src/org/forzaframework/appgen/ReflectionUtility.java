package org.forzaframework.appgen;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * User: cesarreyes
 * Date: 24-dic-2006
 * Time: 18:31:20
 * Description:
 */
public class ReflectionUtility {

    public static List<Class> getClasses(String jarPath, String packageName) {
        List<Class> classes = new ArrayList<Class>();
//        String jarName = "lib/dao.jar";

        packageName = packageName.replaceAll("\\.", "/");
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
            File file = new File(jarPath);
            String url = file.toURL().toString();
            URL u = new URL(url);
            URLClassLoader cld = new URLClassLoader(new URL[]{u}, Thread.currentThread().getContextClassLoader());

            while (true) {
                JarEntry jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {

                    String className = jarEntry.getName().replaceAll("/", "\\.");
                    className = className.substring(0, className.length() - 6);
                    Class cl = cld.loadClass(className);
                    classes.add(cl);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
