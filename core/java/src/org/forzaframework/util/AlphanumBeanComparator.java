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

package org.forzaframework.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

/**
 * User: christian.tavera
 * Date: 15/06/2010
 * Time: 11:03:30 AM
 */

/**
 * To convert to use Templates (Java 1.5+):
 * - Change "implements Comparator" to "implements Comparator<String>"
 * - Change "compare(Object o1, Object o2)" to "compare(String s1, String s2)"
 * - Remove the type checking and casting in compare().
 * <p/>
 * To use this class:
 * Use the static "sort" method from the java.util.Collections class:
 * Collections.sort(your list, new AlphanumComparator());
 */
public class AlphanumBeanComparator implements Comparator {
    /**
     * The property name.
     * <p/>
     * Must start with a lower-case letter as per the JavaBeans specification.
     */
    private String property;
    private String direction;

    public AlphanumBeanComparator(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public int compare(Object o1, Object o2) {
        // Get the value of the properties
        Object p1 = null;
        Object p2 = null;
        try {
            p1 = PropertyUtils.getProperty(o1, property);
            p2 = PropertyUtils.getProperty(o2, property);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }

//        if (!(o1 instanceof String) || !(o2 instanceof String)) {
//            return 0;
//        }
        String s1 = (String) p1;
        String s2 = (String) p2;

        int thisMarker = 0;
        int thatMarker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while (thisMarker < s1Length && thatMarker < s2Length) {
            String thisChunk = getChunk(s1, s1Length, thisMarker);
            thisMarker += thisChunk.length();

            String thatChunk = getChunk(s2, s2Length, thatMarker);
            thatMarker += thatChunk.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0))) {
                // Simple chunk comparison by length.
                int chunkLength;
                if (!"DESC".equalsIgnoreCase(direction)) {
                    chunkLength = thisChunk.length();
                    result = chunkLength - thatChunk.length();
                } else {
                    chunkLength = thatChunk.length();
                    result = chunkLength - thisChunk.length();
                }

                // If equal, the first different number counts
                if (result == 0) {
                    for (int i = 0; i < chunkLength; i++) {
                        if (!"DESC".equalsIgnoreCase(direction)) {
                            result = thisChunk.charAt(i) - thatChunk.charAt(i);
                        } else {
                            result = thatChunk.charAt(i) - thisChunk.charAt(i);
                        }

                        if (result != 0) {
                            return result;
                        }
                    }
                }
            } else {
                if (!"DESC".equalsIgnoreCase(direction)) {
                    result = thisChunk.compareTo(thatChunk);
                } else {
                    result = thatChunk.compareTo(thisChunk);
                }
            }

            if (result != 0)
                return result;
        }

        if (!"DESC".equalsIgnoreCase(direction)) {
            return s1Length - s2Length;
        } else {
            return s2Length - s1Length;
        }
    }

    /**
     * Length of string is passed in for improved efficiency (only need to calculate it once) *
     */
    private final String getChunk(String s, int slength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (isDigit(c)) {
            while (marker < slength) {
                c = s.charAt(marker);
                if (!isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        } else {
            while (marker < slength) {
                c = s.charAt(marker);
                if (isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    private final boolean isDigit(char ch) {
        return ch >= 48 && ch <= 57;
    }
}
