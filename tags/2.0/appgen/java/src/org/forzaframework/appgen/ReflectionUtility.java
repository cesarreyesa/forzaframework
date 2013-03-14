/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
